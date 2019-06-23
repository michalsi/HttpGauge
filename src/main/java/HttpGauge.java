import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class HttpGauge {

    private final static long MILLISECONDS = 1000;
    private final String DEFAULT_URL = PropertiesLoader.getProperty("defaultUrl");
    private String url = ConfigLoader.getEnvOrElse("URL", DEFAULT_URL);
    private final long DEFAULT_POLLING_TIME_SECONDS = Long.valueOf(PropertiesLoader.getProperty("defaultPoolingTimeSeconds"));
    private long pollingTime = ConfigLoader.getEnvOrElse("POLLING_TIME_SECONDS", DEFAULT_POLLING_TIME_SECONDS) * MILLISECONDS;

    private ShutdownListener shutdownListener;
    private Timer timer;
    private CsvHandler csv;
    private CommunicationService communicationService;

    HttpGauge() {
        communicationService = new CommunicationService();
        timer = new Timer();
        csv = new CsvHandler(PropertiesLoader.getProperty("resultFilePath"));
    }

    void execute(){
        executeMeasurement(communicationService.buildRequest(url));
    }

    private void executeMeasurement(HttpRequest request) {
        try {
            shutdownListener = new ShutdownListener();
            while (true) {
                timer.start();
                HttpResponse response = communicationService.sendRequest(request);
                timer.stop();
                saveResponse(timer, csv, response);
                checkGracefulShutdown();
            }
        } catch (InterruptedException e) {
            System.out.println("Program is shutting down");
        } finally {
            csv.close();
        }
    }

    private void checkGracefulShutdown() throws InterruptedException {
        if (pollingTime > MILLISECONDS) {
            long timeIncrement = MILLISECONDS;
            for (long timeProbing = 0; timeProbing <= pollingTime; timeProbing+=timeIncrement){
                checkUserInputForShutdown();
                Thread.sleep(timeIncrement);
            }
        }else{
            checkUserInputForShutdown();
            Thread.sleep(pollingTime);
        }
    }

    private void checkUserInputForShutdown() throws InterruptedException {
        if (!shutdownListener.isRunning()) {
            new Thread(shutdownListener).start();
        } else if (shutdownListener.shouldExit()) {
            throw new InterruptedException();
        }
    }

    private static void saveResponse(Timer timer, CsvHandler csv, HttpResponse response) {
        ResultObject result = new ResultObject();

        if (response != null) {
            result.setTimestamp(timer.getStartTime())
                    .setResponseTime(timer.getElapsedTimeInMilliseconds())
                    .setStatusCode(response.statusCode());
            csv.writeResult(result);

            System.out.println("Response code:\t " + response.statusCode());
            System.out.println("Response time:\t " + timer.getElapsedTimeInMilliseconds() + " ms");
        }
    }
}
