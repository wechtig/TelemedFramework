package com.example.telemedframeworkappointment.controller;

import com.example.telemedframeworkappointment.dtos.AppointmentDto;
import com.example.telemedframeworkappointment.entities.AppointmentEntity;
import com.example.telemedframeworkappointment.entities.UserEntity;
import com.example.telemedframeworkappointment.repositories.AppointmentRepository;
import com.example.telemedframeworkappointment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AppointmentController {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/current")
    public String getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/appointment-request")
    public void appointmentRequest(@RequestBody AppointmentDto appointmentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        UserEntity userEntity = userRepository.findByUsername(name);
        if(userEntity == null) {
            userEntity = userRepository.findByLastname(name);
        }

        if(userEntity == null) {
            return;
        }

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setAccepted(userEntity.getRole().equals("PATIENT") ? false : true);
        appointmentEntity.setPatientId(userEntity.getUid().toString());
        appointmentEntity.setLocation(appointmentDto.getLocation());
        LocalDateTime dateTime = LocalDateTime.parse(appointmentDto.getDate(), formatter);
        appointmentEntity.setDate(dateTime);
        appointmentEntity.setDescription(appointmentDto.getDescription());

        appointmentRepository.save(appointmentEntity);
    }

    @GetMapping("/appointments")
    public List<AppointmentDto> getAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAll();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();

        for(AppointmentEntity appointmentEntity : appointmentEntities) {
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointmentEntity.getUid());
            appointmentDto.setAccepted(appointmentEntity.isAccepted());

            UserEntity userEntityDoctor = userRepository.findByUsername(name);

            Optional<UserEntity> userEntityPatientOpt = userRepository.findAll().stream()
                    .filter(patient -> patient.getUid().toString().equals(appointmentEntity.getPatientId())).findFirst();

            if(!userEntityPatientOpt.isPresent()) {
                continue;
            }

            UserEntity userEntityPatient = userEntityPatientOpt.get();
            appointmentDto.setUsernamePatient(userEntityPatient.getUsername());
            appointmentDto.setUsernameDoctor(userEntityDoctor.getUsername());
            appointmentDto.setLocation(appointmentEntity.getLocation());
            appointmentDto.setDate(appointmentEntity.getDate().format(formatter));
            appointmentDto.setDescription(appointmentEntity.getDescription());

            if(userEntityDoctor != null) {
                appointmentDto.setFullnameDoctor(userEntityDoctor.getFirstName() + " " + userEntityDoctor.getLastName());
            }

            appointmentDto.setFullnamePatient(userEntityPatient.getFirstName() + " " + userEntityPatient.getLastName());
            appointmentDtos.add(appointmentDto);
        }

        return appointmentDtos;
    }

    @GetMapping("/own-appointments")
    public List<AppointmentDto> getOwnAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        UserEntity userEntityPatient = userRepository.findByUsername(name);

        List<AppointmentEntity> appointmentEntities = appointmentRepository.findByPatientId(userEntityPatient.getUid().toString());
        List<AppointmentDto> appointmentDtos = new ArrayList<>();

        for(AppointmentEntity appointmentEntity : appointmentEntities) {
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointmentEntity.getUid());
            appointmentDto.setAccepted(appointmentEntity.isAccepted());

            UserEntity userEntityDoctor = null;
            if(appointmentEntity.getDoctorId() != null) {
                userEntityDoctor = userRepository.getById(UUID.fromString(appointmentEntity.getDoctorId()));
                appointmentDto.setUsernameDoctor(userEntityDoctor.getUsername());
                appointmentDto.setFullnameDoctor(userEntityPatient.getFirstName() + " " + userEntityPatient.getLastName());

            }

            appointmentDto.setUsernamePatient(userEntityPatient.getUsername());
            appointmentDto.setLocation(appointmentEntity.getLocation());
            appointmentDto.setDate(appointmentEntity.getDate().format(formatter));
            appointmentDto.setDescription(appointmentEntity.getDescription());

            if(userEntityDoctor != null) {
                appointmentDto.setFullnameDoctor(userEntityDoctor.getFirstName() + " " + userEntityDoctor.getLastName());
            }

            appointmentDto.setFullnamePatient(userEntityPatient.getFirstName() + " " + userEntityPatient.getLastName());
            appointmentDtos.add(appointmentDto);
        }

        return appointmentDtos;
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

    @PostMapping("/appointment-accept")
    public void acceptAppoitment(@RequestBody AppointmentDto appointmentDto) {
        LocalDateTime dateTime = LocalDateTime.parse(appointmentDto.getDate(), formatter);
        UserEntity userEntity = userRepository.findByUsername(appointmentDto.getUsernamePatient());

        appointmentRepository.setAcceptedForPatient(userEntity.getUid().toString(), dateTime);
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
}
