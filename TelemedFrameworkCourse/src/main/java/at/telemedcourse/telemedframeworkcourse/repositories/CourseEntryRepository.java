package at.telemedcourse.telemedframeworkcourse.repositories;

import at.telemedcourse.telemedframeworkcourse.entities.CourseEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CourseEntryRepository extends JpaRepository<CourseEntryEntity, UUID> {
    @Query
    List<CourseEntryEntity> getByCourseId(UUID courseID);

    @Query
    CourseEntryEntity getByFilename(String filename);
}
