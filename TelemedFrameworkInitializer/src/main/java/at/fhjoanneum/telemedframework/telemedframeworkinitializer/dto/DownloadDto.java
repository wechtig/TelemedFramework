package at.fhjoanneum.telemedframework.telemedframeworkinitializer.dto;

public class DownloadDto {
    private boolean communication;
    private boolean symptom;
    private boolean appointment;
    private boolean course;
    private boolean export;

    public DownloadDto(boolean communication, boolean symptom, boolean appointment, boolean course, boolean export) {
        this.communication = communication;
        this.symptom = symptom;
        this.appointment = appointment;
        this.course = course;
        this.export = export;
    }

    public DownloadDto() {

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
}
