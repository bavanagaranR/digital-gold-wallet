package com.goldwallet.digitalgoldwallet.modules.vendor.dto.request;


import jakarta.validation.constraints.NotNull;

public class CreateBranchRequest {

    @NotNull(message = "Address ID cannot be null")
    private Long addressId;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}