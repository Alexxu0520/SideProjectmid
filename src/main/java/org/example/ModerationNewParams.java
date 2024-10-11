package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationNewParams {

    private Object input;
    private String model;

    public ModerationNewParams(Object input) {
        this.input = input;
    }

    public ModerationNewParams(Object input, String model) {
        this.input = input;
        this.model = model;
    }

    @JsonProperty("input")
    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
