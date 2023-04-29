package at.telemed.symptom.telemedframeworksymptom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class SymptomService {
    private static final String SYMPTOMS_FILE = "symptoms.txt";

    @Autowired
    private ResourceLoader resourceLoader;

    public List<String> readSymptoms() {
        Resource resource = resourceLoader.getResource("classpath:"+SYMPTOMS_FILE);
        try {
            return Files.readAllLines(resource.getFile().toPath())
                    .stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
