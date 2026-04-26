package com.goldwallet.digitalgoldwallet.modules.wallet;

import com.goldwallet.digitalgoldwallet.common.exception.InsufficientBalanceException;
import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.request.WalletTransactionRequest;
import com.goldwallet.digitalgoldwallet.modules.wallet.dto.response.WalletResponse;
import com.goldwallet.digitalgoldwallet.modules.wallet.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    // ================= POSITIVE TESTS (10) =================

    // 1 - Credit increases user balance correctly
    @Test
    void testCredit_success_balanceIncreased() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Cathy");
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        WalletResponse res = walletService.credit(1L, req);

        assertEquals(new BigDecimal("150"), res.getBalance());
    }

    // 2 - Credit called multiple times accumulates balance
    @Test
    void testCredit_success_multipleCreditsCumulate() {
        User user = new User();
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        walletService.credit(1L, req);
        walletService.credit(1L, req);

        assertEquals(new BigDecimal("200"), user.getBalance());
    }

    // 3 - Credit with zero amount keeps balance unchanged
    @Test
    void testCredit_success_zeroAmountKeepsBalance() {
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("500"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(BigDecimal.ZERO);

        WalletResponse res = walletService.credit(1L, req);

        assertEquals(new BigDecimal("500"), res.getBalance());
    }

    // 4 - Credit calls userRepository save once
    @Test
    void testCredit_success_repositorySaveCalledOnce() {
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("200"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("100"));

        walletService.credit(1L, req);

        verify(userRepository, times(1)).save(user);
    }

    // 5 - Debit reduces user balance correctly
    @Test
    void testDebit_success_balanceDecreased() {
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        WalletResponse res = walletService.debit(1L, req);

        assertEquals(new BigDecimal("50"), res.getBalance());
    }

    // 6 - Debit with exact balance reduces balance to zero
    @Test
    void testDebit_success_exactBalanceBecomesZero() {
        User user = new User();
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("100"));

        WalletResponse res = walletService.debit(1L, req);

        assertEquals(BigDecimal.ZERO, res.getBalance());
    }

    // 7 - Debit calls userRepository save once
    @Test
    void testDebit_success_repositorySaveCalledOnce() {
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("300"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("100"));

        walletService.debit(1L, req);

        verify(userRepository, times(1)).save(user);
    }

    // 8 - Get balance returns correct balance for user
    @Test
    void testGetBalance_success_returnsCurrentBalance() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Cathy");
        user.setBalance(new BigDecimal("500"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletResponse res = walletService.getBalance(1L);

        assertEquals(new BigDecimal("500"), res.getBalance());
    }

    // 9 - Get balance after credit reflects updated amount
    @Test
    void testGetBalance_success_afterCreditReflectsUpdate() {
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("1000"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletResponse res = walletService.getBalance(1L);

        assertEquals(new BigDecimal("1000"), res.getBalance());
    }

    // 10 - Get balance returns user name in response
    @Test
    void testGetBalance_success_userNameInResponse() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Subha");
        user.setBalance(new BigDecimal("250"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletResponse res = walletService.getBalance(1L);

        assertNotNull(res);
        assertEquals(new BigDecimal("250"), res.getBalance());
    }

    // ================= NEGATIVE TESTS (5) =================

    // 11 - Credit throws when user not found
    @Test
    void testCredit_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.credit(1L, req));
    }

    // 12 - Debit throws when user not found
    @Test
    void testDebit_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        WalletTransactionRequest req = new WalletTransactionRequest();

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.debit(1L, req));
    }

    // 13 - Debit throws when balance is insufficient
    @Test
    void testDebit_insufficientBalance_throwsException() {
        User user = new User();
        user.setBalance(new BigDecimal("10"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        assertThrows(InsufficientBalanceException.class,
                () -> walletService.debit(1L, req));
    }

    // 14 - Get balance throws when user not found
    @Test
    void testGetBalance_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.getBalance(1L));
    }

    // 15 - Debit throws when amount exceeds balance by a large margin
    @Test
    void testDebit_largeAmountExceedsBalance_throwsException() {
        User user = new User();
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("99999"));

        assertThrows(InsufficientBalanceException.class,
                () -> walletService.debit(1L, req));
    }
}
