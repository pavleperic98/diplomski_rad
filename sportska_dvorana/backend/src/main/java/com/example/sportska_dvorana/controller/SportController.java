package com.example.sportska_dvorana.controller;

import com.example.sportska_dvorana.dto.ApiResponse;
import com.example.sportska_dvorana.dto.SportDTO;
import com.example.sportska_dvorana.model.Sport;
import com.example.sportska_dvorana.service.SportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/sport")
@Tag(name = "Sport Controller")
public class SportController {

    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    @GetMapping
    public List<SportDTO> getAllSports() {
        return sportService.getAllSports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSportById(@PathVariable Long id) {
        Optional<Sport> sportOpt = sportService.getSportById(id);
        
        if (sportOpt.isPresent()) {
            return ResponseEntity.ok(sportService.toDTO(sportOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Sport not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createSport(@Valid @RequestBody SportDTO sportDTO) {
        try {
            Sport created = sportService.createSport(sportDTO);
            SportDTO dto = sportService.toDTO(created);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sport created successfully");
            response.put("sport", dto);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSport(@PathVariable Long id, @Valid @RequestBody SportDTO sportDTO) {
        try {
            Sport updated = sportService.updateSport(id, sportDTO)
                    .orElseThrow(() -> new NoSuchElementException("Sport not found"));

            SportDTO dto = sportService.toDTO(updated);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sport updated successfully");
            response.put("sport", dto);

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
    public ResponseEntity<?> deleteSport(@PathVariable Long id) {
        Optional<Sport> existing = sportService.getSportById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Sport not found"));
        }

        sportService.deleteSport(id);
        return ResponseEntity.ok(new ApiResponse("Sport deleted successfully"));
    }
}