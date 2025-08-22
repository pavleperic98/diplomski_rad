package com.example.sportska_dvorana.dto;

public class HallBasicDTO {
    private Long hallId;
    private String hall;

    public HallBasicDTO(Long hallId, String hall) {
        this.hallId = hallId;
        this.hall = hall;
    }

    public Long getHallId() {
        return hallId;
    }
    public String getHall() {
        return hall;
    }
}
