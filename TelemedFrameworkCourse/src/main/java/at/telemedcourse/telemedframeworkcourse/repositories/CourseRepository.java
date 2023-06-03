package at.telemedcourse.telemedframeworkcourse.repositories;

import at.telemedcourse.telemedframeworkcourse.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {
    @Query
    CourseEntity getByName(String name);
    @Query()
    CourseEntity getByUid(UUID uid);
}

