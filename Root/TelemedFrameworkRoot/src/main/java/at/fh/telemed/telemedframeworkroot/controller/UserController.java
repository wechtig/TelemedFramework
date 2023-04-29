package at.fh.telemed.telemedframeworkroot.controller;

import at.fh.telemed.telemedframeworkroot.dtos.MedUser;
import at.fh.telemed.telemedframeworkroot.dtos.MedUserRole;
import at.fh.telemed.telemedframeworkroot.dtos.Roles;
import at.fh.telemed.telemedframeworkroot.entities.RoleEntity;
import at.fh.telemed.telemedframeworkroot.repositories.RoleRepository;
import at.fh.telemed.telemedframeworkroot.services.UserSecureSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserSecureSecurityService secureSecurityService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping(value = "/test")
    public void createTestUser() {
        MedUser medUser = new MedUser();
        medUser.setEmail("test@user.at");
        medUser.setFirstName("test");
        medUser.setLastName("user");
        medUser.setUsername("testuser");
        medUser.setPassword("test");

        secureSecurityService.saveUser(medUser);
    }

    @PostMapping(value = "/save")
    public void create(final MedUser request) {
        RoleEntity patient = roleRepository.findByRole("PATIENT");
        MedUserRole medUserRole = new MedUserRole();
        medUserRole.setUid(patient.getUid());
        medUserRole.setRole(patient.getRole());
        request.setRole(medUserRole.getRole());

        if (secureSecurityService.findUserByUsername(request.getUsername()) != null) {
            return;
        }

        secureSecurityService.saveUser(request);
    }

    @GetMapping("/current")
    public String getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/token")
    public String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object o = authentication.getDetails();
        String sessionId = ((WebAuthenticationDetails) o).getSessionId();
        return sessionId;
    }


}
