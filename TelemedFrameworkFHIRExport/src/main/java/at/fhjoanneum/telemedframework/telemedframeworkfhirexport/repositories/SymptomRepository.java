package at.fhjoanneum.telemedframework.telemedframeworkfhirexport.repositories;

import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.entities.SymptomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SymptomRepository extends JpaRepository<SymptomEntity, UUID> {
    List<SymptomEntity> findByUsername(String username);
}