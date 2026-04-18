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

    // ---------- TC-01: GET BY ID - success ----------

    @Test
    void testGetTransactionById_success() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(tx));

        TransactionResponse response = transactionService.getTransactionById(1L);

        assertEquals(1L, response.getTransactionId());
    }

    // ---------- TC-02: GET BY ID - not found ----------

    @Test
    void testGetTransactionById_notFound() {
        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getTransactionById(1L));
    }

    // ---------- TC-03: USER TRANSACTIONS - success ----------

    @Test
    void testGetUserTransactions_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(tx));

        assertEquals(1, transactionService.getUserTransactions(1L).size());
    }

    // ---------- TC-04: USER TRANSACTIONS - user not found ----------

    @Test
    void testGetUserTransactions_userNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getUserTransactions(1L));
    }

    // ---------- TC-05: USER TRANSACTIONS - empty list ----------

    @Test
    void testGetUserTransactions_emptyList() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of());

        assertTrue(transactionService.getUserTransactions(1L).isEmpty());
    }

    // ---------- TC-06: BRANCH TRANSACTIONS - success ----------

    @Test
    void testGetBranchTransactions_success() {
        when(branchRepository.existsById(1L)).thenReturn(true);

        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(1L);

        when(transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(tx));

        assertEquals(1, transactionService.getBranchTransactions(1L).size());
    }

    // ---------- TC-07: BRANCH TRANSACTIONS - branch not found ----------

    @Test
    void testGetBranchTransactions_branchNotFound() {
        when(branchRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getBranchTransactions(1L));
    }

    // ---------- TC-08: BRANCH TRANSACTIONS - empty list ----------

    @Test
    void testGetBranchTransactions_emptyList() {
        when(branchRepository.existsById(1L)).thenReturn(true);
        when(transactionHistoryRepository.findByBranchBranchIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of());

        assertTrue(transactionService.getBranchTransactions(1L).isEmpty());
    }

    // ---------- TC-09: MAPPING - user and branch null ----------

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

    // ---------- TC-10: MAPPING - full data ----------

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

    // ---------- TC-11: MAPPING - transaction type and status ----------

    @Test
    void testMapping_transactionTypeAndStatus() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(5L);
        tx.setTransactionType(TransactionHistory.TransactionType.BUY);
        tx.setTransactionStatus(TransactionHistory.TransactionStatus.SUCCESS);

        when(transactionHistoryRepository.findById(5L)).thenReturn(Optional.of(tx));

        TransactionResponse res = transactionService.getTransactionById(5L);

        assertEquals(TransactionHistory.TransactionType.BUY, res.getTransactionType());
        assertEquals(TransactionHistory.TransactionStatus.SUCCESS, res.getTransactionStatus());
    }

    // ---------- TC-12: MAPPING - SELL type with FAILED status ----------

    @Test
    void testMapping_sellTypeFailedStatus() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(6L);
        tx.setTransactionType(TransactionHistory.TransactionType.SELL);
        tx.setTransactionStatus(TransactionHistory.TransactionStatus.FAILED);

        when(transactionHistoryRepository.findById(6L)).thenReturn(Optional.of(tx));

        TransactionResponse res = transactionService.getTransactionById(6L);

        assertEquals(TransactionHistory.TransactionType.SELL, res.getTransactionType());
        assertEquals(TransactionHistory.TransactionStatus.FAILED, res.getTransactionStatus());
    }

    // ---------- TC-13: MAPPING - amount and quantity values ----------

    @Test
    void testMapping_amountAndQuantityValues() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(7L);
        tx.setAmount(new BigDecimal("1250.50"));
        tx.setQuantity(new BigDecimal("3.75"));

        when(transactionHistoryRepository.findById(7L)).thenReturn(Optional.of(tx));

        TransactionResponse res = transactionService.getTransactionById(7L);

        assertEquals(0, new BigDecimal("1250.50").compareTo(res.getAmount()));
        assertEquals(0, new BigDecimal("3.75").compareTo(res.getQuantity()));
    }

    // ---------- TC-14: USER TRANSACTIONS - multiple transactions returned ----------

    @Test
    void testGetUserTransactions_multipleTransactions() {
        when(userRepository.existsById(2L)).thenReturn(true);

        TransactionHistory tx1 = new TransactionHistory();
        tx1.setTransactionId(10L);

        TransactionHistory tx2 = new TransactionHistory();
        tx2.setTransactionId(11L);

        TransactionHistory tx3 = new TransactionHistory();
        tx3.setTransactionId(12L);

        when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(2L))
                .thenReturn(List.of(tx1, tx2, tx3));

        List<TransactionResponse> results = transactionService.getUserTransactions(2L);

        assertEquals(3, results.size());
        assertEquals(10L, results.get(0).getTransactionId());
        assertEquals(11L, results.get(1).getTransactionId());
        assertEquals(12L, results.get(2).getTransactionId());
    }

    // ---------- TC-15: MAPPING - createdAt timestamp preserved ----------

    @Test
    void testMapping_createdAtTimestampPreserved() {
        LocalDateTime fixedTime = LocalDateTime.of(2025, 6, 15, 10, 30, 0);

        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionId(8L);
        tx.setCreatedAt(fixedTime);

        when(transactionHistoryRepository.findById(8L)).thenReturn(Optional.of(tx));

        TransactionResponse res = transactionService.getTransactionById(8L);

        assertEquals(fixedTime, res.getCreatedAt());
    }
}
