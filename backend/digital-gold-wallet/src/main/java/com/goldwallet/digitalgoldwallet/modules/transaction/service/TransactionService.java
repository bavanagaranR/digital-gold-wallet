package com.goldwallet.digitalgoldwallet.modules.transaction.service;

import com.goldwallet.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

// Service interface defining all business operations related to transactions
// Implemented by TransactionServiceImpl — promotes loose coupling and testability
public interface TransactionService {

    // Retrieves a single transaction by its unique ID
    TransactionResponse getTransactionById(Long transactionId);

    // Retrieves paginated transaction history for a specific user
    Page<TransactionResponse> getUserTransactions(Long userId, Pageable pageable);

    // Retrieves paginated transactions processed at a specific vendor branch
    Page<TransactionResponse> getBranchTransactions(Long branchId, Pageable pageable);

    // Retrieves paginated transactions where the amount meets or exceeds the given threshold
    Page<TransactionResponse> getTransactionsGreaterThanAmount(BigDecimal amount, Pageable pageable);

    // Retrieves paginated transactions filtered by type (BUY, SELL, CONVERT_TO_PHYSICAL)
    Page<TransactionResponse> getTransactionsByType(String type, Pageable pageable);

    // Retrieves paginated transactions filtered by status (SUCCESS, FAILED)
    Page<TransactionResponse> getTransactionsByStatus(String status, Pageable pageable);
}
