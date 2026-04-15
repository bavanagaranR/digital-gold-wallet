package com.goldwallet.digitalgoldwallet.modules.transaction.service.impl;

import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;
import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import com.goldwallet.digitalgoldwallet.modules.transaction.repository.TransactionHistoryRepository;
import com.goldwallet.digitalgoldwallet.modules.transaction.service.TransactionService;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final UserRepository userRepository;
    private final VendorBranchRepository branchRepository;

    @Override
    public TransactionResponse getTransactionById(Long transactionId) {
        TransactionHistory tx = transactionHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));
        return mapToResponse(tx);
    }

    @Override
    public List<TransactionResponse> getUserTransactions(Long userId) {
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("User not found: " + userId);
        return transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getBranchTransactions(Long branchId) {
        if (!branchRepository.existsById(branchId)) throw new ResourceNotFoundException("Branch not found: " + branchId);
        return transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(branchId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
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
}
