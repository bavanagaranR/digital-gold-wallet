package com.goldwallet.digitalgoldwallet.modules.vendor;

import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorRepository;
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

    // Helper method
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
                .websiteUrl("https://www." + name.toLowerCase().replace(" ", "") + ".com")                .totalGoldQuantity(BigDecimal.ZERO)
                .currentGoldPrice(new BigDecimal("5700.00"))
                .build());
    }
    //  Create Vendor
    @Test
    void testCreateVendor() {
        Vendor vendor = createVendor("Tanishq", "tanishq@gmail.com");
        assertNotNull(vendor.getVendorId());
    }

    //  Find Vendor by ID
    @Test
    void testFindVendorById() {
        Vendor vendor = createVendor("Kalyan", "kalyan@gmail.com");

        Optional<Vendor> found = vendorRepository.findById(vendor.getVendorId());

        assertTrue(found.isPresent());
    }

    //  Update Vendor Gold Price
    @Test
    void testUpdateGoldPrice() {
        Vendor vendor = createVendor("Malabar", "malabar@gmail.com");

        vendor.setCurrentGoldPrice(new BigDecimal("6000"));
        Vendor updated = vendorRepository.save(vendor);

        assertEquals(new BigDecimal("6000"), updated.getCurrentGoldPrice());
    }

    //  Delete Vendor
    @Test
    void testDeleteVendor() {
        Vendor vendor = createVendor("GRT", "grt@gmail.com");

        vendorRepository.delete(vendor);

        Optional<Vendor> found = vendorRepository.findById(vendor.getVendorId());
        assertFalse(found.isPresent());
    }

    //  Create Vendor Branch
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

    //  Vendor - Branch Mapping
    @Test
    void testVendorBranchMapping() {
        Vendor vendor = createVendor("AVM Gold", "avm@gmail.com");
        Address address = createAddress("Anna Nagar");

        VendorBranch branch = vendorBranchRepository.save(
                VendorBranch.builder()
                        .vendor(vendor)
                        .address(address)
                        .quantity(new BigDecimal("100"))
                        .build()
        );

        Optional<VendorBranch> found = vendorBranchRepository.findById(branch.getBranchId());

        assertTrue(found.isPresent());
        assertEquals("AVM Gold", found.get().getVendor().getVendorName());
    }

    //  Multiple Vendors
    @Test
    void testMultipleVendors() {
        createVendor("Vendor1", "v1@gmail.com");
        createVendor("Vendor2", "v2@gmail.com");

        List<Vendor> vendors = vendorRepository.findAll();

        assertEquals(2, vendors.size());
    }

    //  Branch Quantity Update
    @Test
    void testUpdateBranchQuantity() {
        Vendor vendor = createVendor("Prince Gold", "prince@gmail.com");
        Address address = createAddress("Velachery");

        VendorBranch branch = vendorBranchRepository.save(
                VendorBranch.builder()
                        .vendor(vendor)
                        .address(address)
                        .quantity(new BigDecimal("20"))
                        .build()
        );

        branch.setQuantity(new BigDecimal("80"));
        VendorBranch updated = vendorBranchRepository.save(branch);

        assertEquals(new BigDecimal("80"), updated.getQuantity());
    }

    //  Delete Branch
    @Test
    void testDeleteBranch() {
        Vendor vendor = createVendor("Lalitha", "lalitha@gmail.com");
        Address address = createAddress("Porur");

        VendorBranch branch = vendorBranchRepository.save(
                VendorBranch.builder()
                        .vendor(vendor)
                        .address(address)
                        .quantity(new BigDecimal("30"))
                        .build()
        );

        vendorBranchRepository.delete(branch);

        Optional<VendorBranch> found = vendorBranchRepository.findById(branch.getBranchId());
        assertFalse(found.isPresent());
    }

    //  Vendor Default Values
    @Test
    void testDefaultValues() {
        Vendor vendor = vendorRepository.save(Vendor.builder()
                .vendorName("DefaultVendor")
                .contactEmail("default@gmail.com")
                .websiteUrl("https://default.com")  // ✅ ADD THIS
                .build());

        assertEquals(BigDecimal.ZERO, vendor.getTotalGoldQuantity());
        assertEquals(new BigDecimal("5700.00"), vendor.getCurrentGoldPrice());
    }
}
