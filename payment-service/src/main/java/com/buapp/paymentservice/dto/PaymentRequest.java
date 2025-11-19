package com.buapp.paymentservice.dto;

import com.buapp.paymentservice.model.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    @NotNull(message = "Payment type cannot be null")
    private PaymentType type;

    @NotNull(message = "Reference ID cannot be null")
    private Long referenceId; // ticketId or abonnementId

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String currency = "usd"; // default currency
}
