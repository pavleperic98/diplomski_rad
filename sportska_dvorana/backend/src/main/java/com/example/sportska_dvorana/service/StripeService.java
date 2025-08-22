package com.example.sportska_dvorana.service;

import com.example.sportska_dvorana.model.Payment;
import com.example.sportska_dvorana.model.Reservation;
import com.example.sportska_dvorana.model.Status;
import com.example.sportska_dvorana.repository.PaymentRepository;
import com.example.sportska_dvorana.repository.ReservationRepository;
import com.example.sportska_dvorana.repository.StatusRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StripeService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final StatusRepository statusRepository;

    public StripeService(PaymentRepository paymentRepository,
                         ReservationRepository reservationRepository,
                         StatusRepository statusRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.statusRepository = statusRepository;
    }

    // Kreira Stripe Checkout Session
    public String createCheckoutSession(Long reservationId, Double amount) throws StripeException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));

        long stripeAmount = Math.round(amount * 100);

        String description = String.format(
                "RSD %.2f | Sport: %s | Datum: %s | Vreme: %s - %s | Korisnik: %s %s",
                amount,
                reservation.getSport().getSport(),
                reservation.getDate(),
                reservation.getTimeFrom(),
                reservation.getTimeTo(),
                reservation.getUser().getFirstName(),
                reservation.getUser().getLastName()
        );

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/payment-success?reservationId=" + reservationId)
                .setCancelUrl("http://localhost:4200/reservations?canceled=true")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("RSD")
                                                .setUnitAmount(stripeAmount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(description)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .putMetadata("reservationId", reservationId.toString())
                .putMetadata("amount", amount.toString())
                .putMetadata("userId", reservation.getUser().getUserId().toString())
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    // Obrada uspešnog checkout-a
    @Transactional
    public void handleSuccessfulCheckout(Session session) {
        try {
            if (!"complete".equals(session.getStatus()) || !"paid".equals(session.getPaymentStatus())) {
                throw new IllegalStateException("Session is not completed or payment not done.");
            }

            // Extract and validate reservation ID
            String reservationIdStr = session.getMetadata().get("reservationId");
            if (reservationIdStr == null || reservationIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Session does not contain reservationId in metadata. Metadata: " + session.getMetadata());
            }
            
            Long reservationId;
            try {
                reservationId = Long.parseLong(reservationIdStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid reservationId format: " + reservationIdStr);
            }
            
            System.out.println("Processing reservation ID: " + reservationId);

            // Find reservation
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));

            // Check if payment already exists for this session
            Optional<Payment> existingPayment = paymentRepository.findByStripeId(session.getId());
            if (existingPayment.isPresent()) return;

            Payment payment = new Payment();
            payment.setStripeId(session.getId());
            payment.setAmount(reservation.getFinalPrice());
            payment.setCurrency("RSD");
            payment.setPaymentStatus("PAID");
            payment.setReservation(reservation); 

            paymentRepository.save(payment);

            // Update reservation status
            Status paidStatus = statusRepository.findByStatus("placena")
                    .orElseThrow(() -> new IllegalArgumentException("Status 'placena' not found"));
            reservation.setStatus(paidStatus);
            reservationRepository.save(reservation);

        } catch (Exception e) {
            throw new RuntimeException("Failed to process successful checkout for session: " + session.getId(), e);
        }
    }
}