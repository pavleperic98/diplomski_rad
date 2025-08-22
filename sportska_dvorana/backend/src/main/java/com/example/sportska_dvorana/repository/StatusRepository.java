package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.Status;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long>  {

    Optional<Status> findByStatus(String status);
}
