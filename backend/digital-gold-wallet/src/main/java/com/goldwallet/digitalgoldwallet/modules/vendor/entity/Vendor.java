package com.goldwallet.digitalgoldwallet.modules.vendor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

@Entity // Marks this class as a JPA entity (mapped to DB table)
@Table(name = "vendors") // Maps to "vendors" table
@Builder // Enables Builder pattern for object creation
public class Vendor {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    @Column(name = "vendor_id")
    private Long vendorId;

    // Vendor name (required, max 100 chars)
    @NotBlank(message = "Vendor name cannot be empty")
    @Size(max = 100, message = "Vendor name must be less than 100 characters")
    @Column(name = "vendor_name", nullable = false, length = 100)
    private String vendorName;

    // Optional description (stored as TEXT)
    @Size(max = 1000, message = "Description too long")
    @Column(columnDefinition = "TEXT")
    private String description;

    // Contact person name (optional)
    @Size(max = 100, message = "Contact person name must be less than 100 characters")
    @Column(name = "contact_person_name", length = 100)
    private String contactPersonName;

    // Email validation
    @Email(message = "Invalid email format")
    @Size(max = 100)
    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    // Phone number validation (required)
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+\\d{1,3}\\s?)?[0-9]{10}$",
            message = "Invalid phone number"
    )
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    // Website URL validation
    @URL(message = "Invalid website URL")
    @Column(name = "website_url")
    private String websiteUrl;

    // Total gold across all branches (default = 0)
    @Column(name = "total_gold_quantity", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalGoldQuantity = BigDecimal.ZERO;

    // Current gold price (required, > 0)
    @NotNull(message = "Gold price cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Gold price must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid price format")
    @Column(name = "current_gold_price", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal currentGoldPrice = new BigDecimal("5700.00");

    // Auto-generated timestamp when record is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // One vendor can have many branches
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VendorBranch> branches;

    // Default constructor required by JPA
    public Vendor() {
    }

    // Parameterized constructor
    public Vendor(Long vendorId, String vendorName, String description, String contactPersonName, String contactEmail, String contactPhone, String websiteUrl, BigDecimal totalGoldQuantity, BigDecimal currentGoldPrice, LocalDateTime createdAt, List<VendorBranch> branches) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.description = description;
        this.contactPersonName = contactPersonName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.websiteUrl = websiteUrl;
        this.totalGoldQuantity = totalGoldQuantity;
        this.currentGoldPrice = currentGoldPrice;
        this.createdAt = createdAt;
        this.branches = branches;
    }

    // Getters and setters
    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public BigDecimal getTotalGoldQuantity() {
        return totalGoldQuantity;
    }

    public void setTotalGoldQuantity(BigDecimal totalGoldQuantity) {
        this.totalGoldQuantity = totalGoldQuantity;
    }

    public BigDecimal getCurrentGoldPrice() {
        return currentGoldPrice;
    }

    public void setCurrentGoldPrice(BigDecimal currentGoldPrice) {
        this.currentGoldPrice = currentGoldPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<VendorBranch> getBranches() {
        return branches;
    }

    public void setBranches(List<VendorBranch> branches) {
        this.branches = branches;
    }
}