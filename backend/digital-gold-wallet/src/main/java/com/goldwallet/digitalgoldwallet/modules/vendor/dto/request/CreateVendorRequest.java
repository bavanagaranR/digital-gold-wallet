package com.goldwallet.digitalgoldwallet.modules.vendor.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public class CreateVendorRequest {

    // Vendor name is required and should not exceed 100 characters
    @NotBlank(message = "Vendor name cannot be empty")
    @Size(max = 100, message = "Vendor name must be less than 100 characters")
    private String vendorName;

    // Optional description with max length restriction
    @Size(max = 1000, message = "Description too long")
    private String description;

    // Optional contact person name with length validation
    @Size(max = 100, message = "Contact person name must be less than 100 characters")
    private String contactPersonName;

    // Email is required and must be valid format
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String contactEmail;

    // Phone number is required and must follow valid pattern
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+\\d{1,3}\\s?)?[0-9]{10}$",
            message = "Invalid phone number"
    )
    private String contactPhone;

    // Website URL must be valid if provided
    @URL(message = "Invalid website URL")
    private String websiteUrl;

    // Gold price is required and must be greater than 0
    @NotNull(message = "Gold price cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Gold price must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid price format")
    private BigDecimal currentGoldPrice;

    // Getter for vendorName
    public String getVendorName() {
        return vendorName;
    }

    // Setter for vendorName
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter for contactPersonName
    public String getContactPersonName() {
        return contactPersonName;
    }

    // Setter for contactPersonName
    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    // Getter for contactEmail
    public String getContactEmail() {
        return contactEmail;
    }

    // Setter for contactEmail
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    // Getter for contactPhone
    public String getContactPhone() {
        return contactPhone;
    }

    // Setter for contactPhone
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    // Getter for websiteUrl
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    // Setter for websiteUrl
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    // Getter for currentGoldPrice
    public BigDecimal getCurrentGoldPrice() {
        return currentGoldPrice;
    }

    // Setter for currentGoldPrice
    public void setCurrentGoldPrice(BigDecimal currentGoldPrice) {
        this.currentGoldPrice = currentGoldPrice;
    }
}