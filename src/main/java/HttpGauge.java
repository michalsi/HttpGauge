import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class HttpGauge {
    private final static long MILLISECONDS = 1000;
    private final static String DEFAULT_URL = PropertiesLoader.getProperty("defaultUrl");
    private final static long DEFAULT_POLLING_TIME_SECONDS = Long.valueOf(PropertiesLoader.getProperty("defaultPoolingTimeSeconds"));
    private final static String RESULT_FILE_PATH = PropertiesLoader.getProperty("resultFilePath");

    private String url;
    private long pollingTime;

    private ShutdownListener shutdownListener;
    private Timer timer;
    private CsvHandler csv;
    private CommunicationService communicationService;

    HttpGauge() {
        loadConfiguration();
        communicationService = new CommunicationService();
        timer = new Timer();
        csv = new CsvHandler(RESULT_FILE_PATH);
    }

    void execute() {
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
            for (long timeProbing = 0; timeProbing <= pollingTime; timeProbing += timeIncrement) {
                checkUserInputForShutdown();
                Thread.sleep(timeIncrement);
            }
        } else {
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

    private void saveResponse(Timer timer, CsvHandler csv, HttpResponse response) {
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

    private void loadConfiguration() {
        url = ConfigLoader.getEnvOrElse("URL", DEFAULT_URL);
        if (!isValidUrl(url))
            System.out.println("Bad URL: " + url);
        pollingTime = ConfigLoader.getEnvOrElse("POLLING_TIME_SECONDS", DEFAULT_POLLING_TIME_SECONDS) * MILLISECONDS;
        pollingTime = pollingTime < 0 ? DEFAULT_POLLING_TIME_SECONDS : pollingTime;
    }

    private boolean isValidUrl(String url) {
        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            u.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
