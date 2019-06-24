import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

class CommunicationService {

    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private static final Duration REQUEST_TIMEOUT_SECONDS =
            Duration.ofSeconds(ConfigLoader.getEnvOrElse("REQUEST_TIMEOUT_SECONDS", DEFAULT_TIMEOUT_SECONDS));

    private final HttpClient client;

    CommunicationService() {
        client = HttpClient.newHttpClient();
    }

    HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .timeout(REQUEST_TIMEOUT_SECONDS)
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
