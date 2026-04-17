package com.goldwallet.digitalgoldwallet.modules.payment.dto.request;

import com.goldwallet.digitalgoldwallet.modules.payment.entity.Payment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InitiatePaymentRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private Payment.PaymentMethod paymentMethod;

    @NotNull(message = "Transaction type is required")
    private Payment.TransactionType transactionType;
}
