package at.telemedcourse.telemedframeworkcourse.controller;

import at.telemedcourse.telemedframeworkcourse.entities.CourseEntity;
import at.telemedcourse.telemedframeworkcourse.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

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

    @GetMapping("/course")
    public List<CourseEntity> getAllCourses() {
        return courseRepository.findAll();
    }
}
