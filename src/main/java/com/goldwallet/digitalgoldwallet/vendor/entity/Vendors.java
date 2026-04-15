package com.goldwallet.digitalgoldwallet.vendor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
public class Vendors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vendorId;

    private String vendorName;
    private String description;
    private String contactPersonName;
    private String contactEmail;
    private String contactPhone;
    private String websiteUrl;

    private Double totalGoldQuantity;
    private Double currentGoldPrice;

    private LocalDateTime createdAt;
}

