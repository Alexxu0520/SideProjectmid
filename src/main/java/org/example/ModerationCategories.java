package org.example;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationCategories {
    @JsonProperty("hate")
    private boolean hate;

    @JsonProperty("hate/threatening")
    private boolean hateThreatening;

    @JsonProperty("harassment")
    private boolean harassment;

    @JsonProperty("harassment/threatening")
    private boolean HarassmentThreatening;

    @JsonProperty("self-harm")
    private boolean selfHarm;

    @JsonProperty("self-harm/intent")
    private boolean selfHarmIntent;

    @JsonProperty("self-harm/instructions")
    private boolean selfHarmInstructions;

    @JsonProperty("sexual")
    private boolean sexual;

    @JsonProperty("sexual/minors")
    private boolean sexualMinors;

    @JsonProperty("violence")
    private boolean violence;

    @JsonProperty("violence/graphic")
    private boolean violenceGraphic;

    @JsonProperty("illicit")
    private boolean illicit;

    @JsonProperty("illicit/violent")
    private boolean illicitViolent;

    public boolean isHate() {
        return hate;
    }

    public boolean isHateThreatening() {
        return hateThreatening;
    }

    public boolean isHarassment() {
        return harassment;
    }

    public boolean isHarassmentThreatening() {
        return HarassmentThreatening;
    }

    public boolean isSelfHarm() {
        return selfHarm;
    }

    public boolean isSelfHarmIntent() {
        return selfHarmIntent;
    }

    public boolean isSelfHarmInstructions() {
        return selfHarmInstructions;
    }

    public boolean isSexual() {
        return sexual;
    }

    public boolean isSexualMinors() {
        return sexualMinors;
    }

    public boolean isViolence() {
        return violence;
    }

    public boolean isViolenceGraphic() {
        return violenceGraphic;
    }

    public boolean isIllicit() {
        return illicit;
    }

    public boolean isIllicitViolent() {
        return illicitViolent;
    }

    public void setHate(boolean hate) {
        this.hate = hate;
    }

    public void setHateThreatening(boolean hateThreatening) {
        this.hateThreatening = hateThreatening;
    }

    public void setHarassment(boolean harassment) {
        this.harassment = harassment;
    }

    public void setHarassmentThreatening(boolean harassmentThreatening) {
        HarassmentThreatening = harassmentThreatening;
    }

    public void setSelfHarm(boolean selfHarm) {
        this.selfHarm = selfHarm;
    }

    public void setSelfHarmIntent(boolean selfHarmIntent) {
        this.selfHarmIntent = selfHarmIntent;
    }

    public void setSelfHarmInstructions(boolean selfHarmInstructions) {
        this.selfHarmInstructions = selfHarmInstructions;
    }

    public void setSexual(boolean sexual) {
        this.sexual = sexual;
    }

    public void setSexualMinors(boolean sexualMinors) {
        this.sexualMinors = sexualMinors;
    }

    public void setViolence(boolean violence) {
        this.violence = violence;
    }

    public void setViolenceGraphic(boolean violenceGraphic) {
        this.violenceGraphic = violenceGraphic;
    }

    public void setIllicit(boolean illicit) {
        this.illicit = illicit;
    }

    public void setIllicitViolent(boolean illicitViolent) {
        this.illicitViolent = illicitViolent;
    }
}
