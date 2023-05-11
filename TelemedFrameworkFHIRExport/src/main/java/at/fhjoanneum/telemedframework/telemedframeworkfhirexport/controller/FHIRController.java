package at.fhjoanneum.telemedframework.telemedframeworkfhirexport.controller;

import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.entities.UserEntity;
import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.repositories.UserRepository;
import at.fhjoanneum.telemedframework.telemedframeworkfhirexport.services.FHIRMappingService;
import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.catalina.User;
import ca.uhn.fhir.parser.IParser;import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FHIRController {
    @Autowired
    private FHIRMappingService fhirMappingService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/patient/{firstName}/{lastName}/json")
    public UserEntity getByNameJson(@PathVariable String firstName, @PathVariable String lastName) {
        return userRepository.getByName(firstName, lastName);
    }

    @GetMapping(value = "/patient/{firstName}/{lastName}/fhir", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getByNameFhir(@PathVariable String firstName, @PathVariable String lastName) {
        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();
        Gson gson = new Gson();

        var patient = fhirMappingService.getByNameFhir(firstName, lastName);
        InstantType instantType = new InstantType();
        instantType.setValue(new Date());
        patient.getMeta().setLastUpdatedElement(instantType);

        String jsonString = parser.encodeResourceToString(patient);
        return jsonString;
    }

    @GetMapping("/all/json")
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }


}
