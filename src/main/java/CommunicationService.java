import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CommunicationService {

    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private static String url;
    private Duration timeout = Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS);

    private HttpClient client;

    CommunicationService() {
        client = HttpClient.newHttpClient();
    }

    public CommunicationService setTimeout(int timeout) {
        this.timeout = Duration.ofSeconds(timeout);
        return this;
    }

    HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .timeout(timeout)
                .build();
    }

    HttpResponse sendRequest(HttpRequest request) throws IOException, InterruptedException {
            return this.client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
