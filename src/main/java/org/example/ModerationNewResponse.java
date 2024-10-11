package org.example;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public class ModerationNewResponse {

    private String id;
    private String model;
    private List<ModerationResult> results;

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
