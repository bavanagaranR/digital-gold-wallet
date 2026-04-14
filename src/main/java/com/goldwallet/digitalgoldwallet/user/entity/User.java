package com.goldwallet.digitalgoldwallet.user.entity;

import com.goldwallet.digitalgoldwallet.user.entity.Address;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private Double balance;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
}