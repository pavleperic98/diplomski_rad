package com.example.sportska_dvorana.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class HallDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long hallId;

    @Schema(example = "test hall")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(example = "40")
    @NotNull(message = "Capacity is required")
    private int capacity;

    @Null
    @Schema(example = "test description")
    private String description;

    @Schema(example = "1000")
    @NotNull(message = "Price is required")
    private Double pricePerHour;

    private List<SportBasicDTO> sports;

    private List<Long> sportIds;


    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public List<SportBasicDTO> getSports() {
        return sports;
    }

    public void setSports(List<SportBasicDTO> sports) {
        this.sports = sports;
    }

    public List<Long> getSportIds() {
        return sportIds;
    }

    public void setSportIds(List<Long> sportIds) {
        this.sportIds = sportIds;
    }
}