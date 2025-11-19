package com.buapp.paymentservice.dto;

import com.buapp.paymentservice.model.PaymentStatus;
import com.buapp.paymentservice.model.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private String paymentIntentId;
    private PaymentType type;
    private Long referenceId;
    private Long userId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String clientSecret; // For Stripe Elements integration
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
