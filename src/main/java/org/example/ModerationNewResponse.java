package org.example;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// This class is used to store the response from the moderation API
public class ModerationNewResponse {

    private String id; //Stores the id of the moderation request
    private String model; //Stores the model used for moderation
    private List<ModerationResult> results; //Stores the results of the moderation process

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("results")
    public List<ModerationResult> getResults() {
        return results;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setResults(List<ModerationResult> results) {
        this.results = results;
    }
}
