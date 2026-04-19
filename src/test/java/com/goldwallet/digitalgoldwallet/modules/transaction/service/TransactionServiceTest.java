package com.goldwallet.digitalgoldwallet.modules.transaction.service;

import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.transaction.dto.response.TransactionResponse;
import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import com.goldwallet.digitalgoldwallet.modules.transaction.repository.TransactionHistoryRepository;
import com.goldwallet.digitalgoldwallet.modules.transaction.service.impl.TransactionServiceImpl;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VendorBranchRepository branchRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    // ---------- GET BY ID ----------

    @Test
    void testGetTransactionById_success() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(tx));

        TransactionResponse response = transactionService.getTransactionById(1L);

        assertEquals(1L, response.getTransactionId());
    }

    @Test
    void testGetTransactionById_notFound() {
        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getTransactionById(1L));
    }

    // ---------- USER TRANSACTIONS ----------

    @Test
    void testGetUserTransactions_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(tx));

        assertEquals(1, transactionService.getUserTransactions(1L).size());
    }

    @Test
    void testGetUserTransactions_userNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getUserTransactions(1L));
    }

    @Test
    void testGetUserTransactions_emptyList() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of());

        assertTrue(transactionService.getUserTransactions(1L).isEmpty());
    }

    // ---------- BRANCH TRANSACTIONS ----------

    @Test
    void testGetBranchTransactions_success() {
        when(branchRepository.existsById(1L)).thenReturn(true);

        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(tx));

        assertEquals(1, transactionService.getBranchTransactions(1L).size());
    }

    @Test
    void testGetBranchTransactions_branchNotFound() {
        when(branchRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getBranchTransactions(1L));
    }

    @Test
    void testGetBranchTransactions_emptyList() {
        when(branchRepository.existsById(1L)).thenReturn(true);
        when(transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of());

        assertTrue(transactionService.getBranchTransactions(1L).isEmpty());
    }

    // ---------- MAPPING TEST ----------

    @Test
    void testMapping_userAndBranchNull() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);
        tx.setUser(null);
        tx.setBranch(null);

        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(tx));

        TransactionResponse response = transactionService.getTransactionById(1L);

        assertNull(response.getUserId());
        assertNull(response.getBranchId());
    }

    @Test
    void testMapping_fullData() {
        User user = new User();
        user.setUserId(10L);
        user.setName("Cathy");

        VendorBranch branch = new VendorBranch();
        branch.setBranchId(20L);

        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);
        tx.setUser(user);
        tx.setBranch(branch);
        tx.setAmount(new BigDecimal("500"));
        tx.setQuantity(new BigDecimal("2"));
        tx.setCreatedAt(LocalDateTime.now());

        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(tx));

        TransactionResponse res = transactionService.getTransactionById(1L);

        assertEquals(10L, res.getUserId());
        assertEquals(20L, res.getBranchId());
    }
}

