package com.goldwallet.digitalgoldwallet.modules.vendor.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class VendorResponse {
    private Long vendorId;
    private String vendorName;
    private String description;
    private String contactPersonName;
    private String contactEmail;
    private String contactPhone;
    private String websiteUrl;
    private BigDecimal totalGoldQuantity;
    private BigDecimal currentGoldPrice;
    private LocalDateTime createdAt;
}
