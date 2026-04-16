package com.goldwallet.digitalgoldwallet.modules.transaction.service;

import com.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse getTransactionById(Long transactionId);
    List<TransactionResponse> getUserTransactions(Long userId);
    List<TransactionResponse> getBranchTransactions(Long branchId);
}
