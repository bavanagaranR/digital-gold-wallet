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

// JPA slice test — loads only the persistence layer (repositories, entities) without the full Spring context
@DataJpaTest
@ActiveProfiles("test") // Uses application-test.properties (in-memory H2 database)
class TransactionHistoryRepositoryTest {

    // Repository under test — performs CRUD operations on transaction_history table
    @Autowired
    private TransactionHistoryRepository transactionRepository;

    // Used to create and persist test user records
    @Autowired
    private UserRepository userRepository;

    // Used to create and persist test address records linked to users and branches
    @Autowired
    private AddressRepository addressRepository;

    // Used to create and persist test vendor records
    @Autowired
    private VendorRepository vendorRepository;

    // Used to create and persist test vendor branch records linked to transactions
    @Autowired
    private VendorBranchRepository branchRepository;

    // ─────────────────────────── HELPER METHODS ───────────────────────────

    // Creates and persists a User with a default address — used as test data setup
    private User createUser(String email) {
        // Save a minimal address record required by the User entity
        Address address = addressRepository.save(Address.builder()
                .street("Street")
                .city("Chennai")
                .state("TN")
                .postalCode("600001")
                .country("India")
                .build());

        // Save and return a User linked to the created address
        return userRepository.save(User.builder()
                .name("User " + email)
                .email(email)
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());
    }

    // Creates and persists a VendorBranch with its own vendor and address — used as test data setup
    private VendorBranch createBranch(String name) {
        // Save a Vendor entity that owns the branch
        Vendor vendor = vendorRepository.save(Vendor.builder()
                .vendorName(name)
                .contactEmail(name + "@gmail.com")
                .contactPhone("9876543210")
                .build());

        // Save a separate address for the branch location
        Address address = addressRepository.save(Address.builder()
                .street("Area")
                .city("Chennai")
                .state("TN")
                .postalCode("600002")
                .country("India")
                .build());

        // Save and return the branch linked to the vendor and address
        return branchRepository.save(VendorBranch.builder()
                .vendor(vendor)
                .address(address)
                .quantity(new BigDecimal("100"))
                .build());
    }

    // ─────────────────────────── POSITIVE TESTS ───────────────────────────

    // TC-01: Verifies that a BUY transaction is successfully saved and assigned a primary key
    @Test
    void testCreateBuyTransaction() {
        User user = createUser("txn1@gmail.com");
        VendorBranch branch = createBranch("Vendor1");

        // Save a BUY transaction with 10 units of gold worth ₹57,000
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

        // Transaction ID must be auto-generated after saving
        assertNotNull(txn.getTransactionId());
    }

    // TC-02: Verifies that a SELL transaction is saved with the correct transaction type
    @Test
    void testCreateSellTransaction() {
        User user = createUser("txn2@gmail.com");
        VendorBranch branch = createBranch("Vendor2");

        // Save a SELL transaction with 5 units of gold worth ₹28,500
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

        // Verify the transaction type is persisted as SELL
        assertEquals(TransactionHistory.TransactionType.SELL, txn.getTransactionType());
    }

    // TC-03: Verifies that a saved transaction can be retrieved by its ID
    @Test
    void testFindTransaction() {
        User user = createUser("txn3@gmail.com");
        VendorBranch branch = createBranch("Vendor3");

        // Save a BUY transaction to test findById
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

        // Fetch the transaction using its generated ID
        Optional<TransactionHistory> found = transactionRepository.findById(txn.getTransactionId());

        // Transaction must be found in the database
        assertTrue(found.isPresent());
    }

    // TC-04: Verifies that a transaction's status can be updated from FAILED to SUCCESS
    @Test
    void testUpdateTransactionStatus() {
        User user = createUser("txn4@gmail.com");
        VendorBranch branch = createBranch("Vendor4");

        // Initially save a transaction with FAILED status
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

        // Update the status to SUCCESS and re-save
        txn.setTransactionStatus(TransactionHistory.TransactionStatus.SUCCESS);
        TransactionHistory updated = transactionRepository.save(txn);

        // Confirm the updated status is persisted correctly
        assertEquals(TransactionHistory.TransactionStatus.SUCCESS, updated.getTransactionStatus());
    }

    // TC-05: Verifies that a transaction record is permanently removed from the database after deletion
    @Test
    void testDeleteTransaction() {
        User user = createUser("txn5@gmail.com");
        VendorBranch branch = createBranch("Vendor5");

        // Save a transaction to be deleted
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

        // Delete the transaction
        transactionRepository.delete(txn);

        // Confirm the record no longer exists in the database
        assertFalse(transactionRepository.findById(txn.getTransactionId()).isPresent());
    }

    // TC-06: Verifies that a CONVERT_TO_PHYSICAL transaction type is correctly persisted
    @Test
    void testConvertToPhysical() {
        User user = createUser("txn6@gmail.com");
        VendorBranch branch = createBranch("Vendor6");

        // Save a transaction representing a physical gold conversion request
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

        // Confirm the transaction type is stored as CONVERT_TO_PHYSICAL
        assertEquals(TransactionHistory.TransactionType.CONVERT_TO_PHYSICAL, txn.getTransactionType());
    }

    // TC-07: Verifies that the user association (ManyToOne) is correctly stored and retrievable
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

        // Reload the transaction from the database and verify the linked user's name
        Optional<TransactionHistory> found = transactionRepository.findById(txn.getTransactionId());

        assertEquals("User txn7@gmail.com", found.get().getUser().getName());
    }

    // TC-08: Verifies that the vendor branch association (ManyToOne) is correctly stored and retrievable
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

        // Reload the transaction and verify the associated branch's vendor name
        Optional<TransactionHistory> found = transactionRepository.findById(txn.getTransactionId());

        assertEquals("Vendor8", found.get().getBranch().getVendor().getVendorName());
    }

    // TC-09: Verifies that multiple transactions can be saved and all are retrievable via findAll()
    @Test
    void testMultipleTransactions() {
        User user = createUser("txn9@gmail.com");
        VendorBranch branch = createBranch("Vendor9");

        // Save first transaction — BUY
        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.BUY)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("2"))
                .amount(new BigDecimal("11400"))
                .build());

        // Save second transaction — SELL
        transactionRepository.save(TransactionHistory.builder()
                .user(user)
                .branch(branch)
                .transactionType(TransactionHistory.TransactionType.SELL)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(new BigDecimal("1"))
                .amount(new BigDecimal("5700"))
                .build());

        List<TransactionHistory> list = transactionRepository.findAll();

        // Both records must be present in the database
        assertEquals(2, list.size());
    }

    // ─────────────────────────── NEGATIVE TEST ───────────────────────────

    // TC-10: Verifies that saving a transaction with a null amount throws a database constraint violation
    @Test
    void testNullAmount() {
        User user = createUser("txn10@gmail.com");
        VendorBranch branch = createBranch("Vendor10");

        // Attempt to save a transaction without a required amount value — should fail at DB level
        Exception exception = assertThrows(Exception.class, () -> {
            transactionRepository.save(TransactionHistory.builder()
                    .user(user)
                    .branch(branch)
                    .transactionType(TransactionHistory.TransactionType.BUY)
                    .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                    .quantity(new BigDecimal("5"))
                    .amount(null) // NULL violates the NOT NULL constraint on the amount column
                    .build());
        });

        // Exception must be thrown — confirms the DB constraint is enforced
        assertNotNull(exception);
    }
}