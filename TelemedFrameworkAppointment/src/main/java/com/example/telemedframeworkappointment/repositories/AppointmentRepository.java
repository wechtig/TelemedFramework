package com.example.telemedframeworkappointment.repositories;

import com.example.telemedframeworkappointment.entities.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {
    List<AppointmentEntity> findByPatientId(String patientId);

    @Modifying
    @Transactional
    @Query("update AppointmentEntity set accepted = true where patientId = :patientId and date = :dateTime")
    void setAcceptedForPatient(String patientId, LocalDateTime dateTime);
}
