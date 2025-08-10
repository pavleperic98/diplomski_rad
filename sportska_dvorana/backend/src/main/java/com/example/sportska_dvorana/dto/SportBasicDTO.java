package com.example.sportska_dvorana.dto;

public class SportBasicDTO {
    private Long sportId;
    private String sport;

    public SportBasicDTO(Long sportId, String sport) {
        this.sportId = sportId;
        this.sport = sport;
    }

    public Long getSportId() {
        return sportId;
    }

    public String getSport() {
        return sport;
    }
}
