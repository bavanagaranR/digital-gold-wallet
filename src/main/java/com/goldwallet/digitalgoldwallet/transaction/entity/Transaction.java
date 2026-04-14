package com.goldwallet.digitalgoldwallet.transaction.entity;

import com.goldwallet.digitalgoldwallet.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transaction_history")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    private String transactionType;
    // Example: "Buy", "Sell", "Convert to Physical"

    private String transactionStatus;
    // Example: "Success", "Failed"

    private Double quantity;
    private Double amount;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private VendorBranch branch;
}