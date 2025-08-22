package com.example.sportska_dvorana.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class ReservationDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long reservationId;

    @Schema(example = "1")
    private Long hallId;

    @Schema(example = "1")
    private Long sportId;

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "1")
    private Long statusId;

    @Schema(example = "2025-08-04")
    private LocalDate date;

    @Schema(example = "10:00")
    private LocalTime timeFrom;

    @Schema(example = "12:00")
    private LocalTime timeTo;

    @Schema(example = "2000")
    private BigDecimal finalPrice;


    // Getteri i setteri
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }
}