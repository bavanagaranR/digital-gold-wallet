package com.goldwallet.digitalgoldwallet.modules.transaction.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;
import com.goldwallet.digitalgoldwallet.modules.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

// REST controller that exposes all transaction-related API endpoints
@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    // Injects the transaction service to delegate business logic
    @Autowired
    private TransactionService transactionService;

    // Fetches a single transaction by its unique ID
    @GetMapping("/transactions/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionById(id)));
    }

    // Retrieves paginated transaction history for a specific user
    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getUserTransactions(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getUserTransactions(userId, pageable)));
    }

    // Retrieves paginated transactions processed at a specific vendor branch
    @GetMapping("/branches/{branchId}/transactions")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getBranchTransactions(@PathVariable Long branchId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getBranchTransactions(branchId, pageable)));
    }

    // Filters transactions by status (SUCCESS or FAILED)
    @GetMapping("/transactions/status/{status}")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getByStatus(@PathVariable String status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionsByStatus(status, pageable)));
    }

    // Filters transactions by type (BUY, SELL, CONVERT_TO_PHYSICAL)
    @GetMapping("/transactions/type/{type}")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getByType(@PathVariable String type, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionsByType(type, pageable)));
    }

    // Returns all transactions with amount >= the given value
    @GetMapping("/transactions/amount")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getByAmount(@RequestParam BigDecimal amount, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionsGreaterThanAmount(amount, pageable)));
    }
}
