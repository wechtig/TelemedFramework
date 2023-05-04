package at.telemedcourse.telemedframeworkcourse.repositories;

import at.telemedcourse.telemedframeworkcourse.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {
}
