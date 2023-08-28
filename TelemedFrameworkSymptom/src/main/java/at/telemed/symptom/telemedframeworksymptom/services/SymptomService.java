package at.telemed.symptom.telemedframeworksymptom.services;

import at.telemed.symptom.telemedframeworksymptom.dtos.Symptom;
import at.telemed.symptom.telemedframeworksymptom.entities.SymptomEntity;
import at.telemed.symptom.telemedframeworksymptom.entities.UserEntity;
import at.telemed.symptom.telemedframeworksymptom.repositories.SymptomRepository;
import at.telemed.symptom.telemedframeworksymptom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class SymptomService {
    private static final String SYMPTOMS_FILE = "symptoms.txt";

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public List<Symptom> readSymptoms(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        Resource resource = resourceLoader.getResource("classpath:"+SYMPTOMS_FILE);
        List<Symptom> symptoms = new ArrayList<>();
        try {
            var allSymptoms = Files.readAllLines(resource.getFile().toPath())
                        .stream()
                        .map(String::trim)
                        .collect(Collectors.toList());

            var activeSymptoms = symptomRepository.findByUserId(userEntity.getUid().toString());
            for(var symptomText : allSymptoms) {
                Symptom symptomDto = new Symptom();
                symptomDto.setSymptom(symptomText);
                symptomDto.setActive(false);

                for(var activeSymptom : activeSymptoms) {
                    if(symptomText.equals(activeSymptom.getSymptom())) {
                        symptomDto.setActive(activeSymptom.isActive());
                        symptomDto.setDescription(activeSymptom.getDescription());
                    }
                }

                symptoms.add(symptomDto);
            }

            symptoms.addAll(getWeitereSymptome(activeSymptoms));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return symptoms;
    }

    private List<Symptom> getWeitereSymptome(List<SymptomEntity> activeSymptoms) {
        List<SymptomEntity> extraSymptoms = activeSymptoms.stream()
                .filter(symptom -> symptom.getSymptom().equals("Weitere Symptome"))
                .collect(Collectors.toList());

        List<Symptom> extraSymptomsDto = new ArrayList<>();

        for(SymptomEntity symptomEntity : extraSymptoms) {
            Symptom symptomDto = new Symptom();
            symptomDto.setSymptom(symptomEntity.getSymptom());
            symptomDto.setDescription(symptomEntity.getDescription());
            symptomDto.setActive(symptomEntity.isActive());
            extraSymptomsDto.add(symptomDto);
        }

        return extraSymptomsDto;
    }

    public void saveSyptomList(List<Symptom> symptoms, String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        var activeSymptoms = symptomRepository.findByUserId(userEntity.getUid().toString());
        symptomRepository.deleteByUserId(userEntity.getUid().toString());

        for(Symptom symptom : symptoms) {
            boolean found = false;
            for(SymptomEntity symptomEntity : activeSymptoms) {
                if(!found && symptom.getSymptom().equals(symptomEntity.getSymptom())) {
                    found = true;
                    SymptomEntity newSymptom = new SymptomEntity();
                    newSymptom.setSymptom(symptom.getSymptom());
                    newSymptom.setUserId(userEntity.getUid().toString());
                    newSymptom.setDescription(symptom.getDescription());
                    newSymptom.setActive(symptom.isActive());
                    newSymptom.setCreateDate(symptomEntity.getCreateDate());
                    symptomRepository.save(newSymptom);
                }
            }

            if(!found && symptom.isActive()) {
                SymptomEntity symptomEntity = new SymptomEntity();
                symptomEntity.setSymptom(symptom.getSymptom());
                symptomEntity.setUserId(userEntity.getUid().toString());
                symptomEntity.setDescription(symptom.getDescription());
                symptomEntity.setActive(symptom.isActive());
                symptomEntity.setCreateDate(LocalDate.now());
                symptomRepository.save(symptomEntity);
            }

        }
    }

    public List<Symptom> readSymptomsByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        var entities = symptomRepository.findByUserId(userEntity.getUid().toString());
        List<Symptom> symptoms = new ArrayList<>();

        for(SymptomEntity symptomEntity : entities) {
            Symptom symptomDto = new Symptom();
            symptomDto.setSymptom(symptomEntity.getSymptom());
            symptomDto.setDescription(symptomEntity.getDescription());
            symptomDto.setActive(symptomEntity.isActive());
            symptoms.add(symptomDto);
        }

        return symptoms;
    }
}
