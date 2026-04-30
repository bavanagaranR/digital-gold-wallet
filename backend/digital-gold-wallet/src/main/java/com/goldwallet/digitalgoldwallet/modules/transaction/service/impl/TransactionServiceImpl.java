package com.goldwallet.digitalgoldwallet.modules.transaction.service.impl;

import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;
import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import com.goldwallet.digitalgoldwallet.modules.transaction.repository.TransactionHistoryRepository;
import com.goldwallet.digitalgoldwallet.modules.transaction.service.TransactionService;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

// Service implementation that contains the core business logic for all transaction operations
@Service
public class TransactionServiceImpl implements TransactionService {

    // Repository for performing CRUD and custom queries on transaction_history table
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    // Used to validate if a user exists before fetching their transactions
    @Autowired
    private UserRepository userRepository;

    // Used to validate if a branch exists before fetching its transactions
    @Autowired
    private VendorBranchRepository branchRepository;

    // Fetches a single transaction by ID — throws ResourceNotFoundException if not found
    @Override
    public TransactionResponse getTransactionById(Long transactionId) {
        // Query the DB by primary key; throw a descriptive error if record is absent
        TransactionHistory tx = transactionHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));
        // Map entity to response DTO before returning to controller
        return mapToResponse(tx);
    }

    // Returns paginated transaction history for a specific user, ordered by latest first
    @Override
    public Page<TransactionResponse> getUserTransactions(Long userId, Pageable pageable) {
        // Validate that the user exists before querying their transactions
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("User not found: " + userId);
        // Fetch and map all matching transactions to response DTOs
        return transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::mapToResponse);
    }

    // Returns paginated transactions for a specific vendor branch, ordered by latest first
    @Override
    public Page<TransactionResponse> getBranchTransactions(Long branchId, Pageable pageable) {
        // Validate that the branch exists before querying its transactions
        if (!branchRepository.existsById(branchId)) throw new ResourceNotFoundException("Branch not found: " + branchId);
        // Fetch and map all matching transactions to response DTOs
        return transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(branchId, pageable)
                .map(this::mapToResponse);
    }

    // Internal helper method to convert a TransactionHistory entity to a TransactionResponse DTO
    private TransactionResponse mapToResponse(TransactionHistory tx) {
        return TransactionResponse.builder()
                .transactionId(tx.getTransactionId())
                .userId(tx.getUser() != null ? tx.getUser().getUserId() : null)       // Null-safe: handle missing user
                .userName(tx.getUser() != null ? tx.getUser().getName() : null)       // Null-safe: handle missing user name
                .branchId(tx.getBranch() != null ? tx.getBranch().getBranchId() : null) // Null-safe: handle missing branch
                .transactionType(tx.getTransactionType())
                .transactionStatus(tx.getTransactionStatus())
                .quantity(tx.getQuantity())
                .amount(tx.getAmount())
                .createdAt(tx.getCreatedAt())
                .build();
    }

    // Filters transactions by status string — converts input to enum with validation
    @Override
    public Page<TransactionResponse> getTransactionsByStatus(String status, Pageable pageable) {
        TransactionHistory.TransactionStatus enumStatus;
        try {
            // Convert the incoming string to uppercase and parse as a valid enum constant
            enumStatus = TransactionHistory.TransactionStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            // Throw a clear error if the status value does not match any valid enum
            throw new RuntimeException("Invalid status: " + status);
        }
        // Query the repository with the resolved enum and map results to DTOs
        return transactionHistoryRepository.findByStatus(enumStatus, pageable)
                .map(this::mapToResponse);
    }

    // Filters transactions by type string — converts input to enum and delegates to repository
    @Override
    public Page<TransactionResponse> getTransactionsByType(String type, Pageable pageable) {
        // Parse the type string (e.g., "buy") to the corresponding enum (e.g., BUY)
        TransactionHistory.TransactionType enumType =
                TransactionHistory.TransactionType.valueOf(type.toUpperCase());
        // Fetch matching transactions and map to response DTOs
        return transactionHistoryRepository.findByTransactionType(enumType, pageable)
                .map(this::mapToResponse);
    }

    // Returns all transactions where the amount is greater than or equal to the given value
    @Override
    public Page<TransactionResponse> getTransactionsGreaterThanAmount(BigDecimal amount, Pageable pageable) {
        Page<TransactionHistory> transactions =
                transactionHistoryRepository.findTransactionsGreaterThan(amount, pageable);

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No transactions found greater than amount: " + amount);
        }
        // Delegate the amount filter query to the repository and map results to DTOs
        return transactionHistoryRepository.findTransactionsGreaterThan(amount, pageable)
                .map(this::mapToResponse);
    }
}
