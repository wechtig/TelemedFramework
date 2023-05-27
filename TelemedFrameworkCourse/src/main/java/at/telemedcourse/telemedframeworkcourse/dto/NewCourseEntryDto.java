package at.telemedcourse.telemedframeworkcourse.dto;

public class NewCourseEntryDto {
    private String courseName;
    private String text;
    private String title;
    private String attachment;

    private String filename;

    public NewCourseEntryDto(String courseName, String text, String title, String attachment, String filename) {
        this.courseName = courseName;
        this.text = text;
        this.title = title;
        this.attachment = attachment;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
