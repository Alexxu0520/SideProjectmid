package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationCategoryScores {
    @JsonProperty("hate")
    private double hate;

    @JsonProperty("hate/threatening")
    private double hateThreatening;

    @JsonProperty("harassment")
    private double harassment;

    @JsonProperty("harassment/threatening")
    private double harassmentThreatening;

    @JsonProperty("self-harm")
    private double selfHarm;

    @JsonProperty("self-harm/intent")
    private double selfHarmIntent;

    @JsonProperty("self-harm/instructions")
    private double selfHarmInstructions;

    @JsonProperty("sexual")
    private double sexual;

    @JsonProperty("sexual/minors")
    private double sexualMinors;

    @JsonProperty("violence")
    private double violence;

    @JsonProperty("violence/graphic")
    private double violenceGraphic;

    @JsonProperty("illicit")
    private double illicit;

    @JsonProperty("illicit/violent")
    private double illicitViolent;

    public double getHate() {
        return hate;
    }

    public double getHateThreatening() {
        return hateThreatening;
    }

    public double getHarassment() {
        return harassment;
    }

    public double getHarassmentThreatening() {
        return harassmentThreatening;
    }

    public double getSelfHarm() {
        return selfHarm;
    }

    public double getSelfHarmIntent() {
        return selfHarmIntent;
    }

    public double getSelfHarmInstructions() {
        return selfHarmInstructions;
    }

    public double getSexual() {
        return sexual;
    }

    public double getSexualMinors() {
        return sexualMinors;
    }

    public double getViolence() {
        return violence;
    }

    public double getViolenceGraphic() {
        return violenceGraphic;
    }

    public double getIllicit() {
        return illicit;
    }

    public double getIllicitViolent() {
        return illicitViolent;
    }

    public void setHate(double hate) {
        this.hate = hate;
    }

    public void setHateThreatening(double hateThreatening) {
        this.hateThreatening = hateThreatening;
    }

    public void setHarassment(double harassment) {
        this.harassment = harassment;
    }

    public void setHarassmentThreatening(double harassmentThreatening) {
        this.harassmentThreatening = harassmentThreatening;
    }

    public void setSelfHarm(double selfHarm) {
        this.selfHarm = selfHarm;
    }

    public void setSelfHarmIntent(double selfHarmIntent) {
        this.selfHarmIntent = selfHarmIntent;
    }

    public void setSelfHarmInstructions(double selfHarmInstructions) {
        this.selfHarmInstructions = selfHarmInstructions;
    }

    public void setSexual(double sexual) {
        this.sexual = sexual;
    }

    public void setSexualMinors(double sexualMinors) {
        this.sexualMinors = sexualMinors;
    }

    public void setViolence(double violence) {
        this.violence = violence;
    }

    public void setViolenceGraphic(double violenceGraphic) {
        this.violenceGraphic = violenceGraphic;
    }

    public void setIllicit(double illicit) {
        this.illicit = illicit;
    }

    public void setIllicitViolent(double illicitViolent) {
        this.illicitViolent = illicitViolent;
    }
}
