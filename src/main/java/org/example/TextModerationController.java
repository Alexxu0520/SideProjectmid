package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/text")
public class TextModerationController {

    private final ModerationService moderationService;
    private final HistoryService historyService;
    private final UserService userService;
    private final SharedRecordsService sharedRecordsService;

    @Autowired
    public TextModerationController(ModerationService moderationService, HistoryService historyService, UserService userService, SharedRecordsService sharedRecordsService) {
        this.moderationService = moderationService;
        this.historyService = historyService;
        this.userService = userService;
        this.sharedRecordsService = sharedRecordsService;
    }

    // POST /text/
    @PostMapping("/")
    public Map<String, Object> moderateText(@RequestBody Map<String, String> requestBody) throws IOException, InterruptedException {
        // Extract text and email from the request
        String text = requestBody.get("text");
        String email = requestBody.get("email");

        // Retrieve userId from email
        Integer userId = userService.getUserIdByEmail(email);
        if (userId == null) {
            throw new IllegalArgumentException("User with this email does not exist.");
        }

        // Perform moderation
        ModerationNewParams params = new ModerationNewParams(text);
        ModerationNewResponse response = moderationService.moderate(params);

        // Calculate sensitivity score
        double sensitivityScore = moderationService.calculateSensitivityScore(response.getResults().get(0).getCategoryScores());

        // Store moderation result in the Histories table with the retrieved userId
        int historyId = historyService.saveModerationResult(response, text, sensitivityScore, userId);

        // Return the stored history ID
        Map<String, Object> result = new HashMap<>();
        result.put("id", historyId);
        result.put("message", "Text moderated and stored successfully.");
        return result;
    }

    // GET /text/annotations/{id}
    @GetMapping("/annotations/{id}")
    public ModerationCategories getAnnotation(@PathVariable int id) {
        return historyService.getCategoriesById(id);
    }

    // GET /text/score/{id}
    @GetMapping("/score/{id}")
    public Map<String, Object> getScore(@PathVariable int id) {
        Map<String, Object> scoreResponse = new HashMap<>();
        scoreResponse.put("category_scores", historyService.getCategoryScoresById(id));
        scoreResponse.put("sensitivity_score", historyService.getSensitivityScoreById(id));
        return scoreResponse;
    }

    @GetMapping("/histories")
    public List<Map<String, Object>> getAllHistoriesByUserEmail(@RequestParam String email) {
        return historyService.getAllHistoriesByUserEmail(email);
    }

    // Endpoint to share a history with a friend
    @PostMapping("/share")
    public String shareHistory(@RequestBody ShareHistoryDTO shareHistoryDTO) {
        return sharedRecordsService.shareHistory(
                shareHistoryDTO.getUserEmail(),
                shareHistoryDTO.getFriendEmail(),
                shareHistoryDTO.getHistoryId()
        );
    }

    // Endpoint to get all shared records for a user
    @GetMapping("/shared")
    public List<HashMap<String, Object>> getAllSharedRecords(@RequestParam String email) {
        return sharedRecordsService.getAllSharedRecords(email);
    }

    // Endpoint to get shared record details
    @GetMapping("/shared/{historyId}")
    public HashMap<String, Object> getSharedRecordDetail(@PathVariable int historyId, @RequestParam String email) {
        return sharedRecordsService.getSharedRecordDetail(historyId, email);
    }
}