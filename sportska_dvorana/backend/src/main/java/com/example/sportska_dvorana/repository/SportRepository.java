package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.Sport;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
    Sport findBySport(String sport);
}

