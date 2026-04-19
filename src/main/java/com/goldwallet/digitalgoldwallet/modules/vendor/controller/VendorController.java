package com.goldwallet.digitalgoldwallet.modules.vendor.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateBranchRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.BranchResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.VendorResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/vendors")
    public ResponseEntity<ApiResponse<VendorResponse>> createVendor(@Valid @RequestBody CreateVendorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vendor created", vendorService.createVendor(request)));
    }

    @GetMapping("/vendors/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> getVendor(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(vendorService.getVendorById(id)));
    }

    @GetMapping("/vendors")
    public ResponseEntity<ApiResponse<List<VendorResponse>>> getAllVendors() {
        return ResponseEntity.ok(ApiResponse.success(vendorService.getAllVendors()));
    }

    @PutMapping("/vendors/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> updateVendor(
            @PathVariable Long id, @RequestBody CreateVendorRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Vendor updated", vendorService.updateVendor(id, request)));
    }

    @GetMapping("/vendors/{id}/price")
    public ResponseEntity<ApiResponse<BigDecimal>> getGoldPrice(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(vendorService.getVendorGoldPrice(id)));
    }

    @PostMapping("/vendors/{vendorId}/branches")
    public ResponseEntity<ApiResponse<BranchResponse>> addBranch(
            @PathVariable Long vendorId, @RequestBody CreateBranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Branch added", vendorService.addBranch(vendorId, request)));
    }

    @GetMapping("/branches/{branchId}")
    public ResponseEntity<ApiResponse<BranchResponse>> getBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(ApiResponse.success(vendorService.getBranchById(branchId)));
    }

    @GetMapping("/vendors/{vendorId}/branches")
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getBranches(@PathVariable Long vendorId) {
        return ResponseEntity.ok(ApiResponse.success(vendorService.getBranchesByVendor(vendorId)));
    }

    @GetMapping("/branches/{branchId}/inventory")
    public ResponseEntity<ApiResponse<BigDecimal>> getBranchInventory(@PathVariable Long branchId) {
        return ResponseEntity.ok(ApiResponse.success(vendorService.getBranchInventory(branchId)));
    }
}