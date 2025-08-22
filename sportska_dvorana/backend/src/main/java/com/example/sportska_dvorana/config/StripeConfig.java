package com.example.sportska_dvorana.config;

import com.stripe.Stripe;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class StripeConfig {

    private final StripeProperties stripeProperties;

    public StripeConfig(StripeProperties stripeProperties) {
        this.stripeProperties = stripeProperties;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getApiKey();
    }

    public String getApiKey() {
        return stripeProperties.getApiKey();
    }

    public String getWebhookSecret() {
        return stripeProperties.getWebhookSecret();
    }
}