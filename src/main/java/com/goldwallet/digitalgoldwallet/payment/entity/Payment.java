package com.goldwallet.digitalgoldwallet.payment.entity;


import com.goldwallet.digitalgoldwallet.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required")
    private User user;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    // ENUM replaced with String
    @NotBlank(message = "Payment method is required")
    @Column(name = "payment_method")
    private String paymentMethod;

    @NotBlank(message = "Transaction type is required")
    @Column(name = "transaction_type")
    private String transactionType;

    @NotBlank(message = "Payment status is required")
    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}