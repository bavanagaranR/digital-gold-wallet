package com.goldwallet.digitalgoldwallet.modules.vendor.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BranchResponse {
    private Long branchId;
    private Long vendorId;
    private String vendorName;
    private Long addressId;
    private BigDecimal quantity;
    private LocalDateTime createdAt;
}
