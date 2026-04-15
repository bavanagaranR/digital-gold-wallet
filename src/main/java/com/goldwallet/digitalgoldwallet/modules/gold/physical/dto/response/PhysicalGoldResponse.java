package com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PhysicalGoldResponse {
    private Long transactionId;
    private Long userId;
    private String userName;
    private Long branchId;
    private BigDecimal quantity;
    private Long deliveryAddressId;
    private LocalDateTime createdAt;
}
