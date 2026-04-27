package com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BuyGoldRequest {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "UserId must be greater than 0")
    private Long userId;

    @NotNull(message = "Branch ID is required")
    @Min(value = 1, message = "BranchId must be greater than 0")
    private Long branchId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Invalid quantity format")

    private BigDecimal quantity;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
