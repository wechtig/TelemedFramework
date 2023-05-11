package at.fhjoanneum.telemedframework.telemedframeworkfhirexport.repositories;

import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select u from UserEntity u where u.firstName = :firstName and u.lastName = :lastName")
    UserEntity getByName(String firstName, String lastName);
}
