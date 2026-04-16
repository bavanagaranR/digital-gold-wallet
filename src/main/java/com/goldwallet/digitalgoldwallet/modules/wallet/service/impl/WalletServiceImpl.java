package com.goldwallet.digitalgoldwallet.modules.wallet.service.impl;

import com.goldwallet.digitalgoldwallet.common.exception.InsufficientBalanceException;
import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.request.WalletTransactionRequest;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.response.WalletResponse;
import com.goldwallet.digitalgoldwallet.modules.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public WalletResponse credit(Long userId, WalletTransactionRequest request) {
        User user = findUserOrThrow(userId);
        user.setBalance(user.getBalance().add(request.getAmount()));
        userRepository.save(user);
        log.info("Credited {} to user {}, new balance: {}", request.getAmount(), userId, user.getBalance());
        return buildResponse(user, "Wallet credited successfully");
    }

    @Override
    @Transactional
    public WalletResponse debit(Long userId, WalletTransactionRequest request) {
        User user = findUserOrThrow(userId);
        if (user.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient wallet balance. Available: " + user.getBalance());
        }
        user.setBalance(user.getBalance().subtract(request.getAmount()));
        userRepository.save(user);
        log.info("Debited {} from user {}, new balance: {}", request.getAmount(), userId, user.getBalance());
        return buildResponse(user, "Wallet debited successfully");
    }

    @Override
    public WalletResponse getBalance(Long userId) {
        User user = findUserOrThrow(userId);
        return buildResponse(user, "Balance fetched successfully");
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private WalletResponse buildResponse(User user, String message) {
        return WalletResponse.builder()
                .userId(user.getUserId())
                .userName(user.getName())
                .balance(user.getBalance())
                .message(message)
                .build();
    }
}
