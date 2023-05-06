package com.example.telemedframeworkappointment.dtos;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentDto {
    private UUID id;
    private String usernamePatient;
    private String usernameDoctor;
    private String fullnamePatient;
    private String fullnameDoctor;
    private String description;
    private boolean accepted;
    private String location;
    private String date;

    public AppointmentDto() {

    }

    public AppointmentDto(String usernamePatient, String usernameDoctor, String fullnamePatient, String fullnameDoctor, String description, boolean accepted, String location, String date) {
        this.usernamePatient = usernamePatient;
        this.usernameDoctor = usernameDoctor;
        this.fullnamePatient = fullnamePatient;
        this.fullnameDoctor = fullnameDoctor;
        this.description = description;
        this.accepted = accepted;
        this.location = location;
        this.date = date;
    }

    public String getUsernamePatient() {
        return usernamePatient;
    }

    public void setUsernamePatient(String usernamePatient) {
        this.usernamePatient = usernamePatient;
    }

    public String getUsernameDoctor() {
        return usernameDoctor;
    }

    public void setUsernameDoctor(String usernameDoctor) {
        this.usernameDoctor = usernameDoctor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullnamePatient() {
        return fullnamePatient;
    }

    public void setFullnamePatient(String fullnamePatient) {
        this.fullnamePatient = fullnamePatient;
    }

    public String getFullnameDoctor() {
        return fullnameDoctor;
    }

    public void setFullnameDoctor(String fullnameDoctor) {
        this.fullnameDoctor = fullnameDoctor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
