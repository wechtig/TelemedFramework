package at.fhjoanneum.telemedframework.telemedframeworkinitializer.dto;

import org.springframework.web.multipart.MultipartFile;

public class DownloadDto {
    private boolean communication;
    private boolean symptom;
    private boolean appointment;
    private boolean course;
    private boolean export;
    private String color;
    private String praxisname;
    private String logo;

    public DownloadDto(boolean communication, boolean symptom, boolean appointment, boolean course, boolean export) {
        this.communication = communication;
        this.symptom = symptom;
        this.appointment = appointment;
        this.course = course;
        this.export = export;
    }

    public DownloadDto() {

    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isCommunication() {
        return communication;
    }

    public void setCommunication(boolean communication) {
        this.communication = communication;
    }

    public boolean isSymptom() {
        return symptom;
    }

    public void setSymptom(boolean symptom) {
        this.symptom = symptom;
    }

    public boolean isAppointment() {
        return appointment;
    }

    public void setAppointment(boolean appointment) {
        this.appointment = appointment;
    }

    public boolean isCourse() {
        return course;
    }

    public void setCourse(boolean course) {
        this.course = course;
    }

    public boolean isExport() {
        return export;
    }

    public void setExport(boolean export) {
        this.export = export;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPraxisname() {
        return praxisname;
    }

    public void setPraxisname(String praxisname) {
        this.praxisname = praxisname;
    }
}
