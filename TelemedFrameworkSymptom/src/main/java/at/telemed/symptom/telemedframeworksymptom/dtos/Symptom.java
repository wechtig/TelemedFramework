package at.telemed.symptom.telemedframeworksymptom.dtos;

import at.telemed.symptom.telemedframeworksymptom.services.SymptomService;

import java.util.UUID;

public class Symptom {
    private String symptom;
    private String description;
    private boolean active;

    public Symptom(String symptom, String description, boolean active) {
        this.symptom = symptom;
        this.description = description;
        this.active = active;
    }

    public Symptom() {

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
