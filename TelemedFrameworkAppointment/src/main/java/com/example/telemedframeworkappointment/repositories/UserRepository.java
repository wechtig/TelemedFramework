package com.example.telemedframeworkappointment.repositories;

import com.example.telemedframeworkappointment.entities.AppointmentEntity;
import com.example.telemedframeworkappointment.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select u from UserEntity u where u.username = :username")
    UserEntity findByUsername(String username);
    @Query("select u from UserEntity u where u.lastName = :lastName")
    UserEntity findByLastname(String lastName);
}
