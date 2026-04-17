package com.goldwallet.digitalgoldwallet.modules.transaction;

import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import com.goldwallet.digitalgoldwallet.modules.transaction.repository.TransactionHistoryRepository;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorRepository;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TransactionHistoryRepositoryTest {

    @Autowired
    private TransactionHistoryRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorBranchRepository branchRepository;

    @Autowired
    private EntityManager entityManager;

    // ---------- Helpers ----------
    private User createUser(String email) {
        Address address = addressRepository.save(Address.builder()
                .street("Street")
                .city("Chennai")
                .state("TN")
                .postalCode("600001")
                .country("India")
                .build());

        return userRepository.save(User.builder()
                .name("User " + email)
                .email(email)
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());
    }

    private VendorBranch createBranch(String name) {
        Vendor vendor = vendorRepository.save(Vendor.builder()
                .vendorName(name)
                .contactEmail(name + "@gmail.com")
                .build());

        Address address = addressRepository.save(Address.builder()
                .street("Area")
                .city("Chennai")
                .state("TN")
                .postalCode("600002")
                .country("India")
                .build());

        return branchRepository.save(VendorBranch.builder()
                .vendor(vendor)
                .address(address)
                .quantity(new BigDecimal("100"))
                .build());
    }

    // ---------- 1 CREATE ----------
    @Test
    void testCreateTransaction() {
        User user = createUser("t1@gmail.com");
        VendorBranch branch = createBranch("V1");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("5"))
                        .amount(new BigDecimal("25000"))
                        .build()
        );

        assertNotNull(txn.getTransactionId());
    }

    // ---------- 2 READ ----------
    @Test
    void testFindById() {
        User user = createUser("t2@gmail.com");
        VendorBranch branch = createBranch("V2");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.SELL)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("2"))
                        .amount(new BigDecimal("10000"))
                        .build()
        );

        assertTrue(transactionRepository.findById(txn.getTransactionId()).isPresent());
    }

    // ---------- 3 UPDATE ----------
    @Test
    void testUpdateStatus() {
        User user = createUser("t3@gmail.com");
        VendorBranch branch = createBranch("V3");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .transactionStatus(TransactionHistory.TransactionStatus.FAILED)
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("5000"))
                        .build()
        );

        txn.setTransactionStatus(TransactionHistory.TransactionStatus.SUCCESS);
        TransactionHistory updated = transactionRepository.save(txn);

        assertEquals(TransactionHistory.TransactionStatus.SUCCESS, updated.getTransactionStatus());
    }

    // ---------- 4 DELETE ----------
    @Test
    void testDeleteTransaction() {
        User user = createUser("t4@gmail.com");
        VendorBranch branch = createBranch("V4");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.SELL)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("5000"))
                        .build()
        );

        transactionRepository.delete(txn);

        assertFalse(transactionRepository.findById(txn.getTransactionId()).isPresent());
    }

    // ---------- 5 FIND BY USER ----------
    @Test
    void testFindByUser() {
        User user = createUser("t5@gmail.com");
        VendorBranch branch = createBranch("V5");

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.BUY)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("2"))
                .amount(new BigDecimal("1000"))
                .build());

        List<TransactionHistory> list =
                transactionRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId());

        assertFalse(list.isEmpty());
    }

    // ---------- 6 FIND BY BRANCH ----------
    @Test
    void testFindByBranch() {
        User user = createUser("t6@gmail.com");
        VendorBranch branch = createBranch("V6");

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.BUY)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("2"))
                .amount(new BigDecimal("1000"))
                .build());

        List<TransactionHistory> list =
                transactionRepository.findByBranchBranchIdOrderByCreatedAtDesc(branch.getBranchId());

        assertFalse(list.isEmpty());
    }

    // ---------- 7 FILTER STATUS ----------
    @Test
    void testFindByStatus() {
        User user = createUser("t7@gmail.com");
        VendorBranch branch = createBranch("V7");

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.BUY)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("1"))
                .amount(new BigDecimal("1000"))
                .build());

        assertFalse(transactionRepository
                .findByStatus(TransactionHistory.TransactionStatus.SUCCESS).isEmpty());
    }

    // ---------- 8 FILTER TYPE ----------
    @Test
    void testFindByType() {
        User user = createUser("t8@gmail.com");
        VendorBranch branch = createBranch("V8");

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.SELL)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("1"))
                .amount(new BigDecimal("1000"))
                .build());

        assertFalse(transactionRepository
                .findByTransactionType(TransactionHistory.TransactionType.SELL).isEmpty());
    }

    // ---------- 9 AMOUNT FILTER ----------
    @Test
    void testAmountGreaterThan() {
        User user = createUser("t9@gmail.com");
        VendorBranch branch = createBranch("V9");

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.BUY)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("1"))
                .amount(new BigDecimal("5000"))
                .build());

        assertEquals(1,
                transactionRepository.findTransactionsGreaterThan(new BigDecimal("1000")).size());
    }

    // ---------- 10 UPDATE CUSTOM ----------
    @Test
    void testCustomUpdate() {
        User user = createUser("t10@gmail.com");
        VendorBranch branch = createBranch("V10");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionStatus(TransactionHistory.TransactionStatus.FAILED)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("1000"))
                        .build()
        );

        transactionRepository.updateTransactionStatus(txn.getTransactionId(),
                TransactionHistory.TransactionStatus.SUCCESS);

        entityManager.clear();

        assertEquals(TransactionHistory.TransactionStatus.SUCCESS,
                transactionRepository.findById(txn.getTransactionId()).get().getTransactionStatus());
    }

    // ---------- 11 DELETE CUSTOM ----------
    @Test
    void testCustomDelete() {
        User user = createUser("t11@gmail.com");
        VendorBranch branch = createBranch("V11");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("1000"))
                        .build()
        );

        transactionRepository.deleteTransactionByIdCustom(txn.getTransactionId());

        entityManager.clear();

        assertFalse(transactionRepository.findById(txn.getTransactionId()).isPresent());
    }

    // ---------- 12 AGGREGATION ----------
    @Test
    void testTotalAmount() {
        User user = createUser("t12@gmail.com");
        VendorBranch branch = createBranch("V12");

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.BUY)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("1"))
                .amount(new BigDecimal("1000"))
                .build());

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.SELL)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("1"))
                .amount(new BigDecimal("2000"))
                .build());

        BigDecimal total =
                transactionRepository.getTotalTransactionAmountByUser(user.getUserId());

        assertEquals(0, total.compareTo(new BigDecimal("3000")));
    }

    // ---------- 13 USER MAPPING ----------
    @Test
    void testUserMapping() {
        User user = createUser("t13@gmail.com");
        VendorBranch branch = createBranch("V13");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("1000"))
                        .build()
        );

        assertEquals("User t13@gmail.com",
                transactionRepository.findById(txn.getTransactionId()).get().getUser().getName());
    }

    // ---------- 14 BRANCH MAPPING ----------
    @Test
    void testBranchMapping() {
        User user = createUser("t14@gmail.com");
        VendorBranch branch = createBranch("Vendor14");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.SELL)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("1000"))
                        .build()
        );

        assertEquals("Vendor14",
                transactionRepository.findById(txn.getTransactionId()).get()
                        .getBranch().getVendor().getVendorName());
    }

    // ---------- 15 NULL VALIDATION ----------
    @Test
    void testNullAmount() {
        User user = createUser("t15@gmail.com");
        VendorBranch branch = createBranch("V15");

        assertThrows(Exception.class, () -> {
            transactionRepository.save(TransactionHistory.builder()
                    .user(user)
                    .branch(branch)
                    .transactionType(TransactionHistory.TransactionType.BUY)
                    .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                    .quantity(new BigDecimal("1"))
                    .amount(null)
                    .build());
        });
    }
}

