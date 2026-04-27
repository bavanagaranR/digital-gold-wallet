package com.goldwallet.digitalgoldwallet.modules.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class UpdateUserRequest {
    @Pattern (regexp = "^[A-Za-z]+$", message = "Name must contain only letters")
    private String name;

    @Pattern(regexp = "^$|^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Invalid email format")
    private String email;

    @Min(value = 1, message = "User ID must be greater than 0")
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
