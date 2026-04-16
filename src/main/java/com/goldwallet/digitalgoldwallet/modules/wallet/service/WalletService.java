package com.goldwallet.digitalgoldwallet.modules.wallet.service;

import com.goldwallet.digitalgoldwallet.modules.wallet.dto.request.WalletTransactionRequest;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.response.WalletResponse;

public interface WalletService {
    WalletResponse credit(Long userId, WalletTransactionRequest request);
    WalletResponse debit(Long userId, WalletTransactionRequest request);
    WalletResponse getBalance(Long userId);
}
