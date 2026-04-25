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

    public VendorBranch() {
    }

    public VendorBranch(Long branchId, Vendor vendor, Address address, BigDecimal quantity, LocalDateTime createdAt) {
        this.branchId = branchId;
        this.vendor = vendor;
        this.address = address;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}