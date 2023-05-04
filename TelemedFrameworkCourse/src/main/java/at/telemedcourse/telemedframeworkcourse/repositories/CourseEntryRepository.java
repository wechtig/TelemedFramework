package at.telemedcourse.telemedframeworkcourse.repositories;

import at.telemedcourse.telemedframeworkcourse.entities.CourseEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseEntryRepository extends JpaRepository<CourseEntryEntity, UUID> {
}
