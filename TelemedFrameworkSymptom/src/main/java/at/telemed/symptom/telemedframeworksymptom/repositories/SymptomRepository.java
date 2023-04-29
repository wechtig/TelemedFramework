package at.telemed.symptom.telemedframeworksymptom.repositories;

import at.telemed.symptom.telemedframeworksymptom.entities.SymptomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SymptomRepository extends JpaRepository<SymptomEntity, UUID> {
    SymptomEntity findByUserId(UUID userId);
}