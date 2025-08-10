package com.example.sportska_dvorana.service;

import com.example.sportska_dvorana.model.Payment;
import com.example.sportska_dvorana.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        return paymentRepository.findById(id).map(payment -> {
            payment.setStripeId(paymentDetails.getStripeId());
            payment.setAmount(paymentDetails.getAmount());
            payment.setCurrency(paymentDetails.getCurrency());
            payment.setPaymentMethod(paymentDetails.getPaymentMethod());
            payment.setPaymentStatus(paymentDetails.getPaymentStatus());
            payment.setCreatedAt(paymentDetails.getCreatedAt());
            return paymentRepository.save(payment);
        }).orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}