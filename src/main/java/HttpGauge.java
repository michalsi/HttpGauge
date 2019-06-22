import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpGauge {

    private static String url = "http://www.apache.org/";

    public static void main(String[] args) {

        CommunicationService communicationService = new CommunicationService();
        Timer timer = new Timer();
        CsvHandler csv = new CsvHandler("./target/results.csv");

        HttpRequest request = communicationService.buildRequest(url);
        HttpResponse response;
        ResultObject result = new ResultObject();

        try {
            ShutdownListener shutdownListener = new ShutdownListener();

            while (true) {
                timer.start();
                response = communicationService.sendRequest(request);
                timer.stop();

                if (response != null) {
                    result.setTimestamp(timer.getStartTime())
                            .setResponseTime(timer.getElapsedTimeInMilliseconds())
                            .setStatusCode(response.statusCode());
                    csv.writeResult(result);

                    System.out.println("Response code:\t " + response.statusCode());
                    System.out.println("Response time:\t " + timer.getElapsedTimeInMilliseconds() + " ms");
                }
                Thread.sleep(1000);

                if (!shutdownListener.isRunning()) {
                    new Thread(shutdownListener).start();

                } else if (shutdownListener.shouldExit()) {
                    throw new InterruptedException();
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Program is shutting down");
            csv.close();
        }
    }
}
