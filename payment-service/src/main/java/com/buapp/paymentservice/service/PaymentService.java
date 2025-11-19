package com.buapp.paymentservice.service;

import com.buapp.paymentservice.dto.PaymentRequest;
import com.buapp.paymentservice.dto.PaymentResponse;
import com.buapp.paymentservice.model.Payment;
import com.buapp.paymentservice.model.PaymentStatus;
import com.buapp.paymentservice.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse createPaymentIntent(PaymentRequest request) {
        try {
            // Convert amount to cents for Stripe (Stripe uses smallest currency unit)
            long amountInCents = request.getAmount().multiply(java.math.BigDecimal.valueOf(100)).longValue();

            // Create Stripe Payment Intent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(request.getCurrency())
                    .putMetadata("type", request.getType().name())
                    .putMetadata("referenceId", request.getReferenceId().toString())
                    .putMetadata("userId", request.getUserId().toString())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Save payment record
            Payment payment = Payment.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .type(request.getType())
                    .referenceId(request.getReferenceId())
                    .userId(request.getUserId())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(PaymentStatus.PENDING)
                    .clientSecret(paymentIntent.getClientSecret())
                    .build();

            payment = paymentRepository.save(payment);

            log.info("Created payment intent: {} for {} ID: {}", 
                    paymentIntent.getId(), request.getType(), request.getReferenceId());

            return mapToResponse(payment);

        } catch (StripeException e) {
            log.error("Failed to create payment intent", e);
            throw new RuntimeException("Failed to create payment: " + e.getMessage());
        }
    }

    public PaymentResponse getPaymentByIntentId(String paymentIntentId) {
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapToResponse(payment);
    }

    public List<PaymentResponse> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(String paymentIntentId, PaymentStatus status) {
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        payment.setStatus(status);
        payment = paymentRepository.save(payment);
        
        log.info("Updated payment {} status to {}", paymentIntentId, status);
        return mapToResponse(payment);
    }

    @Transactional
    public void handleWebhookEvent(String payload, String signature) {
        // Webhook handling will be implemented in controller
        log.info("Processing webhook event");
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentIntentId(payment.getPaymentIntentId())
                .type(payment.getType())
                .referenceId(payment.getReferenceId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .clientSecret(payment.getClientSecret())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
