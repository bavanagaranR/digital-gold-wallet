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

    //no-args constructor
    public PhysicalGoldTransaction() { }

    //all-args constructor

    public PhysicalGoldTransaction(Long transactionId, User user, VendorBranch branch, BigDecimal quantity, Address deliveryAddress, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.user = user;
        this.branch = branch;
        this.quantity = quantity;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = createdAt;
    }

    //getters and setters
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}