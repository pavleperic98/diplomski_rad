package com.example.sportska_dvorana.service;

import com.example.sportska_dvorana.dto.SportDTO;
import com.example.sportska_dvorana.model.Hall;
import com.example.sportska_dvorana.model.Sport;
import com.example.sportska_dvorana.repository.HallRepository;
import com.example.sportska_dvorana.repository.SportRepository;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SportService {

    private final SportRepository sportRepository;
    private final HallRepository hallRepository;

    public SportService(SportRepository sportRepository, HallRepository hallRepository) {
        this.sportRepository = sportRepository;
        this.hallRepository = hallRepository;
    }

    // Get all sports
    public List<SportDTO> getAllSports() {
        return sportRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Get by ID
    public Optional<Sport> getSportById(Long id) {
        return sportRepository.findById(id);
    }

    // Create sport
    public Sport createSport(SportDTO dto) {
        Sport sport = fromDTO(dto);
        sport.setSportId(null);
        return sportRepository.save(sport);
    }

     // Update sport
    public Optional<Sport> updateSport(Long id, SportDTO dto) {
        return sportRepository.findById(id)
                .map(existing -> {
                    existing.setSport(dto.getSport());
                    return sportRepository.save(existing);
                });
    }

    // Delete sport
    public void deleteSport(Long id) {
        sportRepository.deleteById(id);
    }

    // Mapping entity to DTO
    public SportDTO toDTO(Sport sport) {
        if (sport == null) return null;

        SportDTO dto = new SportDTO();

        dto.setSportId(sport.getSportId());
        dto.setSport(sport.getSport());
        dto.setHallIds(sport.getHalls().stream().map(Hall::getHallId).toList());

        return dto;
    }

    // Mapping DTO to entity
    public Sport fromDTO(SportDTO dto) {
        if (dto == null) return null;

        Sport sport = new Sport();

        sport.setSportId(dto.getSportId());
        sport.setSport(dto.getSport());
        sport.setHalls(new HashSet<>(hallRepository.findAllById(dto.getHallIds())));

        return sport;
    }

}