package com.goldwallet.digitalgoldwallet.modules.transaction.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;
import com.goldwallet.digitalgoldwallet.modules.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getUserTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getUserTransactions(userId)));
    }

    @GetMapping("/branches/{branchId}/transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getBranchTransactions(@PathVariable Long branchId) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getBranchTransactions(branchId)));
    }
    @GetMapping("/transactions/status/{status}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(
                ApiResponse.success(transactionService.getTransactionsByStatus(status))
        );
    }

    @GetMapping("/transactions/type/{type}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(
                ApiResponse.success(transactionService.getTransactionsByType(type))
        );
    }

    @GetMapping("/transactions/amount")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByAmount(
            @RequestParam BigDecimal amount) {

        return ResponseEntity.ok(
                ApiResponse.success(transactionService.getTransactionsGreaterThanAmount(amount))
        );
    }
}
