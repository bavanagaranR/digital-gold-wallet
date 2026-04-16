package com.goldwallet.digitalgoldwallet.modules.gold;

import com.goldwallet.digitalgoldwallet.modules.gold.physical.repository.PhysicalGoldTransactionRepository;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.entity.PhysicalGoldTransaction;

import com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity.VirtualGoldHolding;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.repository.VirtualGoldHoldingRepository;
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

class GoldRepositoryTest {

    @Autowired
    private PhysicalGoldTransactionRepository physicalRepo;

    @Autowired
    private VirtualGoldHoldingRepository virtualRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorBranchRepository branchRepository;

    // 🔧 Helper Methods
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
                .street("Branch Area")
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

    //  Create Physical Transaction
    @Test
    void testCreatePhysicalTransaction() {
        User user = createUser("phy1@gmail.com");
        VendorBranch branch = createBranch("Vendor1");

        PhysicalGoldTransaction tx = physicalRepo.save(
                PhysicalGoldTransaction.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("10"))
                        .deliveryAddress(user.getAddress())
                        .build()
        );

        assertNotNull(tx.getTransactionId());
    }

    //  Find Physical Transaction
    @Test
    void testFindPhysicalTransaction() {
        User user = createUser("phy2@gmail.com");
        VendorBranch branch = createBranch("Vendor2");

        PhysicalGoldTransaction tx = physicalRepo.save(
                PhysicalGoldTransaction.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("5"))
                        .build()
        );

        Optional<PhysicalGoldTransaction> found = physicalRepo.findById(tx.getTransactionId());

        assertTrue(found.isPresent());
    }

    //  Delete Physical Transaction
    @Test
    void testDeletePhysicalTransaction() {
        User user = createUser("phy3@gmail.com");
        VendorBranch branch = createBranch("Vendor3");

        PhysicalGoldTransaction tx = physicalRepo.save(
                PhysicalGoldTransaction.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("3"))
                        .build()
        );

        physicalRepo.delete(tx);

        assertFalse(physicalRepo.findById(tx.getTransactionId()).isPresent());
    }

    // Create Virtual Holding
    @Test
    void testCreateVirtualHolding() {
        User user = createUser("virt1@gmail.com");
        VendorBranch branch = createBranch("Vendor4");

        VirtualGoldHolding holding = virtualRepo.save(
                VirtualGoldHolding.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("20"))
                        .build()
        );

        assertNotNull(holding.getHoldingId());
    }

    //  Find Virtual Holding
    @Test
    void testFindVirtualHolding() {
        User user = createUser("virt2@gmail.com");
        VendorBranch branch = createBranch("Vendor5");

        VirtualGoldHolding holding = virtualRepo.save(
                VirtualGoldHolding.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("15"))
                        .build()
        );

        Optional<VirtualGoldHolding> found = virtualRepo.findById(holding.getHoldingId());

        assertTrue(found.isPresent());
    }

    //  Update Virtual Holding Quantity
    @Test
    void testUpdateVirtualHolding() {
        User user = createUser("virt3@gmail.com");
        VendorBranch branch = createBranch("Vendor6");

        VirtualGoldHolding holding = virtualRepo.save(
                VirtualGoldHolding.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("10"))
                        .build()
        );

        holding.setQuantity(new BigDecimal("50"));
        VirtualGoldHolding updated = virtualRepo.save(holding);

        assertEquals(new BigDecimal("50"), updated.getQuantity());
    }

    //  Delete Virtual Holding
    @Test
    void testDeleteVirtualHolding() {
        User user = createUser("virt4@gmail.com");
        VendorBranch branch = createBranch("Vendor7");

        VirtualGoldHolding holding = virtualRepo.save(
                VirtualGoldHolding.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("25"))
                        .build()
        );

        virtualRepo.delete(holding);

        assertFalse(virtualRepo.findById(holding.getHoldingId()).isPresent());
    }

    //  Mapping Check (User ↔ Virtual Gold)
    @Test
    void testUserVirtualGoldMapping() {
        User user = createUser("virt5@gmail.com");
        VendorBranch branch = createBranch("Vendor8");

        VirtualGoldHolding holding = virtualRepo.save(
                VirtualGoldHolding.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("12"))
                        .build()
        );

        Optional<VirtualGoldHolding> found = virtualRepo.findById(holding.getHoldingId());

        assertEquals("User virt5@gmail.com", found.get().getUser().getName());
    }

    //  Multiple Holdings
    @Test
    void testMultipleHoldings() {
        User user = createUser("virt6@gmail.com");
        VendorBranch branch = createBranch("Vendor9");

        virtualRepo.save(VirtualGoldHolding.builder()
                .user(user)
                .branch(branch)
                .quantity(new BigDecimal("5"))
                .build());

        virtualRepo.save(VirtualGoldHolding.builder()
                .user(user)
                .branch(branch)
                .quantity(new BigDecimal("10"))
                .build());

        List<VirtualGoldHolding> list = virtualRepo.findAll();

        assertEquals(2, list.size());
    }

    //  Physical Transaction with Address Mapping
    @Test
    void testPhysicalTransactionAddress() {
        User user = createUser("phy4@gmail.com");
        VendorBranch branch = createBranch("Vendor10");

        PhysicalGoldTransaction tx = physicalRepo.save(
                PhysicalGoldTransaction.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("7"))
                        .deliveryAddress(user.getAddress())
                        .build()
        );

        Optional<PhysicalGoldTransaction> found = physicalRepo.findById(tx.getTransactionId());

        assertNotNull(found.get().getDeliveryAddress());
        assertEquals("Chennai", found.get().getDeliveryAddress().getCity());
    }
}

