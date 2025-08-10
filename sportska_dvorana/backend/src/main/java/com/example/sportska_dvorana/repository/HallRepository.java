package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.Hall;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findByCapacityGreaterThan(int minCapacity);
    Hall findByName(String name);
}
