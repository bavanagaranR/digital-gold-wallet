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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    // ================= POSITIVE TESTS (10) =================

    // 1 - Get transaction by ID returns correct transaction
    @Test
    void testGetTransactionById_success_returnsTransaction() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(tx));

        TransactionResponse response = transactionService.getTransactionById(1L);

        assertEquals(1L, response.getTransactionId());
    }

    // 2 - Get transaction by ID verifies repository is called once
    @Test
    void testGetTransactionById_success_repositoryCalledOnce() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(tx));

        transactionService.getTransactionById(1L);

        verify(transactionHistoryRepository, times(1)).findById(1L);
    }

    // 3 - Get transaction by ID maps user and branch correctly
    @Test
    void testGetTransactionById_success_mapsUserAndBranch() {
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

    // 4 - Get transaction by ID maps amount field
    @Test
    void testGetTransactionById_success_amountMapped() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);
        tx.setAmount(new BigDecimal("1000"));

        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(tx));

        TransactionResponse response = transactionService.getTransactionById(1L);

        assertEquals(new BigDecimal("1000"), response.getAmount());
    }

    // 5 - Get user transactions returns paginated results
    @Test
    void testGetUserTransactions_success_returnsSingleTransaction() {
        when(userRepository.existsById(1L)).thenReturn(true);

        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(tx)));

        assertEquals(1, transactionService.getUserTransactions(1L, Pageable.unpaged()).getContent().size());
    }

    // 6 - Get user transactions returns empty page when no transactions exist
    @Test
    void testGetUserTransactions_success_emptyList() {
        when(userRepository.existsById(1L)).thenReturn(true);

        when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        assertTrue(transactionService.getUserTransactions(1L, Pageable.unpaged()).isEmpty());
    }

    // 7 - Get user transactions returns multiple transactions
    @Test
    void testGetUserTransactions_success_multipleTransactions() {
        when(userRepository.existsById(1L)).thenReturn(true);

        TransactionHistory tx1 = new TransactionHistory();
        tx1.setTransactionId(1L);

        TransactionHistory tx2 = new TransactionHistory();
        tx2.setTransactionId(2L);

        when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(tx1, tx2)));

        Page<TransactionResponse> response =
                transactionService.getUserTransactions(1L, Pageable.unpaged());

        assertEquals(2, response.getContent().size());
    }

    // 8 - Get branch transactions returns paginated results
    @Test
    void testGetBranchTransactions_success_returnsSingleTransaction() {
        when(branchRepository.existsById(1L)).thenReturn(true);

        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(tx)));

        assertEquals(1, transactionService.getBranchTransactions(1L, Pageable.unpaged()).getContent().size());
    }

    // 9 - Get branch transactions returns empty page
    @Test
    void testGetBranchTransactions_success_emptyList() {
        when(branchRepository.existsById(1L)).thenReturn(true);

        when(transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        assertTrue(transactionService.getBranchTransactions(1L, Pageable.unpaged()).isEmpty());
    }

    // 10 - Get transaction handles null user and branch gracefully
    @Test
    void testGetTransactionById_success_nullUserAndBranchMapped() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);
        tx.setUser(null);
        tx.setBranch(null);

        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(tx));

        TransactionResponse response = transactionService.getTransactionById(1L);

        assertNull(response.getUserId());
        assertNull(response.getBranchId());
    }

    // ================= NEGATIVE TESTS (5) =================

    // 11 - Get transaction by ID throws when not found
    @Test
    void testGetTransactionById_notFound_throwsException() {
        when(transactionHistoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getTransactionById(99L));
    }

    // 12 - Get user transactions throws when user does not exist
    @Test
    void testGetUserTransactions_userNotFound_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getUserTransactions(1L, Pageable.unpaged()));
    }

    // 13 - Get branch transactions throws when branch does not exist
    @Test
    void testGetBranchTransactions_branchNotFound_throwsException() {
        when(branchRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getBranchTransactions(1L, Pageable.unpaged()));
    }

    // 14 - Get user transactions throws for non-existent user ID
    @Test
    void testGetUserTransactions_unknownUserId_throwsException() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getUserTransactions(999L, Pageable.unpaged()));
    }

    // 15 - Get branch transactions throws for non-existent branch ID
    @Test
    void testGetBranchTransactions_unknownBranchId_throwsException() {
        when(branchRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getBranchTransactions(999L, Pageable.unpaged()));
    }
}