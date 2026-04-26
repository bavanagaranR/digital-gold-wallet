package com.goldwallet.digitalgoldwallet.modules.vendor.service;

import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateBranchRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.UpdateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.BranchResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
public interface VendorService {

    // Create vendor
    VendorResponse createVendor(CreateVendorRequest request);

    // Get vendor by ID
    VendorResponse getVendorById(Long vendorId);

    // Get all vendors (pagination)
    Page<VendorResponse> getAllVendors(Pageable pageable);

    // Update vendor details
    VendorResponse updateVendor(Long vendorId, UpdateVendorRequest request);

    // Get vendor gold price
    BigDecimal getVendorGoldPrice(Long vendorId);

    // Add branch to vendor
    BranchResponse addBranch(Long vendorId, CreateBranchRequest request);

    // Get branch by ID
    BranchResponse getBranchById(Long branchId);

    // Get all branches of a vendor
    List<BranchResponse> getBranchesByVendor(Long vendorId);

    // Get branch inventory
    BigDecimal getBranchInventory(Long branchId);
}