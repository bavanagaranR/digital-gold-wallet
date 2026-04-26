package com.goldwallet.digitalgoldwallet.modules.vendor.entity;

import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.validation.constraints.*;


@Entity
@Table(name = "vendor_branches") // Maps this class to vendor_branches table
@Builder // Enables builder pattern for object creation
public class VendorBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    @Column(name = "branch_id")
    private Long branchId;

    @NotNull(message = "Vendor cannot be null") // Vendor is mandatory
    @ManyToOne(fetch = FetchType.LAZY) // Many branches belong to one vendor
    @JoinColumn(name = "vendor_id", nullable = false) // Foreign key to vendor table
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY) // Many branches can share same address
    @JoinColumn(name = "address_id", nullable = false) // Foreign key to address table
    private Address address;

    @NotNull(message = "Quantity cannot be null") // Quantity is required
    @DecimalMin(value = "0.00", inclusive = true, message = "Quantity cannot be negative") // No negative values
    @Digits(integer = 16, fraction = 2, message = "Invalid quantity format") // Precision control
    @Column(precision = 18, scale = 2) // Database column precision
    @Builder.Default
    private BigDecimal quantity = BigDecimal.ZERO; // Default value = 0

    @CreationTimestamp // Automatically sets creation time
    @Column(name = "created_at", updatable = false) // Cannot be updated later
    private LocalDateTime createdAt;

    // Default constructor required by JPA
    public VendorBranch() {
    }

    // Parameterized constructor for manual object creation
    public VendorBranch(Long branchId, Vendor vendor, Address address, BigDecimal quantity, LocalDateTime createdAt) {
        this.branchId = branchId;
        this.vendor = vendor;
        this.address = address;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    // Getters and setters for accessing and modifying fields

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