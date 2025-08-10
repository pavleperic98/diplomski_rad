package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.User;
import com.example.sportska_dvorana.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //User findByEmail(String email);
    List<User> findByRole(Role role);

    // login/register
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}