package com.goldwallet.digitalgoldwallet.vendor.entity;

import com.goldwallet.digitalgoldwallet.address.entity.Address;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
