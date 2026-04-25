package com.goldwallet.digitalgoldwallet.modules.wallet.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.request.WalletTransactionRequest;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.response.WalletResponse;
import com.goldwallet.digitalgoldwallet.modules.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/{userId}/credit")
    public ResponseEntity<ApiResponse<WalletResponse>> credit(
            @PathVariable Long userId,
            @Valid @RequestBody WalletTransactionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(walletService.credit(userId, request)));
    }

    @PostMapping("/{userId}/debit")
    public ResponseEntity<ApiResponse<WalletResponse>> debit(
            @PathVariable Long userId,
            @Valid @RequestBody WalletTransactionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(walletService.debit(userId, request)));
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<ApiResponse<WalletResponse>> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(walletService.getBalance(userId)));
    }
}
