package lk.binuri.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SMSService {
    @Value("${infobip.api.key}")
    private String apiKey;

    @Value("${infobip.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean sendSms(String to, String text) {
        String url = baseUrl + "/sms/2/text/advanced";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "App " + apiKey);
        Map<String, Object> message = new HashMap<>();
        message.put("from", "InfoSMS");
        message.put("destinations", List.of(Map.of("to",normalizeToE164(to) )));
        message.put("text", text);
        Map<String, Object> body = Map.of("messages", List.of(message));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getStatusCode().is2xxSuccessful();
    }

    public static String normalizeToE164(String mobile) {
        if (mobile == null || mobile.isBlank()) return null;

        // Remove spaces, dashes, etc.
        mobile = mobile.replaceAll("[^0-9+]", "");

        if (mobile.startsWith("+94")) {
            return mobile; // Already in E.164
        } else if (mobile.startsWith("94")) {
            return "+" + mobile; // Missing '+'
        } else if (mobile.startsWith("0")) {
            return "+94" + mobile.substring(1); // Local to international
        } else {
            return "+94" + mobile; // Assume no leading zero or country code
        }
    }
}
