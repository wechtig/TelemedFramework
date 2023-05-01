package at.telemed.symptom.telemedframeworksymptom;

import at.telemed.symptom.telemedframeworksymptom.dtos.Symptom;
import at.telemed.symptom.telemedframeworksymptom.services.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SymptomsController {

    @Autowired
    private SymptomService symptomService;

    @GetMapping("/symptoms-list")
    public List<Symptom> getSymptoms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return symptomService.readSymptoms(authentication.getName());
    }

    @GetMapping("/current")
    public String getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/symptom-save")
    public void processForm(@RequestBody List<Symptom> symptoms) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()) {
            symptomService.saveSyptomList(symptoms, authentication.getName());
        }
    }
}
