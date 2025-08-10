package com.example.sportska_dvorana.service;

import com.example.sportska_dvorana.dto.HallDTO;
import com.example.sportska_dvorana.dto.SportBasicDTO;
import com.example.sportska_dvorana.model.Hall;
import com.example.sportska_dvorana.repository.HallRepository;
import com.example.sportska_dvorana.repository.SportRepository;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HallService {

    private final HallRepository hallRepository;
    private final SportRepository sportRepository;

    public HallService(HallRepository hallRepositor, SportRepository sportRepository) {
        this.hallRepository = hallRepositor;
        this.sportRepository = sportRepository;
    }

    // Get all halls
    public List<HallDTO> getAllHalls() {
        return hallRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Get by ID
    public Optional<Hall> getHallById(Long id) {
        return hallRepository.findById(id);
    }

    // Create hall
    public Hall createHall(HallDTO dto) {
        Hall hall = fromDTO(dto);
        hall.setHallId(null);
        return hallRepository.save(hall);
    }

    // Update hall
    public Optional<Hall> updateHall(Long id, HallDTO dto) {
        return hallRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setCapacity(dto.getCapacity());
                    existing.setDescription(dto.getDescription());
                    existing.setPricePerHour(dto.getPricePerHour());
                    return hallRepository.save(existing);
                });
    }

    // Delete hall
    public void deleteHall(Long id) {
        hallRepository.deleteById(id);
    }

    // Mapping entity to DTO
    public HallDTO toDTO(Hall hall) {
        if (hall == null) return null;

        HallDTO dto = new HallDTO();

        dto.setHallId(hall.getHallId());
        dto.setName(hall.getName());
        dto.setCapacity(hall.getCapacity());
        dto.setDescription(hall.getDescription());
        dto.setPricePerHour(hall.getPricePerHour());
        dto.setSports(
            hall.getSports()
                .stream()
                .map(sport -> new SportBasicDTO(sport.getSportId(), sport.getSport()))
                .toList()
        );

        return dto;
    }

    // Mapping DTO to entity
    public Hall fromDTO(HallDTO dto) {
        if (dto == null) return null;

        Hall hall = new Hall();

        hall.setHallId(dto.getHallId());
        hall.setName(dto.getName());
        hall.setCapacity(dto.getCapacity());
        hall.setDescription(dto.getDescription());
        hall.setPricePerHour(dto.getPricePerHour());

        // Postavi sports ako su poslati sportIds (npr. na kreiranju ili update)
        if (dto.getSportIds() != null) {
            hall.setSports(new HashSet<>(sportRepository.findAllById(dto.getSportIds())));
        } else {
            hall.setSports(new HashSet<>()); // ili null, zavisno od željene logike
        }

        return hall;
    }
}