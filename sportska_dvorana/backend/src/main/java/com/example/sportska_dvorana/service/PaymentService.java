package com.example.sportska_dvorana.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.sportska_dvorana.dto.PaymentDTO;
import com.example.sportska_dvorana.model.Payment;
import com.example.sportska_dvorana.model.Reservation;
import com.example.sportska_dvorana.repository.PaymentRepository;
import com.example.sportska_dvorana.repository.ReservationRepository;
import com.example.sportska_dvorana.repository.StatusRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          ReservationRepository reservationRepository,
                          StatusRepository statusRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    
    // Get all payments
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    
    // Get payment by ID
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    
    // Create payment
    public Payment createPayment(PaymentDTO dto) {
        Payment payment = fromDTO(dto);

        if (dto.getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(dto.getReservationId())
                    .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + dto.getReservationId()));
            
            payment.setReservation(reservation);
        }

        return paymentRepository.save(payment);
    }

    // Update payment
    public Payment updatePayment(Long id, Payment paymentDetails) {
        return paymentRepository.findById(id).map(payment -> {

            payment.setStripeId(paymentDetails.getStripeId());
            payment.setAmount(paymentDetails.getAmount());
            payment.setCurrency(paymentDetails.getCurrency());
            payment.setPaymentStatus(paymentDetails.getPaymentStatus());

            return paymentRepository.save(payment);
        }).orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
    }

    // Delete payment
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
    
    // Entity → DTO
    public PaymentDTO toDTO(Payment payment) {
        if (payment == null) return null;

        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setStripeId(payment.getStripeId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setPaymentStatus(payment.getPaymentStatus());

        if (payment.getReservation() != null) {
            dto.setReservationId(payment.getReservation().getReservationId());
        }

        return dto;
    }

    
    // DTO → Entity
    public Payment fromDTO(PaymentDTO dto) {
        if (dto == null) return null;

        Payment payment = new Payment();
        payment.setStripeId(dto.getStripeId());
        payment.setAmount(dto.getAmount());
        payment.setCurrency(dto.getCurrency());
        payment.setPaymentStatus(dto.getPaymentStatus());

        return payment;
    }
}