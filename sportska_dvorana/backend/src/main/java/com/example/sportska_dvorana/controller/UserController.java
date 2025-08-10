package com.example.sportska_dvorana.controller;

import com.example.sportska_dvorana.dto.ApiResponse;
import com.example.sportska_dvorana.dto.UserCreateDTO;
import com.example.sportska_dvorana.dto.UserDTO;
import com.example.sportska_dvorana.dto.UserUpdateDTO;
import com.example.sportska_dvorana.model.User;
import com.example.sportska_dvorana.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Listanje svih korisnika
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(userService::toDTO)
                .collect(Collectors.toList());
    }


    // Dohvatanje korisnika po ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            UserDTO dto = userService.toDTO(userOpt.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found"));
        }
    }

    // Kreiranje korisnika
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        try {
            User user = userService.fromCreateDTO(userCreateDTO);
            User savedUser = userService.createUser(user);
            UserDTO savedDTO = userService.toDTO(savedUser);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully");
            response.put("user", savedDTO);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // Ažuriranje korisnika po ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            User updatedUser = userService.updateUser(id, userUpdateDTO);
            UserDTO updatedDTO = userService.toDTO(updatedUser);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User updated successfully");
            response.put("user", updatedDTO);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ex.getMessage()));
        }
    }


    // Brisanje korisnika
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found"));
        }

        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse("User deleted successfully."));
    }
}
