package at.telemedcourse.telemedframeworkcourse.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "telemed_course")
public class CourseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uid;

    @Column(name = "course_name")
    private String name;
    private String description;

    public CourseEntity(UUID uid, String name, String description) {
        this.uid = uid;
        this.name = name;
        this.description = description;
    }

    public CourseEntity() {}

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
