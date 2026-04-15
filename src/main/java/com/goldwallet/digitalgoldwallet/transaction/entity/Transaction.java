package com.goldwallet.digitalgoldwallet.transaction.entity;

import com.project.digitalgoldwallet.user.entity.User;
import com.project.digitalgoldwallet.vendor.entity.VendorBranch;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @NotNull
    private VendorBranch branch;

    // ENUM replaced with String
    @NotBlank
    @Column(name = "transaction_type")
    private String transactionType;

    @NotBlank
    @Column(name = "transaction_status")
    private String transactionStatus;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

