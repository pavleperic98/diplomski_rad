package com.example.sportska_dvorana.service;

import com.example.sportska_dvorana.dto.StatusDTO;
import com.example.sportska_dvorana.model.Status;
import com.example.sportska_dvorana.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    // Get all status
    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    // Get status by ID
    public Optional<Status> getStatusById(Long id) {
        return statusRepository.findById(id);
    }

    // Create status
    public Status createStatus(StatusDTO dto) {
        Status status = fromDTO(dto);
        status.setStatusId(null);
        return statusRepository.save(status);
    }

    // Update status
    public Optional<Status> updateStatus(Long id, StatusDTO dto) {
        return statusRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(dto.getStatus());
                    return statusRepository.save(existing);
                });
    }

    // Delete status
    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }

    // Mapping entity in DTO
    public StatusDTO toDTO(Status status) {
        if (status == null) return null;
        StatusDTO dto = new StatusDTO();
        dto.setStatusId(status.getStatusId());
        dto.setStatus(status.getStatus());
        return dto;
    }

    // Mapping DTO in entity
    public Status fromDTO(StatusDTO dto) {
        if (dto == null) return null;
        Status status = new Status();
        status.setStatusId(dto.getStatusId());
        status.setStatus(dto.getStatus());
        return status;
    }
}
