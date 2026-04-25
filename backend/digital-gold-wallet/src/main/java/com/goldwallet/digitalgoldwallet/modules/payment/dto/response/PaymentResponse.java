package com.goldwallet.digitalgoldwallet.modules.payment.dto.response;

import com.goldwallet.digitalgoldwallet.modules.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private Long userId;
    private String userName;
    private BigDecimal amount;
    private Payment.PaymentMethod paymentMethod;
    private Payment.TransactionType transactionType;
    private Payment.PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
}
