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

//vendor service
public interface VendorService {
    VendorResponse createVendor(CreateVendorRequest request);
    VendorResponse getVendorById(Long vendorId);
    Page<VendorResponse> getAllVendors(Pageable pageable);
    VendorResponse updateVendor(Long vendorId, UpdateVendorRequest request);
    BigDecimal getVendorGoldPrice(Long vendorId);
    BranchResponse addBranch(Long vendorId, CreateBranchRequest request);
    BranchResponse getBranchById(Long branchId);
    List<BranchResponse> getBranchesByVendor(Long vendorId);
    BigDecimal getBranchInventory(Long branchId);
}
