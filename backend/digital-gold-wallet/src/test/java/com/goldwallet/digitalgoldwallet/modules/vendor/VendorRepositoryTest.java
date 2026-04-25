package com.goldwallet.digitalgoldwallet.modules.vendor;

import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class VendorRepositoryTest {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorBranchRepository vendorBranchRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EntityManager entityManager;

    // ---------- Helper Methods ----------
    private Address createAddress(String area) {
        return addressRepository.save(Address.builder()
                .street(area)
                .city("Chennai")
                .state("TN")
                .postalCode("600001")
                .country("India")
                .build());
    }

    private Vendor createVendor(String name, String email) {
        return vendorRepository.save(Vendor.builder()
                .vendorName(name)
                .description("Gold Vendor")
                .contactPersonName("Manager " + name)
                .contactEmail(email)
                .contactPhone("9876543210")
                .websiteUrl("https://www." + name.toLowerCase().replace(" ", "") + ".com")
                .totalGoldQuantity(BigDecimal.ZERO)
                .currentGoldPrice(new BigDecimal("5700.00"))
                .build());
    }

    // ---------- BASIC CRUD TESTS ----------

    @Test
    void testCreateVendor() {
        Vendor vendor = createVendor("Tanishq", "tanishq@gmail.com");
        assertNotNull(vendor.getVendorId());
    }

    @Test
    void testFindVendorById() {
        Vendor vendor = createVendor("Kalyan", "kalyan@gmail.com");

        Optional<Vendor> found = vendorRepository.findById(vendor.getVendorId());
        assertTrue(found.isPresent());
    }

    @Test
    void testUpdateVendor() {
        Vendor vendor = createVendor("Malabar", "malabar@gmail.com");

        vendor.setCurrentGoldPrice(new BigDecimal("6000"));
        Vendor updated = vendorRepository.save(vendor);

        assertEquals(new BigDecimal("6000"), updated.getCurrentGoldPrice());
    }

    @Test
    void testDeleteVendor() {
        Vendor vendor = createVendor("GRT", "grt@gmail.com");

        vendorRepository.delete(vendor);

        Optional<Vendor> found = vendorRepository.findById(vendor.getVendorId());
        assertFalse(found.isPresent());
    }

    // ---------- CUSTOM QUERY TESTS ----------

    @Test
    void testFindByVendorName() {
        createVendor("Tanishq", "t@gmail.com");

        Optional<Vendor> vendor = vendorRepository.findByVendorName("Tanishq");
        assertTrue(vendor.isPresent());
    }

    @Test
    void testFindByNameIgnoreCase() {
        createVendor("Kalyan", "k@gmail.com");

        Optional<Vendor> vendor = vendorRepository.findByNameIgnoreCase("kalyan");
        assertTrue(vendor.isPresent());
    }

    @Test
    void testFindVendorsWithGoldPriceGreaterThan() {
        createVendor("Vendor1", "v1@gmail.com");
        Vendor v2 = createVendor("Vendor2", "v2@gmail.com");

        v2.setCurrentGoldPrice(new BigDecimal("6500"));
        vendorRepository.save(v2);

        List<Vendor> result =
                vendorRepository.findVendorsWithGoldPriceGreaterThan(new BigDecimal("6000"));

        assertEquals(1, result.size());
    }
    @Test
    void testCustomUpdateGoldPrice() {

        Vendor vendor = createVendor("Malabar", "m@gmail.com");

        vendorRepository.updateGoldPrice(vendor.getVendorId(), new BigDecimal("6500"));

        entityManager.flush();   // push update
        entityManager.clear();   // clear cache

        Vendor updated = vendorRepository.findById(vendor.getVendorId()).get();

        assertEquals(0, updated.getCurrentGoldPrice().compareTo(new BigDecimal("6500")));
    }

    @Test
    void testCustomDeleteVendor() {

        //  CREATE vendor first
        Vendor vendor = createVendor("DeleteVendor", "d@gmail.com");

        // DELETE
        vendorRepository.deleteVendorById(vendor.getVendorId());

        //  Sync + clear cache
        entityManager.flush();
        entityManager.clear();

        // VERIFY
        Optional<Vendor> found = vendorRepository.findById(vendor.getVendorId());

        assertFalse(found.isPresent());
    }

    // ---------- BRANCH TESTS ----------

    @Test
    void testCreateVendorBranch() {
        Vendor vendor = createVendor("Joyalukkas", "joy@gmail.com");
        Address address = createAddress("T Nagar");

        VendorBranch branch = vendorBranchRepository.save(
                VendorBranch.builder()
                        .vendor(vendor)
                        .address(address)
                        .quantity(new BigDecimal("50"))
                        .build()
        );

        assertNotNull(branch.getBranchId());
    }

    @Test
    void testFindBranchesByCity() {
        Vendor vendor = createVendor("CityVendor", "cv@gmail.com");
        Address address = createAddress("Adyar");

        vendorBranchRepository.save(
                VendorBranch.builder()
                        .vendor(vendor)
                        .address(address)
                        .quantity(new BigDecimal("30"))
                        .build()
        );

        List<VendorBranch> branches =
                vendorBranchRepository.findBranchesByCity("Chennai");

        assertFalse(branches.isEmpty());
    }

    @Test
    void testFindBranchesWithQuantityGreaterThan() {
        Vendor vendor = createVendor("QtyVendor", "q@gmail.com");
        Address address = createAddress("Velachery");

        vendorBranchRepository.save(
                VendorBranch.builder()
                        .vendor(vendor)
                        .address(address)
                        .quantity(new BigDecimal("80"))
                        .build()
        );

        List<VendorBranch> result =
                vendorBranchRepository.findBranchesWithQuantityGreaterThan(new BigDecimal("50"));

        assertEquals(1, result.size());
    }

    @Test
    void testTotalGoldByVendor() {
        Vendor vendor = createVendor("TotalVendor", "tv@gmail.com");
        Address address = createAddress("Tambaram");

        vendorBranchRepository.save(
                VendorBranch.builder()
                        .vendor(vendor)
                        .address(address)
                        .quantity(new BigDecimal("50"))
                        .build()
        );

        BigDecimal total =
                vendorBranchRepository.getTotalGoldByVendor(vendor.getVendorId());

        assertEquals(0, total.compareTo(new BigDecimal("50")));    }

    @Test
    void testDeleteBranchesByVendor() {
        Vendor vendor = createVendor("DeleteBranchVendor", "db@gmail.com");
        Address address = createAddress("Porur");

        vendorBranchRepository.save(
                VendorBranch.builder()
                        .vendor(vendor)
                        .address(address)
                        .quantity(new BigDecimal("40"))
                        .build()
        );

        vendorBranchRepository.deleteByVendorId(vendor.getVendorId());

        List<VendorBranch> branches =
                vendorBranchRepository.findByVendorVendorId(vendor.getVendorId());

        assertTrue(branches.isEmpty());
    }
}

