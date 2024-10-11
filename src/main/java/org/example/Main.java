package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String apiKey = "";

        ModerationService moderationService = new ModerationService(apiKey);

        String textToModerate = "I like jacky.";
        ModerationNewParams params = new ModerationNewParams(textToModerate);

        String textToModerate1 = "I hate you.";
        ModerationNewParams params1 = new ModerationNewParams(textToModerate1);

        String textToModerate2 = "I kill sb.";
        ModerationNewParams params2 = new ModerationNewParams(textToModerate2);

        String textToModerate3 = "I do not like you.";
        ModerationNewParams params3 = new ModerationNewParams(textToModerate3);

        try {
            ModerationNewResponse response = moderationService.moderate(params);
            processModerationResponse(response);

            ModerationNewResponse response1 = moderationService.moderate(params1);
            processModerationResponse(response1);

            ModerationNewResponse response2 = moderationService.moderate(params2);
            processModerationResponse(response2);

            ModerationNewResponse response3 = moderationService.moderate(params3);
            processModerationResponse(response3);

        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred while moderating the text: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void processModerationResponse(ModerationNewResponse response) {
        for (ModerationResult result : response.getResults()) {
            System.out.println("Full Response: " + response);
            System.out.println("Flagged: " + result.isFlagged());

            ModerationCategoryScores scores = result.getCategoryScores();
            System.out.println("Category Scores:");
            System.out.println("  Hate: " + scores.getHate());
            System.out.println("  Hate Threatening: " + scores.getHateThreatening());
            System.out.println("  Harassment: " + scores.getHarassment());
            System.out.println("  Harassment Threatening: " + scores.getHarassmentThreatening());
            System.out.println("  Self Harm: " + scores.getSelfHarm());
            System.out.println("  Sexual: " + scores.getSexual());
            System.out.println("  Violence: " + scores.getViolence());
            System.out.println("  Violence Graphic: " + scores.getViolenceGraphic());
            System.out.println("  Illicit: " + scores.getIllicit());
            System.out.println("  Illicit Violent: " + scores.getIllicitViolent());

            double sensitivityScore = calculateSensitivityScore(scores);

            System.out.println("Sensitivity Score: " + sensitivityScore);

            if (sensitivityScore >= 2) {
                System.out.println("Text is considered sensitive.");
            } else if (sensitivityScore >= 1 && sensitivityScore < 2) {
                System.out.println("Text is potentially sensitive, requiring manual review.");
            } else {
                System.out.println("Text is considered normal.");
            }
        }
    }

    private static double calculateSensitivityScore(ModerationCategoryScores scores) {
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