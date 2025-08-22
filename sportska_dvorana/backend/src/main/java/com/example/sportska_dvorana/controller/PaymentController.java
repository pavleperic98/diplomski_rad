package com.example.sportska_dvorana.controller;

import com.example.sportska_dvorana.dto.ApiResponse;
import com.example.sportska_dvorana.dto.PaymentDTO;
import com.example.sportska_dvorana.model.Payment;
import com.example.sportska_dvorana.service.PaymentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment Controller")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        Optional<Payment> paymentOpt = paymentService.getPaymentById(id);

        if (paymentOpt.isPresent()) {
            return ResponseEntity.ok(paymentService.toDTO(paymentOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Payment not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO dto) {
        Payment payment = paymentService.createPayment(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Payment created", "payment", paymentService.toDTO(payment)));
    }

    @PutMapping("/{id}")
    public Payment updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok().build();
    }
}