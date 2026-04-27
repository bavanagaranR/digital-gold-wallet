package com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ConvertToPhysicalRequest {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "UserID must be greater than 0")
    private Long userId;

    @NotNull(message = "Branch ID is required")
    @Min(value = 1, message = "BranchID must be greater than 0")
    private Long branchId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @NotNull(message = "Delivery address ID is required")
    @Min(value = 1, message = "AddressID must be greater than 0")
    private Long deliveryAddressId;

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

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }
}
