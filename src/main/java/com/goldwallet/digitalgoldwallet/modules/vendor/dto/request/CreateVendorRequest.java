package com.goldwallet.digitalgoldwallet.modules.vendor.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateVendorRequest {
    @NotBlank(message = "Vendor name is required")
    private String vendorName;
    private String description;
    private String contactPersonName;
    private String contactEmail;
    private String contactPhone;
    private String websiteUrl;
    private BigDecimal currentGoldPrice;
}