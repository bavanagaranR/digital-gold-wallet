package com.goldwallet.digitalgoldwallet.modules.gold;

import com.goldwallet.digitalgoldwallet.modules.gold.physical.entity.PhysicalGoldTransaction;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.repository.PhysicalGoldTransactionRepository;
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

    @Autowired
    private EntityManager entityManager;

    // ---------- Helper Methods ----------
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

    // ---------- EXISTING TESTS ----------

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

        assertEquals(0, updated.getQuantity().compareTo(new BigDecimal("50")));
    }

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

    // ---------- NEW CUSTOM QUERY TESTS ----------

    @Test
    void testTotalPhysicalGoldByUser() {
        User user = createUser("phy_total@gmail.com");
        VendorBranch branch = createBranch("VendorP");

        physicalRepo.save(PhysicalGoldTransaction.builder()
                .user(user)
                .branch(branch)
                .quantity(new BigDecimal("10"))
                .build());

        BigDecimal total = physicalRepo.getTotalPhysicalGoldByUser(user.getUserId());
        assertEquals(0, total.compareTo(new BigDecimal("10")));
    }

    @Test
    void testDeletePhysicalTransactionCustom() {
        User user = createUser("phy_del@gmail.com");
        VendorBranch branch = createBranch("VendorPD");

        PhysicalGoldTransaction tx = physicalRepo.save(
                PhysicalGoldTransaction.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("5"))
                        .build()
        );

        physicalRepo.deleteTransactionById(tx.getTransactionId());

        entityManager.flush();
        entityManager.clear();

        assertFalse(physicalRepo.findById(tx.getTransactionId()).isPresent());
    }

    @Test
    void testCustomUpdateHolding() {
        User user = createUser("virt_update@gmail.com");
        VendorBranch branch = createBranch("VendorVU");

        VirtualGoldHolding holding = virtualRepo.save(
                VirtualGoldHolding.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("10"))
                        .build()
        );

        virtualRepo.updateHoldingQuantity(holding.getHoldingId(), new BigDecimal("60"));

        entityManager.flush();
        entityManager.clear();

        VirtualGoldHolding updated = virtualRepo.findById(holding.getHoldingId()).get();
        assertEquals(0, updated.getQuantity().compareTo(new BigDecimal("60")));
    }

    @Test
    void testCustomDeleteHolding() {
        User user = createUser("virt_del@gmail.com");
        VendorBranch branch = createBranch("VendorVD");

        VirtualGoldHolding holding = virtualRepo.save(
                VirtualGoldHolding.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(new BigDecimal("20"))
                        .build()
        );

        virtualRepo.deleteHoldingById(holding.getHoldingId());

        entityManager.flush();
        entityManager.clear();

        assertFalse(virtualRepo.findById(holding.getHoldingId()).isPresent());
    }

    @Test
    void testTotalVirtualGoldByUser() {
        User user = createUser("virt_total@gmail.com");
        VendorBranch branch = createBranch("VendorVT");

        virtualRepo.save(VirtualGoldHolding.builder()
                .user(user)
                .branch(branch)
                .quantity(new BigDecimal("10"))
                .build());

        virtualRepo.save(VirtualGoldHolding.builder()
                .user(user)
                .branch(branch)
                .quantity(new BigDecimal("20"))
                .build());

        BigDecimal total = virtualRepo.getTotalGoldByUser(user.getUserId());
        assertEquals(0, total.compareTo(new BigDecimal("30")));
    }
}
