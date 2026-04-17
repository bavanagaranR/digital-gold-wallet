package com.goldwallet.digitalgoldwallet.modules.transaction.dto.response;
import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long transactionId;
    private Long userId;
    private String userName;
    private Long branchId;
    private TransactionHistory.TransactionType transactionType;
    private TransactionHistory.TransactionStatus transactionStatus;
    private BigDecimal quantity;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}