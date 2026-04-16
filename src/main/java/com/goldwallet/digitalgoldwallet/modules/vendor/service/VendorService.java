package com.goldwallet.digitalgoldwallet.modules.vendor.service;

import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateBranchRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.BranchResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.VendorResponse;

import java.math.BigDecimal;
import java.util.List;

public interface VendorService {
    VendorResponse createVendor(CreateVendorRequest request);
    VendorResponse getVendorById(Long vendorId);
    List<VendorResponse> getAllVendors();
    VendorResponse updateVendor(Long vendorId, CreateVendorRequest request);
    BigDecimal getVendorGoldPrice(Long vendorId);
    BranchResponse addBranch(Long vendorId, CreateBranchRequest request);
    BranchResponse getBranchById(Long branchId);
    List<BranchResponse> getBranchesByVendor(Long vendorId);
    BigDecimal getBranchInventory(Long branchId);
}
