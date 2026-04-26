package com.goldwallet.digitalgoldwallet.modules.wallet.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.request.WalletTransactionRequest;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.response.WalletResponse;
import com.goldwallet.digitalgoldwallet.modules.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// Controller for managing user wallets.
// Provides endpoints for wallet balance inquiries and transactions (credit/debit).

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;


//   Credits an amount to the user's wallet.
//   @param userId The unique identifier of the user.
//   @param request The transaction details containing the amount to credit.
//   @return The updated wallet details.

    @PostMapping("/{userId}/credit")
    public ResponseEntity<ApiResponse<WalletResponse>> credit(
            @PathVariable Long userId,
            @Valid @RequestBody WalletTransactionRequest request)
    {
        return ResponseEntity.ok(ApiResponse.success(walletService.credit(userId, request)));
    }


//     Debits an amount from the user's wallet.
//     @param userId The unique identifier of the user.
//     @param request The transaction details containing the amount to debit.
//     @return The updated wallet details.

    @PostMapping("/{userId}/debit")
    public ResponseEntity<ApiResponse<WalletResponse>> debit(
            @PathVariable Long userId,
            @Valid @RequestBody WalletTransactionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(walletService.debit(userId, request)));
    }

//    Retrieves the current balance of the user's wallet.
//    @param userId The unique identifier of the user.
//    @return The wallet balance and details.
    @GetMapping("/{userId}/balance")
    public ResponseEntity<ApiResponse<WalletResponse>> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(walletService.getBalance(userId)));
    }
}
