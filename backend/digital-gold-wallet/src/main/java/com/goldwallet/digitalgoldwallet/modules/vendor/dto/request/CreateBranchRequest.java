package com.goldwallet.digitalgoldwallet.modules.vendor.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;

public class CreateBranchRequest {

    // Address ID is required to link branch with an address
    @NotNull(message = "Address ID cannot be null")
    private Long addressId;

    @NotNull(message = "Quantity cannot be null")  // Quantity is required and must follow validation rules
    @DecimalMin(value = "0.01", inclusive = true, message = "Quantity must be greater than 0")// Ensures quantity is not negative (>= 0.00)
    @Digits(integer = 16, fraction = 2, message = "Invalid quantity format")
    private BigDecimal quantity;



    // Getter for addressId
    public Long getAddressId() {
        return addressId;
    }

    // Setter for addressId
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    // Getter for quantity
    public BigDecimal getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}