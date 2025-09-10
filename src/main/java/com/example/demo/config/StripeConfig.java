package com.example.demo.config;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

@Configuration
public class StripeConfig {

	@Value("${stripe.api.key}")
  private String stripeApiKey;
	
	@PostConstruct
  public void init() {
      Stripe.apiKey = stripeApiKey;
  }
}
