package at.fh.telemed.telemedframeworkroot.services;

import at.fh.telemed.telemedframeworkroot.dtos.MedUser;
import at.fh.telemed.telemedframeworkroot.entities.RoleEntity;
import at.fh.telemed.telemedframeworkroot.entities.UserEntity;
import at.fh.telemed.telemedframeworkroot.repositories.RoleRepository;
import at.fh.telemed.telemedframeworkroot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class UserSecureSecurityService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserEntity saveUser(MedUser user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setRole(roleRepository.findByRole(user.getRole()).getRole());
        userEntity.setEmail(user.getEmail());
        userEntity.setLastName(user.getLastName());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setUsername(user.getUsername());
        return userService.createUser(userEntity);
    }

    public UserEntity saveUser(UserEntity user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userService.createUser(user);
    }

    public UserEntity saveUserWithCryptPassword(UserEntity user) {
        return userService.createUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userService.findByUsername(userName);

        if (user != null && user.getRole() != null) {
            RoleEntity roleEntity = roleRepository.findByRole(user.getRole());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getUserAuthority(roleEntity));
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity findUserByUsername(String username) {
        return userService.findByUsername(username);
    }

    public void delete(UserEntity userEntity) {
        userService.delete(userEntity);
    }

    public UserEntity findUserByUid(UUID uid) {
        return userService.findUserByUid(uid);
    }

    public List<MedUser> findAllUsers() {
        return userService.findAll();
    }

    private List<GrantedAuthority> getUserAuthority(RoleEntity role) {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role.getRole()));
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    public boolean checkPassword(String currentPasswordCheckSeq, String currentPassword) {
        return bCryptPasswordEncoder.matches(currentPasswordCheckSeq, currentPassword);
    }
}

