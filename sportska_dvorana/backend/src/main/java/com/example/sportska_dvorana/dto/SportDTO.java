package com.example.sportska_dvorana.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class SportDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long sportId;

    @Schema(example = "test sport")
    @NotBlank(message = "Sport name is required")
    private String sport;

    private List<Long> hallIds;


    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public List<Long> getHallIds() {
        return hallIds;
    }

    public void setHallIds(List<Long> hallIds) {
        this.hallIds = hallIds;
    }
}
