package at.fh.telemed.telemedframeworkroot.repositories;

import at.fh.telemed.telemedframeworkroot.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    @Query("select r from RoleEntity r where r.role = :role")
    RoleEntity findByRole(String role);
}
