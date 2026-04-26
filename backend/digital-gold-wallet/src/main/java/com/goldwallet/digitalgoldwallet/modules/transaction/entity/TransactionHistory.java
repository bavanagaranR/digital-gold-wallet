package com.goldwallet.digitalgoldwallet.modules.transaction.entity;

import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.validation.constraints.*;


// Entity class representing a gold transaction record stored in the database
@Entity
@Table(name = "transaction_history")

@Builder
public class TransactionHistory {

    // Primary key — auto-generated unique ID for each transaction record
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    // Many transactions can belong to one user (lazy-loaded to avoid unnecessary DB joins)
    @NotNull(message = "User cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Each transaction is linked to a specific vendor branch where it was processed
    @NotNull(message = "Vendor branch cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private VendorBranch branch;

    // Stores the nature of the transaction: BUY, SELL, or CONVERT_TO_PHYSICAL
    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", length = 30)
    private TransactionType transactionType;

    // Stores whether the transaction succeeded or failed
    @NotNull(message = "Transaction status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", length = 20)
    private TransactionStatus transactionStatus;

    // Gold quantity involved in the transaction — must be positive, max 8 digits with 2 decimal places
    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Quantity must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid quantity format")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    // Monetary value of the transaction — must be at least 0.01
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    // Automatically set to the current timestamp when the transaction record is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Enum defining allowed transaction types for gold operations
    public enum TransactionType {
        BUY, SELL, CONVERT_TO_PHYSICAL
    }

    // Enum defining the outcome of a transaction
    public enum TransactionStatus {
        SUCCESS, FAILED
    }

    // Default no-arg constructor required by JPA
    public TransactionHistory() {
    }

    // All-args constructor used when building a TransactionHistory object manually
    public TransactionHistory(Long transactionId, User user, VendorBranch branch, TransactionType transactionType, TransactionStatus transactionStatus, BigDecimal quantity, BigDecimal amount, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.user = user;
        this.branch = branch;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.quantity = quantity;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public @NotNull(message = "User cannot be null") User getUser() {
        return user;
    }

    public void setUser(@NotNull(message = "User cannot be null") User user) {
        this.user = user;
    }

    public @NotNull(message = "Vendor branch cannot be null") VendorBranch getBranch() {
        return branch;
    }

    public void setBranch(@NotNull(message = "Vendor branch cannot be null") VendorBranch branch) {
        this.branch = branch;
    }

    public @NotNull(message = "Transaction type is required") TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(@NotNull(message = "Transaction type is required") TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public @NotNull(message = "Transaction status is required") TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(@NotNull(message = "Transaction status is required") TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public @NotNull(message = "Quantity cannot be null") @DecimalMin(value = "0.00", inclusive = false, message = "Quantity must be greater than 0") @Digits(integer = 8, fraction = 2, message = "Invalid quantity format") BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "Quantity cannot be null") @DecimalMin(value = "0.00", inclusive = false, message = "Quantity must be greater than 0") @Digits(integer = 8, fraction = 2, message = "Invalid quantity format") BigDecimal quantity) {
        this.quantity = quantity;
    }

    public @NotNull(message = "Amount cannot be null") @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0") @Digits(integer = 16, fraction = 2, message = "Invalid amount format") BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@NotNull(message = "Amount cannot be null") @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0") @Digits(integer = 16, fraction = 2, message = "Invalid amount format") BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}