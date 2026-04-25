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

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorBranchRepository branchRepository;

    @Override
    public TransactionResponse getTransactionById(Long transactionId) {
        TransactionHistory tx = transactionHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));
        return mapToResponse(tx);
    }

    @Override
    public Page<TransactionResponse> getUserTransactions(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("User not found: " + userId);
        return transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<TransactionResponse> getBranchTransactions(Long branchId, Pageable pageable) {
        if (!branchRepository.existsById(branchId)) throw new ResourceNotFoundException("Branch not found: " + branchId);
        return transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(branchId, pageable)
                .map(this::mapToResponse);
    }

    private TransactionResponse mapToResponse(TransactionHistory tx) {
        return TransactionResponse.builder()
                .transactionId(tx.getTransactionId())
                .userId(tx.getUser() != null ? tx.getUser().getUserId() : null)
                .userName(tx.getUser() != null ? tx.getUser().getName() : null)
                .branchId(tx.getBranch() != null ? tx.getBranch().getBranchId() : null)
                .transactionType(tx.getTransactionType())
                .transactionStatus(tx.getTransactionStatus())
                .quantity(tx.getQuantity())
                .amount(tx.getAmount())
                .createdAt(tx.getCreatedAt())
                .build();
    }
    @Override
    public Page<TransactionResponse> getTransactionsByStatus(String status, Pageable pageable) {
        TransactionHistory.TransactionStatus enumStatus;
        try {
            enumStatus = TransactionHistory.TransactionStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid status: " + status);
        }
        return transactionHistoryRepository.findByStatus(enumStatus, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<TransactionResponse> getTransactionsByType(String type, Pageable pageable) {
        TransactionHistory.TransactionType enumType =
                TransactionHistory.TransactionType.valueOf(type.toUpperCase());
        return transactionHistoryRepository.findByTransactionType(enumType, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<TransactionResponse> getTransactionsGreaterThanAmount(BigDecimal amount, Pageable pageable) {
        return transactionHistoryRepository.findTransactionsGreaterThan(amount, pageable)
                .map(this::mapToResponse);
    }
}
