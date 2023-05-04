package at.telemedcourse.telemedframeworkcourse.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "telemed_course_entry")
public class CourseEntryEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uid;
    @Column(name = "course_id")
    private UUID courseId;
    private String title;
    private String text;
    private byte[] attachment;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    public CourseEntryEntity(UUID uid, UUID courseId, String title, String text, byte[] attachment, LocalDateTime creationDate) {
        this.uid = uid;
        this.courseId = courseId;
        this.title = title;
        this.text = text;
        this.attachment = attachment;
        this.creationDate = creationDate;
    }

    public CourseEntryEntity() {}

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
