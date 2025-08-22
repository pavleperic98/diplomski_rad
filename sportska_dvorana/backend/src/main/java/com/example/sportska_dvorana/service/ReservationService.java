package com.example.sportska_dvorana.service;

import com.example.sportska_dvorana.dto.ReservationDTO;
import com.example.sportska_dvorana.dto.ReservationResponseDTO;
import com.example.sportska_dvorana.model.Hall;
import com.example.sportska_dvorana.model.Payment;
import com.example.sportska_dvorana.model.Reservation;
import com.example.sportska_dvorana.model.Sport;
import com.example.sportska_dvorana.model.Status;
import com.example.sportska_dvorana.model.User;
import com.example.sportska_dvorana.repository.HallRepository;
import com.example.sportska_dvorana.repository.ReservationRepository;
import com.example.sportska_dvorana.repository.SportRepository;
import com.example.sportska_dvorana.repository.StatusRepository;
import com.example.sportska_dvorana.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;
    private final HallRepository hallRepository;
    private final SportRepository sportRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    public ReservationService(ReservationRepository reservationRepository, HallRepository hallRepository, SportRepository sportRepository, UserRepository userRepository, StatusRepository statusRepository) {
        this.reservationRepository = reservationRepository;
        this.hallRepository = hallRepository;
        this.sportRepository = sportRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
    }


    // Get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Get by ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Get by user ID
    public List<Reservation> getReservationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                      .orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUser(user);
    }

    // Get by hall ID and date
    public List<Reservation> getReservationsByHallIdAndDate(Long hallId, LocalDate date) {
        return reservationRepository.findByHallHallIdAndDate(hallId, date);
    }

    // Create reservation
    public Reservation createReservation(ReservationDTO dto) {
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new IllegalArgumentException("Hall not found with ID: " + dto.getHallId()));

        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new IllegalArgumentException("Sport not found with ID: " + dto.getSportId()));

        if (!hall.getSports().contains(sport)) {
            throw new IllegalArgumentException("The hall does not support the selected sport");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + dto.getUserId()));

        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new IllegalArgumentException("Status not found with ID: " + dto.getStatusId()));

        Reservation reservation = new Reservation();
        reservation.setHall(hall);
        reservation.setSport(sport);
        reservation.setUser(user);
        reservation.setStatus(status);
        reservation.setDate(dto.getDate());
        reservation.setTimeFrom(dto.getTimeFrom());
        reservation.setTimeTo(dto.getTimeTo());
        reservation.setFinalPrice(dto.getFinalPrice());

        return reservationRepository.save(reservation);
    }
        
    // Update reservation
    public Optional<Reservation> updateReservation(Long id, ReservationDTO dto) {
        Optional<Reservation> existingOpt = reservationRepository.findById(id);
        if (existingOpt.isEmpty()) return Optional.empty();

        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new IllegalArgumentException("Hall not found with ID: " + dto.getHallId()));
        
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new IllegalArgumentException("Sport not found with ID: " + dto.getSportId()));

        if (!hall.getSports().contains(sport)) {
            throw new IllegalArgumentException("The hall does not support the selected sport");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + dto.getUserId()));

        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new IllegalArgumentException("Status not found with ID: " + dto.getStatusId()));

        Reservation reservation = existingOpt.get();
        reservation.setHall(hall);
        reservation.setSport(sport);
        reservation.setUser(user);
        reservation.setStatus(status);
        reservation.setDate(dto.getDate());
        reservation.setTimeFrom(dto.getTimeFrom());
        reservation.setTimeTo(dto.getTimeTo());
        reservation.setFinalPrice(dto.getFinalPrice());

        return Optional.of(reservationRepository.save(reservation));
    }

    public Optional<Reservation> updateReservationStatus(Long id, int statusId) {
        Optional<Reservation> opt = reservationRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        Reservation reservation = opt.get();

        Status status = statusRepository.findById((long) statusId)
                .orElseThrow(() -> new IllegalArgumentException("Status not found with ID: " + statusId));

        reservation.setStatus(status); 
        reservationRepository.save(reservation);
        return Optional.of(reservation);
    }

    // Delete reservation
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Mapping entity to DTO
    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) return null;

        ReservationDTO dto = new ReservationDTO();

        dto.setReservationId(reservation.getReservationId());
        dto.setHallId(reservation.getHall().getHallId());
        dto.setSportId(reservation.getSport().getSportId());
        dto.setUserId(reservation.getUser().getUserId());
        dto.setStatusId(reservation.getStatus().getStatusId());
        dto.setDate(reservation.getDate());
        dto.setTimeFrom(reservation.getTimeFrom());
        dto.setTimeTo(reservation.getTimeTo());
        dto.setFinalPrice(reservation.getFinalPrice());

        return dto;
    }

    // Mapping DTO in entity
    public Reservation fromDTO(ReservationDTO dto, Hall hall, Sport sport, User user, Payment payment, Status status) {
        if (dto == null) return null;

        Reservation reservation = new Reservation();
        reservation.setReservationId(dto.getReservationId());
        reservation.setHall(hall);
        reservation.setSport(sport);
        reservation.setUser(user);
        reservation.setStatus(status);
        reservation.setDate(dto.getDate());
        reservation.setTimeFrom(dto.getTimeFrom());
        reservation.setTimeTo(dto.getTimeTo());
        reservation.setFinalPrice(dto.getFinalPrice());

        return reservation;
    }


    public ReservationResponseDTO toResponseDTO(Reservation r) {
        ReservationResponseDTO dto = new ReservationResponseDTO();

        dto.setReservationId(r.getReservationId());
        dto.setHallId(r.getHall().getHallId());
        dto.setHallName(r.getHall().getName());
        dto.setSportId(r.getSport().getSportId());
        dto.setSportName(r.getSport().getSport());
        dto.setUserId(r.getUser().getUserId());
        dto.setUserFullName(r.getUser().getFirstName() + " " + r.getUser().getLastName());
        dto.setStatusName(r.getStatus().getStatus());
        dto.setDate(r.getDate().toString()); 
        dto.setTimeFrom(r.getTimeFrom().toString()); 
        dto.setTimeTo(r.getTimeTo().toString());
        dto.setFinalPrice(r.getFinalPrice());

        return dto;
    }
}