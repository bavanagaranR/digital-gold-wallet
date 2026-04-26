package com.goldwallet.digitalgoldwallet.modules.vendor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

// DTO for updating vendor details
// Fields are optional since this is used for update operations
// Validation works only when values are provided
public class UpdateVendorRequest {

    // Vendor name can be updated if given, maximum 100 characters allowed
    @Size(max = 100, message = "Vendor name must be less than 100 characters")
    private String vendorName;

    // Description is optional, but should not exceed 1000 characters
    @Size(max = 1000, message = "Description too long")
    private String description;

    // Contact person name is optional with a max limit of 100 characters
    @Size(max = 100, message = "Contact person name must be less than 100 characters")
    private String contactPersonName;

    // Email is optional, but must be valid if provided
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String contactEmail;

    // Phone number is optional, but must match the required format if given
    @Pattern(
            regexp = "^(\\+\\d{1,3}\\s?)?[0-9]{10}$",
            message = "Invalid phone number"
    )
    private String contactPhone;

    // Website URL is optional, but must be valid if provided
    @URL(message = "Invalid website URL")
    private String websiteUrl;

    // Gold price can be updated if provided
    private BigDecimal currentGoldPrice;


    // Getters and Setters

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