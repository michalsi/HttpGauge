import com.google.inject.Inject;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class HttpGauge {

    public static void main(String[] args) {

        final String DEFAULT_URL = PropertiesLoader.getProperty("defaultUrl");
        String url = ConfigLoader.getEnvOrElse("URL", DEFAULT_URL);

        final long MILLISECONDS = 1000;
        final long DEFAULT_POLLING_TIME_SECONDS = 60;
        long pollingTime = ConfigLoader.getEnvOrElse("POLLING_TIME_SECONDS", DEFAULT_POLLING_TIME_SECONDS) * MILLISECONDS;

        CommunicationService communicationService = new CommunicationService();
        Timer timer = new Timer();
        CsvHandler csv = new CsvHandler(PropertiesLoader.getProperty("resultFilePath"));

        HttpRequest request = communicationService.buildRequest(url);
        HttpResponse response;
        System.out.println("url" + url);
        try {
            ShutdownListener shutdownListener = new ShutdownListener();

            while (true) {
                timer.start();
                response = communicationService.sendRequest(request);
                timer.stop();

                saveResponse(timer, csv, response);
                Thread.sleep(pollingTime);

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
