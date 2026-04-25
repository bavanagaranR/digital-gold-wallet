package com.goldwallet.digitalgoldwallet.modules.vendor;

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

@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private VendorBranchRepository branchRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private VendorServiceImpl vendorService;

    // ================= POSITIVE TESTS (10) =================

    // 1 - Create vendor successfully returns correct name and phone
    @Test
    void testCreateVendor_success_returnsVendorDetails() {
        CreateVendorRequest req = new CreateVendorRequest();
        req.setVendorName("TestVendor");
        req.setContactEmail("test@vendor.com");
        req.setContactPhone("9876543210");

        Vendor saved = Vendor.builder()
                .vendorId(1L)
                .vendorName("TestVendor")
                .contactEmail("test@vendor.com")
                .contactPhone("9876543210")
                .build();

        when(vendorRepository.existsByContactEmail(req.getContactEmail())).thenReturn(false);
        when(vendorRepository.save(any(Vendor.class))).thenReturn(saved);

        VendorResponse response = vendorService.createVendor(req);
        assertEquals("TestVendor", response.getVendorName());
        assertEquals("9876543210", response.getContactPhone());
    }

    // 2 - Create vendor sets a default gold price
    @Test
    void testCreateVendor_success_defaultPriceSet() {
        CreateVendorRequest req = new CreateVendorRequest();
        req.setVendorName("Test");
        req.setContactPhone("9876543210");

        Vendor saved = Vendor.builder()
                .vendorId(1L)
                .vendorName("Test")
                .contactPhone("9876543210")
                .currentGoldPrice(new BigDecimal("5700.00"))
                .build();

        when(vendorRepository.save(any(Vendor.class))).thenReturn(saved);

        VendorResponse response = vendorService.createVendor(req);
        assertEquals(0, response.getCurrentGoldPrice().compareTo(new BigDecimal("5700.00")));
    }

    // 3 - Get vendor by ID returns correct vendor
    @Test
    void testGetVendorById_success_returnsVendor() {
        Vendor v = Vendor.builder()
                .vendorId(1L)
                .vendorName("Gold")
                .contactPhone("9876543210")
                .build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));

        VendorResponse response = vendorService.getVendorById(1L);
        assertEquals("Gold", response.getVendorName());
    }

    // 4 - Get all vendors returns paginated list of vendors
    @Test
    void testGetAllVendors_success_returnsAllVendors() {
        List<Vendor> vendors = List.of(
                Vendor.builder().vendorId(1L).vendorName("A").contactPhone("9876543210").build(),
                Vendor.builder().vendorId(2L).vendorName("B").contactPhone("9876543211").build()
        );
        Page<Vendor> page = new PageImpl<>(vendors);

        when(vendorRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<VendorResponse> response = vendorService.getAllVendors(PageRequest.of(0, 10));
        assertEquals(2, response.getContent().size());
    }

    // 5 - Update vendor name successfully
    @Test
    void testUpdateVendor_success_nameUpdated() {
        Vendor v = Vendor.builder()
                .vendorId(1L)
                .vendorName("Old")
                .contactPhone("9876543210")
                .build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);

        UpdateVendorRequest req = new UpdateVendorRequest();
        req.setVendorName("New");

        VendorResponse response = vendorService.updateVendor(1L, req);
        assertEquals("New", response.getVendorName());
    }

    // 6 - Get vendor gold price returns correct value
    @Test
    void testGetVendorGoldPrice_success_returnsPrice() {
        Vendor v = Vendor.builder()
                .vendorId(1L)
                .currentGoldPrice(new BigDecimal("6000"))
                .contactPhone("9876543210")
                .build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));

        assertEquals(0, new BigDecimal("6000").compareTo(vendorService.getVendorGoldPrice(1L)));
    }

    // 7 - Add branch to vendor successfully
    @Test
    void testAddBranch_success_returnsBranchWithId() {
        Vendor vendor = Vendor.builder().vendorId(1L).vendorName("Vendor1").contactPhone("9876543210").build();
        Address address = new Address();
        address.setAddressId(1L);

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        VendorBranch branch = VendorBranch.builder()
                .branchId(1L)
                .vendor(vendor)
                .address(address)
                .quantity(new BigDecimal("100"))
                .build();

        when(branchRepository.save(any(VendorBranch.class))).thenReturn(branch);
        when(branchRepository.findByVendorVendorId(1L)).thenReturn(List.of(branch));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        CreateBranchRequest req = new CreateBranchRequest();
        req.setAddressId(1L);
        req.setQuantity(new BigDecimal("100"));

        BranchResponse response = vendorService.addBranch(1L, req);
        assertNotNull(response.getBranchId());
        assertEquals(1L, response.getBranchId());
    }

    // 8 - Get branch by ID returns correct branch
    @Test
    void testGetBranchById_success_returnsBranch() {
        Vendor vendor = Vendor.builder().vendorId(1L).vendorName("Gold").contactPhone("9876543210").build();
        VendorBranch b = VendorBranch.builder().branchId(1L).vendor(vendor).build();

        when(branchRepository.findById(1L)).thenReturn(Optional.of(b));

        BranchResponse response = vendorService.getBranchById(1L);
        assertEquals(1L, response.getBranchId());
    }

    // 9 - Get branch inventory returns correct quantity
    @Test
    void testGetBranchInventory_success_returnsQuantity() {
        VendorBranch b = VendorBranch.builder()
                .branchId(1L)
                .quantity(new BigDecimal("100"))
                .build();

        when(branchRepository.findById(1L)).thenReturn(Optional.of(b));

        assertEquals(0, new BigDecimal("100").compareTo(vendorService.getBranchInventory(1L)));
    }

    // 10 - Get all vendors returns empty page when no vendors exist
    @Test
    void testGetAllVendors_success_emptyPage() {
        Page<Vendor> emptyPage = new PageImpl<>(List.of());

        when(vendorRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<VendorResponse> response = vendorService.getAllVendors(PageRequest.of(0, 10));
        assertTrue(response.isEmpty());
    }

    // ================= NEGATIVE TESTS (5) =================

    // 11 - Get vendor by ID throws when vendor not found
    @Test
    void testGetVendorById_notFound_throwsException() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.getVendorById(1L));
    }

    // 12 - Update vendor throws when vendor not found
    @Test
    void testUpdateVendor_notFound_throwsException() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.updateVendor(1L, new UpdateVendorRequest()));
    }

    // 13 - Add branch throws when vendor not found
    @Test
    void testAddBranch_vendorNotFound_throwsException() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.addBranch(1L, new CreateBranchRequest()));
    }

    // 14 - Add branch throws when address not found
    @Test
    void testAddBranch_addressNotFound_throwsException() {
        Vendor vendor = Vendor.builder().vendorId(1L).build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        CreateBranchRequest req = new CreateBranchRequest();
        req.setAddressId(1L);

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.addBranch(1L, req));
    }

    // 15 - Get branch by ID throws when branch not found
    @Test
    void testGetBranchById_notFound_throwsException() {
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.getBranchById(1L));
    }
}