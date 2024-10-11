package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

// This class is used to store the result of the moderation process
public class ModerationResult {
    private ModerationCategories categories; //Stores the categories such as hate, violence, self-harm, etc.
    private ModerationCategoryScores category_scores; //Stores the scores for each category
    private boolean flagged; //Indicates whether the text is flagged for moderation

    @JsonProperty("categories")
    public ModerationCategories getCategories() {
        return categories;
    }

    @JsonProperty("category_scores")
    public ModerationCategoryScores getCategoryScores() {
        return category_scores;
    }

    @JsonProperty("flagged")
    public boolean isFlagged() {
        return flagged;
    }

    public void setCategories(ModerationCategories categories) {
        this.categories = categories;
    }

    public void setCategoryScores(ModerationCategoryScores category_scores) {
        this.category_scores = category_scores;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
