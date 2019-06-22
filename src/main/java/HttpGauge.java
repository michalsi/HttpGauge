import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpGauge {

    public static void main(String[] args) {

        CommunicationService communicationService = new CommunicationService();
        Timer timer = new Timer();
        CsvHandler csv = new CsvHandler(PropertiesLoader.getProperty("resultFilePath"));

        String url = "http://www.apache.org/";
        HttpRequest request = communicationService.buildRequest(url);
        HttpResponse response;


        try {
            ShutdownListener shutdownListener = new ShutdownListener();

            while (true) {
                timer.start();
                response = communicationService.sendRequest(request);
                timer.stop();

                saveResponse(timer, csv, response);
                Thread.sleep(5000);

                if (!shutdownListener.isRunning()) {
                    new Thread(shutdownListener).start();

                } else if (shutdownListener.shouldExit()) {
                    throw new InterruptedException();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Program is shutting down");
        } finally {
            csv.close();
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
