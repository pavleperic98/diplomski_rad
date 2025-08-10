package com.example.sportska_dvorana.controller;

import com.example.sportska_dvorana.dto.ApiResponse;
import com.example.sportska_dvorana.dto.RoleDTO;
import com.example.sportska_dvorana.model.Role;
import com.example.sportska_dvorana.service.RoleService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/role")
@Tag(name = "Role Controller")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles()
                .stream()
                .map(roleService::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        Optional<Role> roleOpt = roleService.getRoleById(id);
        if (roleOpt.isPresent()) {
            return ResponseEntity.ok(roleOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Status not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        try {
            Role created = roleService.createRole(roleDTO);
            RoleDTO dto = roleService.toDTO(created);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Role created successfully");
            response.put("role", dto);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
        try {
            Role updated = roleService.updateRole(id, roleDTO)
                    .orElseThrow(() -> new NoSuchElementException("Role not found"));

            RoleDTO dto = roleService.toDTO(updated);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Role updated successfully");
            response.put("role", dto);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        Optional<Role> roleOpt = roleService.getRoleById(id);
        if (roleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Role not found"));
        }

        roleService.deleteRole(id);
        return ResponseEntity.ok(new ApiResponse("Role deleted successfully"));
    }
}