package com.example.sportska_dvorana.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sportska_dvorana.dto.LoginDTO;
import com.example.sportska_dvorana.dto.UserCreateDTO;
import com.example.sportska_dvorana.dto.UserDTO;
import com.example.sportska_dvorana.model.Role;
import com.example.sportska_dvorana.model.User;
import com.example.sportska_dvorana.repository.*;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo, RoleRepository roleRepo, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(UserCreateDTO newUser) {
        if (userRepo.existsByUsername(newUser.getUsername())) {
            throw new RuntimeException("Username already taken.");
        }

        if (userRepo.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("Email already taken.");
        }

        Role userRole = roleRepo.findByRoleId(newUser.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setBirthDate(newUser.getBirthDate());
        user.setRole(userRole);

        userRepo.save(user);

        return jwtService.generateToken(user);
    }

    public String login(LoginDTO loginUser) {
        User user = userRepo.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtService.generateToken(user);
    }

    public UserDTO getUserDTOByEmail(String email) {
        return userRepo.findByEmail(email)
                .map(this::toDTO)
                .orElse(null);
    }

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setBirthDate(user.getBirthDate());
        dto.setRoleId(user.getRole().getRoleId());
        return dto;
    }
}