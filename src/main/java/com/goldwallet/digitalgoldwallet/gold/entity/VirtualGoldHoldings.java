package com.goldwallet.digitalgoldwallet.gold.entity;

import com.goldwallet.digitalgoldwallet.user.entity.Address;
import com.goldwallet.digitalgoldwallet.user.entity.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class VirtualGoldHoldings {
    @Entity
    @Table(name = "virtual_gold_holdings")

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer holdingId;

        private Double quantity;

        private LocalDateTime createdAt;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "branch_id")
        private VendorBranch branch;
    }@Entity
    @Table(name = "physical_gold_transactions")
    public class PhysicalGoldTransaction {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer transactionId;

        private Double quantity;

        private LocalDateTime createdAt;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "branch_id")
        private VendorBranch branch;

        @ManyToOne
        @JoinColumn(name = "delivery_address_id")
        private Address deliveryAddress;
    }

