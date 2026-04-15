package com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class VirtualGoldResponse {
    private Long holdingId;
    private Long userId;
    private String userName;
    private Long branchId;
    private BigDecimal quantity;
    private BigDecimal totalValue;
    private LocalDateTime createdAt;
}
