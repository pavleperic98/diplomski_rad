package com.example.sportska_dvorana.controller;

import com.example.sportska_dvorana.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/stripe")
@Tag(name = "Stripe Controller")
public class StripeController {

    private final StripeService stripeService;
    
    @Value("${stripe.webhook.secret:whsec_Uobff09bzI1d2n54eo2rj62SgwuQ7fme}")
    private String endpointSecret;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }
    
    @PostConstruct
    public void validateConfig() {
        if (endpointSecret == null || endpointSecret.trim().isEmpty()) {
            throw new IllegalStateException("Stripe webhook secret is required but not configured");
        }
        System.out.println("Stripe webhook secret configured successfully");
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> createCheckout(@RequestParam Long reservationId, @RequestParam Double amount) {
        try {
            String url = stripeService.createCheckoutSession(reservationId, amount);
            System.out.println("Checkout session kreiran: " + url);
            return ResponseEntity.ok().body(url);
        } catch (StripeException e) {
            System.err.println("Stripe error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Greška prilikom kreiranja checkout-a: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Neočekivana greška: " + e.getMessage());
        }
    }

    // FINAL WORKING SOLUTION - Fixed session extraction
    @SuppressWarnings("deprecation")
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestHeader("Stripe-Signature") String sigHeader,
                                                @RequestBody String payload) {
        System.out.println("Webhook received, payload length: " + payload.length());
        
        try {
            // Verify webhook signature
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("Webhook verified successfully, event type: " + event.getType());
            System.out.println("Event ID: " + event.getId());

            switch (event.getType()) {
                case "checkout.session.completed":
                    try {
                        System.out.println("Processing checkout.session.completed event");
                        
                        // CORRECTED: Get the session object directly
                        StripeObject stripeObject = event.getData().getObject();
                        
                        Session session = null;
                        
                        // Check if the object is already a Session
                        if (stripeObject instanceof Session) {
                            session = (Session) stripeObject;
                            System.out.println("Session extracted directly from event: " + session.getId());
                        } else {
                            System.out.println("Object is not a Session, trying alternative extraction");
                            System.out.println("Object type: " + stripeObject.getClass().getName());
                            
                            // Fallback: try to get session ID and retrieve via API
                            String sessionId = extractSessionIdFromJson(event);
                            if (sessionId != null) {
                                session = Session.retrieve(sessionId);
                                System.out.println("Session retrieved via API: " + session.getId());
                            }
                        }
                        
                        if (session != null) {
                            System.out.println("Session ID: " + session.getId());
                            System.out.println("Session status: " + session.getStatus());
                            System.out.println("Session payment status: " + session.getPaymentStatus());
                            System.out.println("Session metadata: " + session.getMetadata());
                            
                            // Only process if session is actually completed
                            if ("complete".equals(session.getStatus())) {
                                stripeService.handleSuccessfulCheckout(session);
                                System.out.println("Successfully processed checkout session: " + session.getId());
                            } else {
                                System.out.println("Session not complete, status: " + session.getStatus());
                            }
                        } else {
                            System.err.println("Could not extract session from event");
                            return ResponseEntity.ok("Webhook received but could not extract session");
                        }
                        
                    } catch (Exception sessionError) {
                        System.err.println("Error processing checkout session: " + sessionError.getMessage());
                        sessionError.printStackTrace();
                        return ResponseEntity.ok("Webhook received but processing failed: " + sessionError.getMessage());
                    }
                    break;

                case "payment_intent.succeeded":
                    System.out.println("Payment intent succeeded event received");
                    break;

                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }

            return ResponseEntity.ok("Webhook processed successfully");
            
        } catch (Exception e) {
            System.err.println("Webhook verification or processing failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
        }
    }
    
    // Fallback method to extract session ID from JSON if direct casting fails
    @SuppressWarnings("deprecation")
    private String extractSessionIdFromJson(Event event) {
        try {
            String jsonData = event.getData().getObject().toJson();
            System.out.println("Event data JSON: " + jsonData);
            
            // Simple string parsing to find the session ID
            if (jsonData.contains("\"id\":")) {
                int startIndex = jsonData.indexOf("\"id\":") + 5;
                // Skip any whitespace after the colon
                while (startIndex < jsonData.length() && Character.isWhitespace(jsonData.charAt(startIndex))) {
                    startIndex++;
                }
                // Skip the opening quote
                if (startIndex < jsonData.length() && jsonData.charAt(startIndex) == '"') {
                    startIndex++;
                }
                // Find the closing quote
                int endIndex = jsonData.indexOf('"', startIndex);
                if (endIndex > startIndex) {
                    String sessionId = jsonData.substring(startIndex, endIndex);
                    System.out.println("Extracted session ID from JSON: " + sessionId);
                    return sessionId;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error extracting session ID from JSON: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}