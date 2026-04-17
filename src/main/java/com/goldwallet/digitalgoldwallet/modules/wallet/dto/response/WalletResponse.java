package com.goldwallet.digitalgoldwallet.modules.wallet.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletResponse {
    private Long userId;
    private String userName;
    private BigDecimal balance;
    private String message;
}
