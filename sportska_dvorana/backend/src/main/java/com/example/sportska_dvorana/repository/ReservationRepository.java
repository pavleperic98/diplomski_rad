package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.Hall;
import com.example.sportska_dvorana.model.Reservation;
import com.example.sportska_dvorana.model.Status;
import com.example.sportska_dvorana.model.User;
import com.example.sportska_dvorana.model.Sport;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long>  {
    List<Reservation> findByUser(User user);
    List<Reservation> findBySport(Sport sport);
    List<Reservation> findByHall(Hall hall);
    List<Reservation> findByHallHallIdAndDate(Long hallId, LocalDate date);
    List<Reservation> findByStatus(Status status);
    
}