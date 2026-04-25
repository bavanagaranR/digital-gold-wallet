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

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionById(id)));
    }

    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getUserTransactions(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getUserTransactions(userId, pageable)));
    }

    @GetMapping("/branches/{branchId}/transactions")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getBranchTransactions(@PathVariable Long branchId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getBranchTransactions(branchId, pageable)));
    }
    @GetMapping("/transactions/status/{status}")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getByStatus(@PathVariable String status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionsByStatus(status, pageable)));
    }

    @GetMapping("/transactions/type/{type}")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getByType(@PathVariable String type, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionsByType(type, pageable)));
    }

    @GetMapping("/transactions/amount")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getByAmount(@RequestParam BigDecimal amount, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionsGreaterThanAmount(amount, pageable)));
    }
}
