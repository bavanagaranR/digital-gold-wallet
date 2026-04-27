package com.goldwallet.digitalgoldwallet.modules.payment.dto.request;

import com.goldwallet.digitalgoldwallet.modules.payment.entity.Payment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

//payment request dto updated

public class InitiatePaymentRequest {
    @NotNull(message = "User ID is * required")
    @Min(value = 1, message = "User ID must be greater than 0")
    private Long userId;

    @NotNull(message = "Amount is * required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Payment Method * required")
    private Payment.PaymentMethod paymentMethod;

    @NotNull(message = "Transaction Type * requiered")
    private Payment.TransactionType transactionType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Payment.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Payment.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Payment.TransactionType transactionType) {
        this.transactionType = transactionType;
    }


}
