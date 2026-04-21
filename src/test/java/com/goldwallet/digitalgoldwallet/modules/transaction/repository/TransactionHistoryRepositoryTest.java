package com.goldwallet.digitalgoldwallet.modules.transaction.repository;
import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorRepository;
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

    // 🔧 Helper methods
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

    //  Create BUY Transaction
    @Test
    void testCreateBuyTransaction() {
        User user = createUser("txn1@gmail.com");
        VendorBranch branch = createBranch("Vendor1");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("10"))
                        .amount(new BigDecimal("57000"))
                        .build()
        );

        assertNotNull(txn.getTransactionId());
    }

    //  Create SELL Transaction
    @Test
    void testCreateSellTransaction() {
        User user = createUser("txn2@gmail.com");
        VendorBranch branch = createBranch("Vendor2");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.SELL)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("5"))
                        .amount(new BigDecimal("28500"))
                        .build()
        );

        assertEquals(TransactionHistory.TransactionType.SELL, txn.getTransactionType());
    }

    //  Find Transaction
    @Test
    void testFindTransaction() {
        User user = createUser("txn3@gmail.com");
        VendorBranch branch = createBranch("Vendor3");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("2"))
                        .amount(new BigDecimal("11400"))
                        .build()
        );

        Optional<TransactionHistory> found = transactionRepository.findById(txn.getTransactionId());

        assertTrue(found.isPresent());
    }

    //  Update Transaction Status
    @Test
    void testUpdateTransactionStatus() {
        User user = createUser("txn4@gmail.com");
        VendorBranch branch = createBranch("Vendor4");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .transactionStatus(TransactionHistory.TransactionStatus.FAILED)
                        .quantity(new BigDecimal("3"))
                        .amount(new BigDecimal("17100"))
                        .build()
        );

        txn.setTransactionStatus(TransactionHistory.TransactionStatus.SUCCESS);
        TransactionHistory updated = transactionRepository.save(txn);

        assertEquals(TransactionHistory.TransactionStatus.SUCCESS, updated.getTransactionStatus());
    }

    //  Delete Transaction
    @Test
    void testDeleteTransaction() {
        User user = createUser("txn5@gmail.com");
        VendorBranch branch = createBranch("Vendor5");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.SELL)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("5700"))
                        .build()
        );

        transactionRepository.delete(txn);

        assertFalse(transactionRepository.findById(txn.getTransactionId()).isPresent());
    }

    //  Convert to Physical Transaction
    @Test
    void testConvertToPhysical() {
        User user = createUser("txn6@gmail.com");
        VendorBranch branch = createBranch("Vendor6");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.CONVERT_TO_PHYSICAL)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("8"))
                        .amount(new BigDecimal("45600"))
                        .build()
        );

        assertEquals(TransactionHistory.TransactionType.CONVERT_TO_PHYSICAL, txn.getTransactionType());
    }

    // User Mapping Check
    @Test
    void testUserMapping() {
        User user = createUser("txn7@gmail.com");
        VendorBranch branch = createBranch("Vendor7");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.BUY)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("4"))
                        .amount(new BigDecimal("22800"))
                        .build()
        );

        Optional<TransactionHistory> found = transactionRepository.findById(txn.getTransactionId());

        assertEquals("User txn7@gmail.com", found.get().getUser().getName());
    }

    //  Branch Mapping Check
    @Test
    void testBranchMapping() {
        User user = createUser("txn8@gmail.com");
        VendorBranch branch = createBranch("Vendor8");

        TransactionHistory txn = transactionRepository.save(
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(TransactionHistory.TransactionType.SELL)
                        .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                        .quantity(new BigDecimal("6"))
                        .amount(new BigDecimal("34200"))
                        .build()
        );

        Optional<TransactionHistory> found = transactionRepository.findById(txn.getTransactionId());

        assertEquals("Vendor8", found.get().getBranch().getVendor().getVendorName());
    }

    //  Multiple Transactions
    @Test
    void testMultipleTransactions() {
        User user = createUser("txn9@gmail.com");
        VendorBranch branch = createBranch("Vendor9");

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.BUY)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("2"))
                .amount(new BigDecimal("11400"))
                .build());

        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.SELL)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("1"))
                .amount(new BigDecimal("5700"))
                .build());

        List<TransactionHistory> list = transactionRepository.findAll();

        assertEquals(2, list.size());
    }

    //  Invalid Case (Null Amount)
    @Test
    void testNullAmount() {
        User user = createUser("txn10@gmail.com");
        VendorBranch branch = createBranch("Vendor10");

        Exception exception = assertThrows(Exception.class, () -> {
            transactionRepository.save(TransactionHistory.builder()
                    .user(user)
                    .branch(branch)
                    .transactionType(TransactionHistory.TransactionType.BUY)
                    .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                    .quantity(new BigDecimal("5"))
                    .amount(null)
                    .build());
        });

        assertNotNull(exception);
    }
}
