package ru.gb.diplom.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gb.diplom.model.Role;
import ru.gb.diplom.model.User;
import ru.gb.diplom.repository.RoleRepository;
import ru.gb.diplom.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                           RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void saveUser(User user) {
        User createdUser = new User();
        createdUser.setName(user.getName());
        createdUser.setEmail(user.getEmail());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = roleCheck();
        }
        createdUser.setRoles(List.of(role));
        userRepository.save(createdUser);
    }
    private Role roleCheck() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
    // получить пользователя по id
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));

    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUsers(Long id, User user) {
        User updateUser = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        updateUser.setName(user.getName());
        updateUser.setEmail(user.getEmail());
        updateUser.setNotes(user.getNotes());
        return userRepository.save(updateUser);
    }

}
