package org.example;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ModerationService {
    private static final String API_URL = "https://api.openai.com/v1/moderations";
    private final String apiKey = "";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ModerationNewResponse moderate(ModerationNewParams params) throws IOException, InterruptedException {
        String requestBody = new ObjectMapper().writeValueAsString(params);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new ObjectMapper().readValue(response.body(), ModerationNewResponse.class);
        } else {
            throw new IOException("Error during API call: " + response.body());
        }
    }

    public double calculateSensitivityScore(ModerationCategoryScores scores) {
        double totalScore = 0.0;
        int count = 0;

        totalScore += scores.getHate() * 2;
        count++;
        totalScore += scores.getHateThreatening() * 2;
        count++;
        totalScore += scores.getHarassment();
        count++;
        totalScore += scores.getHarassmentThreatening();
        count++;
        totalScore += scores.getSelfHarm();
        count++;
        totalScore += scores.getSexual();
        count++;
        totalScore += scores.getViolence() * 2;
        count++;
        totalScore += scores.getIllicit();
        count++;
        totalScore += scores.getViolenceGraphic() * 2;
        count++;
        totalScore += scores.getIllicitViolent();
        count++;

        return (totalScore / count) * 100;
    }
}