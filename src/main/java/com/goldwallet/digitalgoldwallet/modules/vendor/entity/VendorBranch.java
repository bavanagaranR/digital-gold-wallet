package com.goldwallet.digitalgoldwallet.modules.vendor.entity;

import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.validation.constraints.*;


@Entity
@Table(name = "vendor_branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long branchId;

    @NotNull(message = "Vendor cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Quantity cannot be negative")
    @Digits(integer = 16, fraction = 2, message = "Invalid quantity format")
    @Column(precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal quantity = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}