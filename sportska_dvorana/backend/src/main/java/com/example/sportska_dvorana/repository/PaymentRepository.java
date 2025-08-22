package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.Payment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>  {

    Optional<Payment> findByStripeId(String stripeId);
    List<Payment> findByReservationReservationId(Long reservationId);
    boolean existsByStripeId(String stripeId);
    Optional<Payment> findByPaymentStatus(String paymentStatus);
}

