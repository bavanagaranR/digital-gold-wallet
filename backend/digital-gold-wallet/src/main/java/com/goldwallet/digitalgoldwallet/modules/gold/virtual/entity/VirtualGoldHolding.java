package com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity;

import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.validation.constraints.*;


@Entity
@Table(name = "virtual_gold_holdings")
@Builder
public class VirtualGoldHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holding_id")
    private Long holdingId;

    @NotNull(message = "User cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Vendor branch cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private VendorBranch branch;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Quantity must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid quantity format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal quantity;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    //no-args constructor
    public VirtualGoldHolding() {
    }

    //all-args constructor

    public VirtualGoldHolding(Long holdingId, User user, VendorBranch branch, BigDecimal quantity, LocalDateTime createdAt) {
        this.holdingId = holdingId;
        this.user = user;
        this.branch = branch;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    //getters and setters

    public Long getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(Long holdingId) {
        this.holdingId = holdingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VendorBranch getBranch() {
        return branch;
    }

    public void setBranch(VendorBranch branch) {
        this.branch = branch;
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