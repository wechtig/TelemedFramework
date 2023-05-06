package at.telemed.symptom.telemedframeworksymptom.repositories;

import at.telemed.symptom.telemedframeworksymptom.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select u.username from UserEntity u where u.role not like 'DOCTOR'")
    List<String> getUsernames();
}
