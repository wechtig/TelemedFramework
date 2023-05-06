package com.example.telemedframeworkappointment.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "telemed_appointment")
public class AppointmentEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uid;

    @Column(name = "username_patient")
    private String usernamePatient;

    @Column(name = "username_doctor ")
    private String usernameDoctor;

    private String description;

    private boolean accepted;

    @Column(name = "location")
    private String location;

    @Column(name = "date")
    private LocalDateTime date;

    public AppointmentEntity() {}

    public AppointmentEntity(UUID uid, String usernamePatient, String usernameDoctor, String description, boolean accepted, String location, LocalDateTime date) {
        this.uid = uid;
        this.usernamePatient = usernamePatient;
        this.usernameDoctor = usernameDoctor;
        this.description = description;
        this.accepted = accepted;
        this.location = location;
        this.date = date;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
