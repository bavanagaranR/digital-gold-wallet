package com.goldwallet.digitalgoldwallet.modules.vendor;

import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateBranchRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateVendorRequest;
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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // ----------- 1. CREATE VENDOR ----------
    @Test
    void testCreateVendor_success() {
        CreateVendorRequest req = new CreateVendorRequest();
        req.setVendorName("TestVendor");

        Vendor saved = Vendor.builder().vendorId(1L).vendorName("TestVendor").build();

        when(vendorRepository.save(any())).thenReturn(saved);

        assertEquals("TestVendor", vendorService.createVendor(req).getVendorName());
    }

    // ----------- 2. CREATE VENDOR DEFAULT PRICE ----------
    @Test
    void testCreateVendor_defaultPrice() {
        CreateVendorRequest req = new CreateVendorRequest();
        req.setVendorName("Test");

        Vendor saved = Vendor.builder().vendorId(1L)
                .vendorName("Test")
                .currentGoldPrice(new BigDecimal("5700.00"))
                .build();

        when(vendorRepository.save(any())).thenReturn(saved);

        assertEquals(0, vendorService.createVendor(req)
                .getCurrentGoldPrice()
                .compareTo(new BigDecimal("5700.00")));
    }

    // ----------- 3. GET VENDOR SUCCESS ----------
    @Test
    void testGetVendorById_success() {
        Vendor v = Vendor.builder().vendorId(1L).vendorName("Gold").build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));

        assertEquals("Gold", vendorService.getVendorById(1L).getVendorName());
    }

    // ----------- 4. GET VENDOR NOT FOUND ----------
    @Test
    void testGetVendorById_notFound() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.getVendorById(1L));
    }

    // ----------- 5. GET ALL VENDORS ----------
    @Test
    void testGetAllVendors() {
        when(vendorRepository.findAll()).thenReturn(List.of(
                Vendor.builder().vendorId(1L).vendorName("A").build(),
                Vendor.builder().vendorId(2L).vendorName("B").build()
        ));

        assertEquals(2, vendorService.getAllVendors().size());
    }

    // ----------- 6. UPDATE VENDOR SUCCESS ----------
    @Test
    void testUpdateVendor_success() {
        Vendor v = Vendor.builder().vendorId(1L).vendorName("Old").build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any())).thenReturn(v);

        CreateVendorRequest req = new CreateVendorRequest();
        req.setVendorName("New");

        assertEquals("New", vendorService.updateVendor(1L, req).getVendorName());
    }

    // ----------- 7. UPDATE VENDOR NOT FOUND ----------
    @Test
    void testUpdateVendor_notFound() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.updateVendor(1L, new CreateVendorRequest()));
    }

    // ----------- 8. GET GOLD PRICE ----------
    @Test
    void testGetGoldPrice() {
        Vendor v = Vendor.builder().vendorId(1L)
                .currentGoldPrice(new BigDecimal("6000"))
                .build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(v));

        assertEquals(new BigDecimal("6000"),
                vendorService.getVendorGoldPrice(1L));
    }

    // ----------- 9. ADD BRANCH SUCCESS ----------
    @Test
    void testAddBranch_success() {
        Vendor vendor = Vendor.builder().vendorId(1L).build();
        Address address = new Address();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(branchRepository.save(any()))
                .thenReturn(VendorBranch.builder().branchId(1L).vendor(vendor).build());

        CreateBranchRequest req = new CreateBranchRequest();
        req.setAddressId(1L);

        assertNotNull(vendorService.addBranch(1L, req).getBranchId());
    }

    // ----------- 10. ADD BRANCH WITHOUT ADDRESS ----------
    @Test
    void testAddBranch_withoutAddress() {
        Vendor vendor = Vendor.builder().vendorId(1L).build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(branchRepository.save(any()))
                .thenReturn(VendorBranch.builder().branchId(1L).vendor(vendor).build());

        CreateBranchRequest req = new CreateBranchRequest();

        assertNotNull(vendorService.addBranch(1L, req).getBranchId());
    }

    // ----------- 11. ADD BRANCH VENDOR NOT FOUND ----------
    @Test
    void testAddBranch_vendorNotFound() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.addBranch(1L, new CreateBranchRequest()));
    }

    // ----------- 12. ADD BRANCH ADDRESS NOT FOUND ----------
    @Test
    void testAddBranch_addressNotFound() {
        Vendor vendor = Vendor.builder().vendorId(1L).build();

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        CreateBranchRequest req = new CreateBranchRequest();
        req.setAddressId(1L);

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.addBranch(1L, req));
    }

    // ----------- 13. GET BRANCH SUCCESS ----------
    @Test
    void testGetBranchById_success() {
        Vendor vendor = Vendor.builder().vendorId(1L).vendorName("Gold").build();
        VendorBranch b = VendorBranch.builder().branchId(1L).vendor(vendor).build();

        when(branchRepository.findById(1L)).thenReturn(Optional.of(b));

        assertEquals(1L, vendorService.getBranchById(1L).getBranchId());
    }

    // ----------- 14. GET BRANCH NOT FOUND ----------
    @Test
    void testGetBranchById_notFound() {
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vendorService.getBranchById(1L));
    }

    // ----------- 15. GET BRANCH INVENTORY ----------
    @Test
    void testGetBranchInventory() {
        VendorBranch b = VendorBranch.builder()
                .branchId(1L)
                .quantity(new BigDecimal("100"))
                .build();

        when(branchRepository.findById(1L)).thenReturn(Optional.of(b));

        assertEquals(new BigDecimal("100"),
                vendorService.getBranchInventory(1L));
    }
}












