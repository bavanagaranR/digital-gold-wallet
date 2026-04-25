package com.goldwallet.digitalgoldwallet.modules.transaction.dto.response;
import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Builder
//DTO class for Transaction
public class TransactionResponse {
    private Long transactionId;
    private Long userId;
    private String userName;
    private Long branchId;
    private TransactionHistory.TransactionType transactionType;
    private TransactionHistory.TransactionStatus transactionStatus;
    private BigDecimal quantity;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public TransactionHistory.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionHistory.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionHistory.TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionHistory.TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}