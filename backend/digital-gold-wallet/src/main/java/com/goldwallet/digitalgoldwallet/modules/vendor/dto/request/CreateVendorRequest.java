package com.goldwallet.digitalgoldwallet.modules.vendor.dto.request;


import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public class CreateVendorRequest {

    @NotBlank(message = "Vendor name cannot be empty")
    @Size(max = 100, message = "Vendor name must be less than 100 characters")
    private String vendorName;

    @Size(max = 1000, message = "Description too long")
    private String description;

    @Size(max = 100, message = "Contact person name must be less than 100 characters")
    private String contactPersonName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String contactEmail;


    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+\\d{1,3}\\s?)?[0-9]{10}$",
            message = "Invalid phone number"
    )
    private String contactPhone;
    @URL(message = "Invalid website URL")
    private String websiteUrl;

    @NotNull(message = "Gold price cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Gold price must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid price format")
    private BigDecimal currentGoldPrice;


    // getters and setters

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

    public BigDecimal getCurrentGoldPrice() {
        return currentGoldPrice;
    }

    public void setCurrentGoldPrice(BigDecimal currentGoldPrice) {
        this.currentGoldPrice = currentGoldPrice;
    }

}