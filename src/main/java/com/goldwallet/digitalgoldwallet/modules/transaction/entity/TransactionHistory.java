package com.goldwallet.digitalgoldwallet.modules.transaction.entity;

import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.validation.constraints.*;


@Entity
@Table(name = "transaction_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @NotNull(message = "User cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Vendor branch cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private VendorBranch branch;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", length = 30)
    private TransactionType transactionType;

    @NotNull(message = "Transaction status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", length = 20)
    private TransactionStatus transactionStatus;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Quantity must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid quantity format")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @NotNull(message = "Amount cannot be null")   // ✅ VALIDATION
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum TransactionType {
        BUY, SELL, CONVERT_TO_PHYSICAL
    }

    public enum TransactionStatus {
        SUCCESS, FAILED
    }
}