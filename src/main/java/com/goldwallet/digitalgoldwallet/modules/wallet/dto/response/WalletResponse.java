package com.goldwallet.digitalgoldwallet.modules.wallet.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

//wallet response dto updated
@Builder
public class WalletResponse {
    private Long userId;
    private String userName;
    private BigDecimal balance;
    private String message;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
