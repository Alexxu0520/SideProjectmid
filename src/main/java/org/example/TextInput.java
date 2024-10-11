package org.example;

// This class is used to store the text input for the moderation API
public class TextInput {

    private String text;
    private String type = "text";

    public TextInput(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public  void setText(String text) {
        this.text =text;
    }
}
