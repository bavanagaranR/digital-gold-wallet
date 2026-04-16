package com.goldwallet.digitalgoldwallet.payment.entity;

import com.goldwallet.digitalgoldwallet.user.entity.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    private Double amount;

    private String paymentMethod;
    // Example: "Credit Card", "Google Pay"

    private String transactionType;
    // Example: "Credited to wallet", "Debited from wallet"

    private String paymentStatus;
    // Example: "Success", "Failed"

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}