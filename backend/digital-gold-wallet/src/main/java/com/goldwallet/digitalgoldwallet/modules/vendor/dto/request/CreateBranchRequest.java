package com.goldwallet.digitalgoldwallet.modules.vendor.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;

public class CreateBranchRequest {

    @NotNull(message = "Address ID cannot be null")
    private Long addressId;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Quantity cannot be negative")
    @Digits(integer = 16, fraction = 2, message = "Invalid quantity format")
    private BigDecimal quantity;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}