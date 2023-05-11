package at.telemed.symptom.telemedframeworksymptom;

import at.telemed.symptom.telemedframeworksymptom.dtos.Symptom;
import at.telemed.symptom.telemedframeworksymptom.repositories.UserRepository;
import at.telemed.symptom.telemedframeworksymptom.services.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SymptomsController {

    @Autowired
    private SymptomService symptomService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/symptoms-list")
    public List<Symptom> getSymptoms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return symptomService.readSymptoms(authentication.getName());
    }

    @GetMapping("/symptoms-list/{username}")
    public List<Symptom> getSymptomsByUser(@PathVariable String username) {
        return symptomService.readSymptoms(username);
    }

    @GetMapping("/usernames")
    public List<String> getUsernames() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        if(authorities.get(0).getAuthority().equals("DOCTOR")) {
            return userRepository.getUsernames();
        }

        return new ArrayList<>();
    }

    @GetMapping("/current-role")
    public String getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority);

        if(authorities.isPresent()) {
            return authorities.get();
        }

        return "";
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

    @PostMapping("/symptom-save/{username}")
    public void symtomSaveForPatient(@PathVariable String username, @RequestBody List<Symptom> symptoms) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()) {
            symptomService.saveSyptomList(symptoms, username);
        }
    }
}
