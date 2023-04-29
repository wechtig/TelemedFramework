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
    public List<String> getSymptoms() {
        return symptomService.readSymptoms();
    }

    @GetMapping("/current")
    public String getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/symptom-save")
    public void processForm(@RequestBody List<Symptom> symptoms) {
       for(var symptom : symptoms) {

       }
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST, consumes="application/json")
    public void test(@RequestBody String text) {
       String tex = text;
    }
}
