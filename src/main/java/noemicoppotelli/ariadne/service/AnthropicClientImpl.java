package noemicoppotelli.ariadne.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import noemicoppotelli.ariadne.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class AnthropicClientImpl implements AnthropicClient {

    @Value("${anthropic.api-key:}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String generaTesto(String promptSistema, String testoUtente) {
        try {
            String body = mapper.writeValueAsString(Map.of(
                    "model", "claude-sonnet-5",
                    "max_tokens", 1024,
                    "system", promptSistema,
                    "messages", List.of(Map.of("role", "user", "content", testoUtente))
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.anthropic.com/v1/messages"))
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode json = mapper.readTree(response.body());

            for (JsonNode blocco : json.path("content")) {
                if ("text".equals(blocco.path("type").asText())) {
                    return blocco.path("text").asText();
                }
            }
            throw new BadRequestException("Nessun blocco di testo nella risposta AI");
        } catch (Exception e) {
            throw new BadRequestException("Generazione mappa fallita: " + e.getMessage());
        }
    }
}