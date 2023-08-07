package at.telemed.symptom.telemedframeworksymptom.repositories;

import at.telemed.symptom.telemedframeworksymptom.entities.SymptomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface SymptomRepository extends JpaRepository<SymptomEntity, UUID> {
    List<SymptomEntity> findByUserId(String userId);
    @Modifying
    @Transactional
    @Query("delete from SymptomEntity s where s.userId = :userId")
    void deleteByUserId(String userId);
}