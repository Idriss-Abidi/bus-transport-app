package com.buapp.paymentservice.controller;

import com.buapp.paymentservice.dto.PaymentRequest;
import com.buapp.paymentservice.dto.PaymentResponse;
import com.buapp.paymentservice.model.PaymentStatus;
import com.buapp.paymentservice.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Creating payment for {} ID: {}", request.getType(), request.getReferenceId());
        PaymentResponse response = paymentService.createPaymentIntent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentIntentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String paymentIntentId) {
        PaymentResponse response = paymentService.getPaymentByIntentId(paymentIntentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(@PathVariable Long userId) {
        List<PaymentResponse> payments = paymentService.getUserPayments(userId);
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        Event event;
        
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.error("Invalid signature for webhook", e);
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (paymentIntent != null) {
                    log.info("Payment succeeded: {}", paymentIntent.getId());
                    paymentService.updatePaymentStatus(paymentIntent.getId(), PaymentStatus.SUCCEEDED);
                }
                break;
                
            case "payment_intent.payment_failed":
                paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (paymentIntent != null) {
                    log.info("Payment failed: {}", paymentIntent.getId());
                    paymentService.updatePaymentStatus(paymentIntent.getId(), PaymentStatus.FAILED);
                }
                break;
                
            case "payment_intent.canceled":
                paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (paymentIntent != null) {
                    log.info("Payment canceled: {}", paymentIntent.getId());
                    paymentService.updatePaymentStatus(paymentIntent.getId(), PaymentStatus.CANCELED);
                }
                break;
                
            default:
                log.info("Unhandled event type: {}", event.getType());
        }

        return ResponseEntity.ok("Success");
    }
}
