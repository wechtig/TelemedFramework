package at.fh.telemed.telemedframeworkroot.services;

import at.fh.telemed.telemedframeworkroot.dtos.MedUser;
import at.fh.telemed.telemedframeworkroot.dtos.MedUserRole;
import at.fh.telemed.telemedframeworkroot.entities.RoleEntity;
import at.fh.telemed.telemedframeworkroot.entities.UserEntity;
import at.fh.telemed.telemedframeworkroot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findUserByUid(UUID uid) {
        var userOpt = userRepository.findById(uid);
        if(userOpt.isPresent()) {
            return userOpt.get();
        }

        return null;
    }

    public List<UserEntity> getPassword() {
        return userRepository.findAll();
    }

    public List<MedUser> findAll() {
        List<UserEntity> users = userRepository.findAll();
        if (users == null) {
            return new ArrayList<>();
        }

        return users.stream()
                .map(u -> new MedUser(u.getUid(), u.getEmail(),
                        u.getUsername(), u.getFirstName(), u.getLastName()
                        ,u.getRole()))
                .collect(Collectors.toList());
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    private MedUserRole convertEntityoRoleDto(RoleEntity roleEntity) {
        MedUserRole role = new MedUserRole();
        role.setRole(roleEntity.getRole());
        role.setUid(roleEntity.getUid());

        return role;
    }
}
