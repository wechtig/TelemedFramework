package at.telemed.symptom.telemedframeworksymptom.dtos;

import at.telemed.symptom.telemedframeworksymptom.services.SymptomService;

import java.util.UUID;

public class Symptom {
    private String symptom;
    private String description;

    public Symptom(String symptom, String description) {
        this.symptom = symptom;
        this.description = description;
    }

    public Symptom() {

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
