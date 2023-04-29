package at.fh.telemed.telemedframeworkroot.repositories;

import at.fh.telemed.telemedframeworkroot.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmail(String email);

    @Query("select u from UserEntity u where u.username = :username")
    UserEntity findByUsername(String username);

}