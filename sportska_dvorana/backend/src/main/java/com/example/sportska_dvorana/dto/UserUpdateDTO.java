package com.example.sportska_dvorana.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserUpdateDTO {

@Schema(example = "Test")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(example = "Testic izmenjen")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(example = "test123")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(example = "test@gmail.com")
    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(example = "060123456")
    private String phoneNumber;

    @Schema(example = "2000-01-01")
    private LocalDate birthDate;

    @Schema(example = "1")
    private Long roleId;


    // Getteri i setteri
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
}
