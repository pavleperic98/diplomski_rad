package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long>  {
    
}
