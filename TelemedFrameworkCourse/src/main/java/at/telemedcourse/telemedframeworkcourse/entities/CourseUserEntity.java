package at.telemedcourse.telemedframeworkcourse.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "telemed_course_user")
public class CourseUserEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uid;

    private String username;
    @Column(name = "course_id")
    private String courseId;

    public CourseUserEntity(UUID uid, String username, String courseId) {
        this.uid = uid;
        this.username = username;
        this.courseId = courseId;
    }

    public CourseUserEntity() {

    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
