package com.goldwallet.digitalgoldwallet.modules.transaction.service;

import com.goldwallet.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface TransactionService {
    TransactionResponse getTransactionById(Long transactionId);
    Page<TransactionResponse> getUserTransactions(Long userId, Pageable pageable);
    Page<TransactionResponse> getBranchTransactions(Long branchId, Pageable pageable);
    Page<TransactionResponse> getTransactionsGreaterThanAmount(BigDecimal amount, Pageable pageable);
    Page<TransactionResponse> getTransactionsByType(String type, Pageable pageable);
    Page<TransactionResponse> getTransactionsByStatus(String status, Pageable pageable);
}
