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
    private String course;

    public CourseUserEntity(UUID uid, String username, String course) {
        this.uid = uid;
        this.username = username;
        this.course = course;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
