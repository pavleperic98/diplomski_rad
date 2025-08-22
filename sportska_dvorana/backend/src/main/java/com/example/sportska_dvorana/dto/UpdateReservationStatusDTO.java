package com.example.sportska_dvorana.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class UpdateReservationStatusDTO {
    
    @Schema(example = "4")
    @NotNull(message = "Status is required")
    private int statusId;

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
