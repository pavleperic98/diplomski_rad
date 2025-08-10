package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByRoleId(Long role);
}