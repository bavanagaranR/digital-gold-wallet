package com.goldwallet.digitalgoldwallet.modules.user.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private BigDecimal balance;
    private AddressResponse address;
    private LocalDateTime createdAt;
}
