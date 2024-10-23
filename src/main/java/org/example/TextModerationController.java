package org.example;

import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/text")
public class TextModerationController {

    private final ModerationService moderationService;

    // In-memory storage for annotations and scores
    private final Map<String, ModerationNewResponse> annotationsStore = new HashMap<>();
    private final Map<String, ModerationNewResponse> scoresStore = new HashMap<>();
    private int idCounter = 1;

    public TextModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    // POST /text/annotate
    @PostMapping("/annotate")
    public Map<String, Object> annotateText(@RequestBody Map<String, String> requestBody) throws IOException, InterruptedException {
        String text = requestBody.get("text");
        ModerationNewParams params = new ModerationNewParams(text);
        ModerationNewResponse response = moderationService.moderate(params);

        // Store annotation result with an auto-generated ID
        String id = String.valueOf(idCounter++);
        annotationsStore.put(id, response);

        // Return annotation result along with the generated ID
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("categories", response.getResults().get(0).getCategories());
        result.put("flagged", response.getResults().get(0).isFlagged());
        return result;
    }

    // GET /text/annotations/{id}
    @GetMapping("/annotations/{id}")
    public ModerationNewResponse getAnnotation(@PathVariable String id) {
        // Retrieve the annotation result by ID
        ModerationNewResponse response = annotationsStore.get(id);
        if (response != null) {
            return response;
        } else {
            throw new IllegalArgumentException("No annotation found with ID: " + id);
        }
    }

    // POST /text/score
    @PostMapping("/score")
    public Map<String, Object> scoreText(@RequestBody Map<String, String> requestBody) throws IOException, InterruptedException {
        String text = requestBody.get("text");
        ModerationNewParams params = new ModerationNewParams(text);
        ModerationNewResponse response = moderationService.moderate(params);

        // Calculate the sensitivity score
        double sensitivityScore = calculateSensitivityScore(response.getResults().get(0).getCategoryScores());

        // Store score result with an auto-generated ID
        String id = String.valueOf(idCounter++);
        scoresStore.put(id, response);

        // Return score result along with the generated ID
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("category_scores", response.getResults().get(0).getCategoryScores());
        result.put("sensitivity_score", sensitivityScore);
        return result;
    }

    // GET /text/score/{id}
    @GetMapping("/score/{id}")
    public ModerationNewResponse getScore(@PathVariable String id) {
        // Retrieve the score result by ID
        ModerationNewResponse response = scoresStore.get(id);
        if (response != null) {
            return response;
        } else {
            throw new IllegalArgumentException("No score found with ID: " + id);
        }
    }

    // Helper function to calculate sensitivity score
    private double calculateSensitivityScore(ModerationCategoryScores scores) {
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