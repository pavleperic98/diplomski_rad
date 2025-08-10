package com.example.sportska_dvorana.controller;

import com.example.sportska_dvorana.dto.ApiResponse;
import com.example.sportska_dvorana.dto.HallDTO;
import com.example.sportska_dvorana.model.Hall;
import com.example.sportska_dvorana.service.HallService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/hall")
@Tag(name = "Hall Controller")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public List<HallDTO> getAllHalls() {

        return hallService.getAllHalls();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHallById(@PathVariable Long id) {
        Optional<Hall> hallOpt = hallService.getHallById(id);
        
        if (hallOpt.isPresent()) {
            return ResponseEntity.ok(hallService.toDTO(hallOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Hall not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createHall(@Valid @RequestBody HallDTO hallDTO) {
        try {
            Hall created = hallService.createHall(hallDTO);
            HallDTO dto = hallService.toDTO(created);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Hall created successfully");
            response.put("hall", dto);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHall(@PathVariable Long id, @Valid @RequestBody HallDTO hallDTO) {
        try {
            Hall updated = hallService.updateHall(id, hallDTO)
                    .orElseThrow(() -> new NoSuchElementException("Hall not found"));

            HallDTO dto = hallService.toDTO(updated);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Hall updated successfully");
            response.put("hall", dto);

            return ResponseEntity.ok(response);

        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHall(@PathVariable Long id) {
        Optional<Hall> existing = hallService.getHallById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Hall not found"));
        }

        hallService.deleteHall(id);
        return ResponseEntity.ok(new ApiResponse("Hall deleted successfully"));
    }
}