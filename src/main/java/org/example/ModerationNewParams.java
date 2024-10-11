package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
//The ModerationNewParams class is designed to represent the parameters for a content moderation request,we use text as our input but we can also use image as our input.
// It has two fields: input and model. The input can be any type of object (likely either text or an image), and model is a string specifying which model to use for the moderation.
@JsonInclude(JsonInclude.Include.NON_NULL)
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
