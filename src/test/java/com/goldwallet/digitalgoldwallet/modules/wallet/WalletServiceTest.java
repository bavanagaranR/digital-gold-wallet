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

    // ---------- CREDIT ----------

    @Test
    void testCredit_success() {
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

    @Test
    void testCredit_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.credit(1L, req));
    }

    // ---------- DEBIT ----------

    @Test
    void testDebit_success() {
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        WalletResponse res = walletService.debit(1L, req);

        assertEquals(new BigDecimal("50"), res.getBalance());
    }

    @Test
    void testDebit_insufficientBalance() {
        User user = new User();
        user.setBalance(new BigDecimal("10"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        assertThrows(InsufficientBalanceException.class,
                () -> walletService.debit(1L, req));
    }

    @Test
    void testDebit_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        WalletTransactionRequest req = new WalletTransactionRequest();

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.debit(1L, req));
    }

    // ---------- GET BALANCE ----------

    @Test
    void testGetBalance_success() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Cathy");
        user.setBalance(new BigDecimal("500"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletResponse res = walletService.getBalance(1L);

        assertEquals(new BigDecimal("500"), res.getBalance());
    }

    @Test
    void testGetBalance_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.getBalance(1L));
    }

    // ---------- EXTRA COVERAGE ----------

    @Test
    void testCredit_multipleTimes() {
        User user = new User();
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("50"));

        walletService.credit(1L, req);
        walletService.credit(1L, req);

        assertEquals(new BigDecimal("200"), user.getBalance());
    }

    @Test
    void testDebit_exactBalance() {
        User user = new User();
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        WalletTransactionRequest req = new WalletTransactionRequest();
        req.setAmount(new BigDecimal("100"));

        WalletResponse res = walletService.debit(1L, req);

        assertEquals(BigDecimal.ZERO, res.getBalance());
    }
}


