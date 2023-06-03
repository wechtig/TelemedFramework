package at.telemedcourse.telemedframeworkcourse.repositories;

import at.telemedcourse.telemedframeworkcourse.entities.CourseEntity;
import at.telemedcourse.telemedframeworkcourse.entities.CourseUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface CourseUserRepository extends JpaRepository<CourseUserEntity, UUID> {
    @Query
    List<CourseUserEntity> getByUsername(String username);
    @Query
    CourseUserEntity getByUsernameAndCourse(String username, String course);
    @Modifying
    @Transactional
    @Query("delete from CourseUserEntity c where c.username = :username AND c.course = :course")
    void deleteByUserAndCourse(String username, String course);


}
