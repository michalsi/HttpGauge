import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpGauge {

    private static String url = "http://www.apache.org/";

    public static void main(String[] args) throws IOException, InterruptedException {
        CommunicationService communicationService = new CommunicationService();
        Timer timer = new Timer();
        CsvHandler csv = new CsvHandler("./target/results.csv");

        HttpRequest request = communicationService.buildRequest(url);

        timer.start();
        HttpResponse response;

        response = communicationService.sendRequest(request);

        timer.stop();

        ResultObject result = new ResultObject();
        result.setTimestamp(
                timer.getStartTime())
                .setResponseTime(timer.getElapsedTimeInMilliseconds())
                .setStatusCode(response.statusCode());
        csv.writeResult(result);

        System.out.println("Response code:\t " + response.statusCode());
        System.out.println("Response time:\t " + timer.getElapsedTimeInMilliseconds() + " ms");
        csv.close();

    }
}
