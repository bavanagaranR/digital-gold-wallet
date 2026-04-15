package com.goldwallet.digitalgoldwallet.vendor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Long vendorId;

    @NotBlank
    @Column(name = "vendor_name")
    private String vendorName;

    private String description;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Email
    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "total_gold_quantity")
    private BigDecimal totalGoldQuantity = BigDecimal.ZERO;

    @Column(name = "current_gold_price")
    private BigDecimal currentGoldPrice = new BigDecimal("5700.00");

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
