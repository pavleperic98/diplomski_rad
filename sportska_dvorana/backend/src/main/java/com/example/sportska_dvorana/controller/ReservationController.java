package com.example.sportska_dvorana.controller;

import com.example.sportska_dvorana.dto.ApiResponse;
import com.example.sportska_dvorana.dto.ReservationDTO;
import com.example.sportska_dvorana.dto.ReservationResponseDTO;
import com.example.sportska_dvorana.model.Reservation;
import com.example.sportska_dvorana.service.ReservationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservation")
@Tag(name = "Reservation Controller")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();

        List<ReservationResponseDTO> dtos = reservations.stream()
                .map(reservationService::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);

        if (reservation.isPresent()) {
            return ResponseEntity.ok(reservationService.toResponseDTO(reservation.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            Reservation created = reservationService.createReservation(reservationDTO);
            ReservationDTO dto = reservationService.toDTO(created);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reservation created successfully");
            response.put("reservation", dto);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            Reservation updated = reservationService.updateReservation(id, reservationDTO)
                    .orElseThrow(() -> new NoSuchElementException("Reservation not found"));
            
            ReservationDTO dto = reservationService.toDTO(updated);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reservation updated successfully");
            response.put("reservation", dto);

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
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        Optional<Reservation> opt = reservationService.getReservationById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Reservation not found"));
        }
        
        reservationService.deleteReservation(id);
        return ResponseEntity.ok(new ApiResponse("Reservation deleted successfully"));
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getReservationsByHallAndDate(@RequestParam Long hallId, @RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date); 

            List<Reservation> reservations = reservationService.getReservationsByHallIdAndDate(hallId, localDate);
            return ResponseEntity.ok(reservations);

        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body("Datum nije validan, koristi format yyyy-MM-dd");
        }
    }
}