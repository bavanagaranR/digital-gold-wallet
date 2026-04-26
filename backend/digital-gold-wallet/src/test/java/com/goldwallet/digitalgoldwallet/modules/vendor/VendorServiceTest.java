package com.goldwallet.digitalgoldwallet.modules.vendor;

import com.goldwallet.digitalgoldwallet.common.exception.BusinessException;
import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateBranchRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.UpdateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.BranchResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.VendorResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.service.impl.VendorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class) // Enables Mockito support in JUnit 5
class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository; // Mocked repository for vendor

    @Mock
    private VendorBranchRepository branchRepository; // Mocked repository for branches

    @Mock
    private AddressRepository addressRepository; // Mocked repository for address

    @InjectMocks
    private VendorServiceImpl vendorService; // Injects mocks into service

    // ----------- 1. CREATE VENDOR SUCCESS ----------
    @Test
    void testCreateVendor_success() {

        // Prepare request data
        CreateVendorRequest req = new CreateVendorRequest();
        req.setVendorName("TestVendor");
        req.setContactEmail("test@vendor.com");
        req.setContactPhone("9876543210");

        // Mocked DB response after save
        Vendor saved = Vendor.builder()
                .vendorId(1L)
                .vendorName("TestVendor")
                .contactEmail("test@vendor.com")
                .contactPhone("9876543210")
                .build();

        // Mock validation checks
        when(vendorRepository.existsByVendorNameIgnoreCase(req.getVendorName())).thenReturn(false);
        when(vendorRepository.existsByContactEmail(req.getContactEmail())).thenReturn(false);

        // Mock save operation
        when(vendorRepository.save(any(Vendor.class))).thenReturn(saved);

        // Call service method
        VendorResponse response = vendorService.createVendor(req);

        // Verify result
        assertEquals("TestVendor", response.getVendorName());
        assertEquals("9876543210", response.getContactPhone());
    }

    // ----------- 2. CREATE VENDOR NULL PRICE ----------
    @Test
    void testCreateVendor_nullPrice_whenNotProvided() {

        // Request without price
        CreateVendorRequest req = new CreateVendorRequest();
        req.setVendorName("Test");
        req.setContactPhone("9876543210");

        // Mocked saved entity with null price
        Vendor saved = Vendor.builder()
                .vendorId(1L)
                .vendorName("Test")
                .contactPhone("9876543210")
                .currentGoldPrice(null)
                .build();

        // Mock validation
        when(vendorRepository.existsByVendorNameIgnoreCase("Test")).thenReturn(false);

        // Mock save
        when(vendorRepository.save(any(Vendor.class))).thenReturn(saved);

        // Execute service
        VendorResponse response = vendorService.createVendor(req);

        // Verify price is null
        assertNull(response.getCurrentGoldPrice());
    }

    // ----------- 3. GET VENDOR SUCCESS ----------
    @Test
    void testGetVendorById_success() {

        // Mock existing vendor
        Vendor v = Vendor.builder()
                .vendorId(1L)
                .vendorName("Gold")
                .contactPhone("9876543210")
                .build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));

        // Call service
        VendorResponse response = vendorService.getVendorById(1L);

        // Verify result
        assertEquals("Gold", response.getVendorName());
    }

    // ----------- 4. GET VENDOR NOT FOUND ----------
    @Test
    void testGetVendorById_notFound() {

        // Mock vendor not found
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify exception
        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.getVendorById(1L));
    }

    // ----------- 5. GET ALL VENDORS ----------
    @Test
    void testGetAllVendors() {

        // Mock vendor list
        List<Vendor> vendors = List.of(
                Vendor.builder().vendorId(1L).vendorName("A").contactPhone("9876543210").build(),
                Vendor.builder().vendorId(2L).vendorName("B").contactPhone("9876543211").build()
        );

        Page<Vendor> page = new PageImpl<>(vendors);

        when(vendorRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Call service
        Page<VendorResponse> response = vendorService.getAllVendors(PageRequest.of(0, 10));

        // Verify results
        assertEquals(2, response.getContent().size());
        assertEquals("A", response.getContent().get(0).getVendorName());
    }

    // ----------- 6. UPDATE VENDOR SUCCESS ----------
    @Test
    void testUpdateVendor_success() {

        // Existing vendor
        Vendor v = Vendor.builder()
                .vendorId(1L)
                .vendorName("Old")
                .contactPhone("9876543210")
                .build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);

        // Update request
        UpdateVendorRequest req = new UpdateVendorRequest();
        req.setVendorName("New");

        // Execute
        VendorResponse response = vendorService.updateVendor(1L, req);

        // Verify update
        assertEquals("New", response.getVendorName());
    }

    // ----------- 7. UPDATE VENDOR NOT FOUND ----------
    @Test
    void testUpdateVendor_notFound() {

        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.updateVendor(1L, new UpdateVendorRequest()));
    }

    // ----------- 8. GET GOLD PRICE ----------
    @Test
    void testGetGoldPrice() {

        // Mock vendor with price
        Vendor v = Vendor.builder()
                .vendorId(1L)
                .currentGoldPrice(new BigDecimal("6000"))
                .contactPhone("9876543210")
                .build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));

        // Verify price
        assertEquals(0,
                new BigDecimal("6000").compareTo(vendorService.getVendorGoldPrice(1L)));
    }

    // ----------- 9. ADD BRANCH SUCCESS ----------
    @Test
    void testAddBranch_success() {

        // Create mock vendor
        Vendor vendor = Vendor.builder()
                .vendorId(1L)
                .vendorName("Vendor1")
                .contactPhone("9876543210")
                .build();

        // Create mock address
        Address address = new Address();
        address.setAddressId(1L);

        // Create mock branch (what DB will return after save)
        VendorBranch branch = VendorBranch.builder()
                .branchId(1L)
                .vendor(vendor)
                .address(address)
                .quantity(new BigDecimal("100"))
                .build();

        // Mock repository calls
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor)); // vendor exists
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address)); // address exists
        when(branchRepository.save(any(VendorBranch.class))).thenReturn(branch); // save branch
        when(branchRepository.findByVendorVendorId(1L)).thenReturn(List.of(branch)); // fetch branches
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor); // update vendor

        // Create request
        CreateBranchRequest req = new CreateBranchRequest();
        req.setAddressId(1L);
        req.setQuantity(new BigDecimal("100"));

        // Call service
        BranchResponse response = vendorService.addBranch(1L, req);

        // Verify result
        assertNotNull(response.getBranchId());
        assertEquals(1L, response.getBranchId());
    }


    // ----------- 10. ADD BRANCH VENDOR NOT FOUND ----------
    @Test
    void testAddBranch_vendorNotFound() {

        // Mock vendor not found
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.addBranch(1L, new CreateBranchRequest()));
    }


    // ----------- 11. ADD BRANCH ADDRESS NOT FOUND ----------
    @Test
    void testAddBranch_addressNotFound() {

        // Mock vendor exists
        Vendor vendor = Vendor.builder().vendorId(1L).build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));

        // Mock address not found
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        // Create request
        CreateBranchRequest req = new CreateBranchRequest();
        req.setAddressId(1L);

        // Expect exception
        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.addBranch(1L, req));
    }


    // ----------- 12. GET BRANCH SUCCESS ----------
    @Test
    void testGetBranchById_success() {

        // Mock vendor
        Vendor vendor = Vendor.builder()
                .vendorId(1L)
                .vendorName("Gold")
                .contactPhone("9876543210")
                .build();

        // Mock address
        Address address = new Address();
        address.setAddressId(10L);

        // Mock branch
        VendorBranch b = VendorBranch.builder()
                .branchId(1L)
                .vendor(vendor)
                .address(address)
                .build();

        // Mock repository response
        when(branchRepository.findById(1L)).thenReturn(Optional.of(b));

        // Call service
        BranchResponse response = vendorService.getBranchById(1L);

        // Verify result
        assertEquals(1L, response.getBranchId());
    }


    // ----------- 13. GET BRANCH NOT FOUND ----------
    @Test
    void testGetBranchById_notFound() {

        // Mock branch not found
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.getBranchById(1L));
    }


    // ----------- 14. GET BRANCH INVENTORY ----------
    @Test
    void testGetBranchInventory() {

        // Mock branch with quantity
        VendorBranch b = VendorBranch.builder()
                .branchId(1L)
                .quantity(new BigDecimal("100"))
                .build();

        when(branchRepository.findById(1L)).thenReturn(Optional.of(b));

        // Verify quantity returned
        assertEquals(0,
                new BigDecimal("100").compareTo(vendorService.getBranchInventory(1L)));
    }


    // ----------- 15. DUPLICATE VENDOR NAME ----------
    @Test
    void testCreateVendor_duplicateName() {

        // Create request with duplicate name
        CreateVendorRequest req = new CreateVendorRequest();
        req.setVendorName("Gold");
        req.setContactPhone("9876543210");

        // Mock duplicate check
        when(vendorRepository.existsByVendorNameIgnoreCase("Gold")).thenReturn(true);

        // Expect BusinessException
        assertThrows(BusinessException.class,
                () -> vendorService.createVendor(req));
    }
}