package spring.educhainminiapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GeminiService { 

    private final RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    public String sessionId = String.valueOf(1234);

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateContent(String prompt) {
        String url = String.format("%s/generate?prompt=%s&sessionId=%s", geminiApiUrl, prompt, sessionId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to call API: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while calling Gemini API: " + e.getMessage());
        }

        return response.getBody();
    }

    public List<String> getHistory() {
        String apiUrl = geminiApiUrl + "/history?sessionId=" + sessionId;
        ResponseEntity<List> response = restTemplate.getForEntity(apiUrl, List.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to retrieve history: " + response.getStatusCode());
        }
    }

    public void clearHistory() {
        String apiUrl = geminiApiUrl + "/clear?sessionId=" + sessionId;
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to clear history: " + response.getStatusCode());
        }
    }
}

