package lk.binuri.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class RmvMockApi {

    private static final String RMV_SITE_URL = "http://localhost:9090/verify-vehicle-details";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean verify(String vehicleNo, String chassisNo, String type) {
        Map<String, String> payload = new HashMap<>();
        payload.put("vehicleNo", vehicleNo);
        payload.put("chassisNo", chassisNo);
        payload.put("type", type);

        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RMV_SITE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode jsonNode = objectMapper.readTree(response.body());
            return jsonNode.path("success").asBoolean(false); // default false if missing

        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}