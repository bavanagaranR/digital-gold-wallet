package com.goldwallet.digitalgoldwallet.modules.gold.physical.entity;


import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;





import jakarta.validation.constraints.*;


@Entity
@Table(name = "physical_gold_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhysicalGoldTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @NotNull(message = "User cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Vendor branch cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private VendorBranch branch;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Quantity must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid quantity format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}