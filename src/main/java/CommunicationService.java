import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CommunicationService {

    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private Duration timeout = Duration.ofSeconds(ConfigLoader.getEnvOrElse("REQUEST_TIMEOUT_SECONDS", DEFAULT_TIMEOUT_SECONDS));

    private final HttpClient client;

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

    HttpResponse sendRequest(HttpRequest request)  {
        try {
            return this.client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
