package com.example.sportska_dvorana.controller;

import com.example.sportska_dvorana.dto.ApiResponse;
import com.example.sportska_dvorana.dto.StatusDTO;
import com.example.sportska_dvorana.model.Status;
import com.example.sportska_dvorana.service.StatusService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/status")
@Tag(name = "Status Controller")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public List<StatusDTO> getAllStatuses() {
        return statusService.getAllStatuses()
                .stream()
                .map(statusService::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStatusById(@PathVariable Long id) {
        Optional<Status> statusOpt = statusService.getStatusById(id);
        
        if (statusOpt.isPresent()) {
            return ResponseEntity.ok(statusOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Status not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createStatus(@Valid @RequestBody StatusDTO statusDTO) {
        try {
            Status created = statusService.createStatus(statusDTO);
            StatusDTO dto = statusService.toDTO(created);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Status created successfully");
            response.put("status", dto);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusDTO statusDTO) {
        try {
            Status updated = statusService.updateStatus(id, statusDTO)
                    .orElseThrow(() -> new NoSuchElementException("Status not found"));

            StatusDTO dto = statusService.toDTO(updated);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Status updated successfully");
            response.put("status", dto);

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
    public ResponseEntity<?> deleteStatus(@PathVariable Long id) {
        Optional<Status> statusOpt = statusService.getStatusById(id);
        
        if (statusOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Status not found"));
        }

        statusService.deleteStatus(id);
        return ResponseEntity.ok(new ApiResponse("Status deleted successfully"));
    }
}
