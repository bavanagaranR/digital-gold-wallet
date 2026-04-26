package com.goldwallet.digitalgoldwallet.modules.user.dto.request;

import jakarta.validation.constraints.Email;

public class UpdateUserRequest {
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private Long addressId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
