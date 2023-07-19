package at.fhjoanneum.telemedframework.telemedframeworkfhirexport.services;

import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.entities.SymptomEntity;
import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.entities.UserEntity;
import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.repositories.SymptomRepository;
import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.repositories.UserRepository;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class FHIRMappingService {
    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String SYMPTOMS_FILE = "symptommapping.txt";

    @Autowired
    private ResourceLoader resourceLoader;

    public Patient getByNameFhir(String firstName, String lastName) {
        UserEntity userEntity = userRepository.getByName(firstName, lastName);;
        List<SymptomEntity> symptomEntityList = symptomRepository.findByUsername(userEntity.getUsername());

        Patient patient = new Patient();
    //    patient.setId(userEntity.getUid().toString());

        HumanName name = new HumanName();
        name.setUse(HumanName.NameUse.OFFICIAL);
        name.setFamily(userEntity.getLastName());
        name.addGiven(userEntity.getFirstName());
        patient.addName(name);

        patient.setExtension(getExtensionsSymptoms(symptomEntityList));
        return patient;
    }

    private List<Extension> getExtensionsSymptoms(List<SymptomEntity> symptomEntityList) {
        Map<String, String> loincSymptoms = new HashMap<>();
        Resource resource = resourceLoader.getResource("classpath:"+SYMPTOMS_FILE);
        List<Reference> references = new ArrayList<>();

        try {
            var allSymptoms = Files.readAllLines(resource.getFile().toPath())
                    .stream()
                    .map(String::trim)
                    .collect(Collectors.toList());

            for(var symptomLoinc : allSymptoms) {
                var symptomText = symptomLoinc.split("/")[0];
                var symptomCode = symptomLoinc.split("/")[1];
                loincSymptoms.put(symptomText, symptomCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int observationCounter = 1;
        for(SymptomEntity symptom : symptomEntityList) {
            String code = loincSymptoms.get(symptom.getSymptom());

            Observation observation = new Observation();
            observation.getStatusElement().setValueAsString("final");
            observation.getCode().addCoding()
                    .setSystem("http://loinc.org")
                    .setCode(code)
                    .setDisplay(symptom.getSymptom());
            observation.setId("observation"+observationCounter);
            Reference reference = new Reference();
            reference.setReference("Observation/" + observation.getId());
            reference.setType("Observation");
            reference.getIdentifier().setSystem("http://loinc.org").setValue(code);
            references.add(reference);

            observationCounter++;
        }


        List<Extension> extensions = new ArrayList<>();
        for(Reference reference : references) {
            Extension symptomsExtension = new Extension("http://telemedframework.com/fhir/extensions#observation-reference");
            symptomsExtension.setValue(reference);
            extensions.add(symptomsExtension);
        }

        return extensions;
    }
}
