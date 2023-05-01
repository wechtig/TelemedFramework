package at.telemed.symptom.telemedframeworksymptom.services;

import at.telemed.symptom.telemedframeworksymptom.dtos.Symptom;
import at.telemed.symptom.telemedframeworksymptom.entities.SymptomEntity;
import at.telemed.symptom.telemedframeworksymptom.repositories.SymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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
    private ResourceLoader resourceLoader;

    public List<Symptom> readSymptoms(String username) {
        Resource resource = resourceLoader.getResource("classpath:"+SYMPTOMS_FILE);
        List<Symptom> symptoms = new ArrayList<>();
        try {
            var allSymptoms = Files.readAllLines(resource.getFile().toPath())
                        .stream()
                        .map(String::trim)
                        .collect(Collectors.toList());

            var activeSymptoms = symptomRepository.findByUsername(username);
            for(var symptomText : allSymptoms) {
                Symptom symptomDto = new Symptom();
                symptomDto.setSymptom(symptomText);
                symptomDto.setActive(false);

                for(var activeSymptom : activeSymptoms) {
                    if(symptomText.equals(activeSymptom.getSymptom())) {
                        symptomDto.setActive(true);
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
            symptomDto.setActive(true);
            extraSymptomsDto.add(symptomDto);
        }

        return extraSymptomsDto;
    }

    public void saveSyptomList(List<Symptom> symptoms, String username) {
        symptomRepository.deleteByUsername(username);
        for(Symptom symptom : symptoms) {
            if(symptom.isActive()) {
                SymptomEntity symptomEntity = new SymptomEntity();
                symptomEntity.setSymptom(symptom.getSymptom());
                symptomEntity.setUsername(username);
                symptomEntity.setDescription(symptom.getDescription());

                symptomRepository.save(symptomEntity);
            }
        }
    }
}
