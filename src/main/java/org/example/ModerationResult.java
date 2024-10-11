package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModerationResult {
    private ModerationCategories categories;
    private ModerationCategoryScores category_scores;
    private boolean flagged;

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
