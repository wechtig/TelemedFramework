package at.fh.telemed.telemedframeworkroot.controller;

import at.fh.telemed.telemedframeworkroot.dtos.MedUser;
import at.fh.telemed.telemedframeworkroot.dtos.NewDoctorDTO;
import at.fh.telemed.telemedframeworkroot.dtos.Roles;
import at.fh.telemed.telemedframeworkroot.services.UserSecureSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestUserController {

    @Autowired
    private UserSecureSecurityService userSecureSecurityService;

    @GetMapping("/current")
    public String getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
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

    @PostMapping("/doctor-save")
    public void saveDoctor(@RequestBody NewDoctorDTO newDoctorDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        if(!authorities.get(0).getAuthority().equals("DOCTOR")) {
            return;
        }

        MedUser medUser = new MedUser();
        medUser.setRole(Roles.DOCTOR.name());
        medUser.setPassword(newDoctorDTO.getPassword());
        medUser.setUsername(newDoctorDTO.getUsername());
        medUser.setBirthDate(newDoctorDTO.getBirthdate());
        medUser.setEmail(newDoctorDTO.getMail());
        medUser.setLastName(newDoctorDTO.getLastName());
        newDoctorDTO.setFirstName(newDoctorDTO.getFirstName());

        userSecureSecurityService.saveUser(medUser);
    }
}
