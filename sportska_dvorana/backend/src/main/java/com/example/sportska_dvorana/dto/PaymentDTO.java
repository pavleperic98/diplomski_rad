package com.example.sportska_dvorana.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long paymentId;

    @Schema(example = "pi_3K...") 
    private String stripeId;

    @Schema(example = "2000")
    private BigDecimal amount;

    @Schema(example = "RSD")
    private String currency;

    @Schema(example = "PAID")
    private String paymentStatus;

    @Schema(example = "1")
    private Long reservationId; 

    // Getteri i setteri
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
} 
