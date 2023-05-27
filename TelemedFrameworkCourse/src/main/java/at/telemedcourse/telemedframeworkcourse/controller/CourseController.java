package at.telemedcourse.telemedframeworkcourse.controller;

import at.telemedcourse.telemedframeworkcourse.dto.NewCourseDto;
import at.telemedcourse.telemedframeworkcourse.dto.NewCourseEntryDto;
import at.telemedcourse.telemedframeworkcourse.entities.CourseEntity;
import at.telemedcourse.telemedframeworkcourse.entities.CourseEntryEntity;
import at.telemedcourse.telemedframeworkcourse.repositories.CourseEntryRepository;
import at.telemedcourse.telemedframeworkcourse.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseEntryRepository courseEntryRepository;

    @GetMapping("/current-role")
    public String getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority);

        if(authorities.isPresent()) {
            return authorities.get();
        }

        return "";
    }

    @GetMapping("/active-modules")
    public String getActiveModules() {
        try {
            String activeModulesString = "";
            Properties props = PropertiesLoaderUtils.loadProperties(new FileSystemResource("telemed.properties"));
            var propsStr = props.toString().strip();
            return propsStr.replaceAll("}", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @GetMapping("/current")
    public String getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/newcourse")
    public void newCourse(@RequestBody NewCourseDto newCourseDto) {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setDescription(newCourseDto.getCourseDescription());
        courseEntity.setName(newCourseDto.getCourseName());
        courseRepository.save(courseEntity);
    }

    @PostMapping("/courseentry")
    public void newCourseEntry(@RequestBody NewCourseEntryDto newCourseDto) {
        CourseEntity courseEntity = courseRepository.getByName(newCourseDto.getCourseName());

        if(courseEntity == null) {
            return;
        }

        CourseEntryEntity courseEntryEntity = new CourseEntryEntity();
        courseEntryEntity.setCourseId(courseEntity.getUid());
        courseEntryEntity.setText(newCourseDto.getText());
        courseEntryEntity.setTitle(newCourseDto.getTitle());
        courseEntryEntity.setCreationDate(LocalDateTime.now());

        if(newCourseDto.getAttachment() != "") {
            String[] parts = newCourseDto.getFilename().split("\\\\");
            courseEntryEntity.setFilename(parts[parts.length-1]);

            String partSeparator = ",";
            String encodedImg = newCourseDto.getAttachment().split(partSeparator)[1];
            courseEntryEntity.setAttachment(Base64.getDecoder().decode(encodedImg));
        }

        courseEntryRepository.save(courseEntryEntity);
    }

    @GetMapping("/courseentry/{coursename}")
    public List<CourseEntryEntity> getAllCourses(@PathVariable String coursename) {
        CourseEntity courseEntity = courseRepository.getByName(coursename);
        return courseEntryRepository.getByCourseId(courseEntity.getUid());
    }

    @PostMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("filename.txt").build());

        CourseEntryEntity courseEntryEntity = courseEntryRepository.getByFilename(filename);
        return new ResponseEntity<>(courseEntryEntity.getAttachment(), headers, HttpStatus.OK);
    }

    @GetMapping("/logo")
    public String getLogo() {
        File file = new File("logo.png");
        byte[] fileContent = new byte[0];
        try {
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(fileContent);
    }

    @GetMapping("/course")
    public List<String> getAllCourses() {
        return courseRepository.findAll().stream().map(CourseEntity::getName).collect(Collectors.toList());
    }
}
