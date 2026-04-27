package com.goldwallet.digitalgoldwallet.modules.payment.dto.response;

import com.goldwallet.digitalgoldwallet.modules.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Builder

public class PaymentResponse {
    private Long paymentId;
    private Long userId;
    private String userName;
    private BigDecimal amount;
    private Payment.PaymentMethod paymentMethod;
    private Payment.TransactionType transactionType;
    private Payment.PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

    public PaymentResponse(Long paymentId, Long userId, String userName, BigDecimal amount, Payment.PaymentMethod paymentMethod, Payment.TransactionType transactionType, Payment.PaymentStatus paymentStatus, LocalDateTime createdAt) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.userName = userName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionType = transactionType;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

    public PaymentResponse() {
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Payment.PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Payment.PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
