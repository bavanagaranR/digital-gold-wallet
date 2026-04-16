package com.goldwallet.digitalgoldwallet.modules.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {
    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
