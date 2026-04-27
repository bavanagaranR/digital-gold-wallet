package com.goldwallet.digitalgoldwallet.modules.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;




import org.hibernate.annotations.CreationTimestamp;



@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^$|^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Invalid email format")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    // Many users can be linked to one address record,Example: multiple users from same family can share one address
    // Improves performance by avoiding unnecessary DB queries
    @ManyToOne(fetch = FetchType.LAZY)// LAZY = Address data is not loaded immediately with User,Address loads only when user.getAddress() is called
    // Use address_id column in users table as foreign key,This column references address_id primary key in addresses table
    @JoinColumn(name = "address_id")
    // Address object linked to this user,Access like: user.getAddress().getCity()
    private Address address;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    @Column(precision = 18, scale = 2)
    @Builder.Default// Keeps default field value when object is created using @Builder,Without this builder may ignore initialized value and set null/default
    private BigDecimal balance = BigDecimal.ZERO;//Yes, BigDecimal is a class,we should create object for it

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public User() {
    }

    public User(Long userId, String email, String name, Address address, BigDecimal balance, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.address = address;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}