package com.example.sportska_dvorana.service;

import com.example.sportska_dvorana.dto.RoleDTO;
import com.example.sportska_dvorana.model.Role;
import com.example.sportska_dvorana.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Get all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Get by ID
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    // Create role
    public Role createRole(RoleDTO dto) {
        Role role = fromDTO(dto);
        role.setRoleId(null);
        return roleRepository.save(role);
    }

    // Update role
    public Optional<Role> updateRole(Long id, RoleDTO dto) {
        return roleRepository.findById(id)
            .map(existing -> {
                existing.setRole(dto.getRole());
                return roleRepository.save(existing);
            });
    }

    // Delete role
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    // Mapping entity in DTO
    public RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();

        dto.setRoleId(role.getRoleId());
        dto.setRole(role.getRole());

        return dto;
    }

    // Mapping DTO in entity
    public Role fromDTO(RoleDTO dto) {
        Role role = new Role();

        role.setRoleId(dto.getRoleId());
        role.setRole(dto.getRole());
        
        return role;
    }
}