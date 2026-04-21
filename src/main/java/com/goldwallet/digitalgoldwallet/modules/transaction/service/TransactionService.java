package com.goldwallet.digitalgoldwallet.modules.transaction.service;

import com.goldwallet.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    TransactionResponse getTransactionById(Long transactionId);
    List<TransactionResponse> getUserTransactions(Long userId);
    List<TransactionResponse> getBranchTransactions(Long branchId);
    List<TransactionResponse> getTransactionsGreaterThanAmount(BigDecimal amount);
    List<TransactionResponse> getTransactionsByType(String type);
    List<TransactionResponse> getTransactionsByStatus(String status);
}
