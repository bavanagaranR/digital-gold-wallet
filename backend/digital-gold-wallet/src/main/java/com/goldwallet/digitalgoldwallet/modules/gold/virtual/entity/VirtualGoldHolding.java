package com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity;

import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

@Entity                                                            // SQL: creates table "virtual_gold_holdings"
@Table(name = "virtual_gold_holdings")                            //specifies exact table name in DB CREATE TABLE virtual_gold_holdings ( ... );
@Builder                                                         // helps create object step-by-step, clean & readable has No SQL impact
public class VirtualGoldHolding {

    @Id                                                         // @Id → marks this field as PRIMARY KEY, PRIMARY KEY (holding_id)
    @GeneratedValue(strategy = GenerationType.IDENTITY)        // @GeneratedValue → ID is auto-generated, IDENTITY → uses AUTO_INCREMENT in DB, holding_id BIGINT AUTO_INCREMENT
    @Column(name = "holding_id")                               // maps field to column name
    private Long holdingId;                                    //holdingId identifies one gold holding entry uniquely in the database

    @NotNull(message = "User cannot be null")                 // validation cannot be null before saving, handled at application level
    @ManyToOne(fetch = FetchType.LAZY)                       // many holdings belong to one user, LAZY → user data is loaded only when needed, SQL: FOREIGN KEY relationship
    @JoinColumn(name = "user_id", nullable = false)         // creates foreign key column, SQL: user_id BIGINT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(user_id) it should match existing user id in the users table
    private User user;

    @NotNull(message = "Vendor branch cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private VendorBranch branch;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Quantity must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid quantity format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal quantity;

    @CreationTimestamp                                        // automatically sets current timestamp when record is inserted
    @Column(name = "created_at", updatable = false)          // cannot be updated after creation, SQL: created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    private LocalDateTime createdAt;

    // no-args constructor, required by Hibernate (uses reflection to create object)
    public VirtualGoldHolding() {
    }

    // all-args constructor, useful for manual object creation / testing
    public VirtualGoldHolding(Long holdingId, User user, VendorBranch branch, BigDecimal quantity, LocalDateTime createdAt) {
        this.holdingId = holdingId;
        this.user = user;
        this.branch = branch;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    // getters and setters (used by Hibernate + application logic)
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