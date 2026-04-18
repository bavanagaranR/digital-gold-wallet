package com.goldwallet.digitalgoldwallet.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAddressRequest {
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;
}
