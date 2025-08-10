package com.example.sportska_dvorana.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class StatusDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long statusId;

    @Schema(example = "test status")
    @NotBlank(message = "Status is required")
    private String status;

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
