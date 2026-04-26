package com.goldwallet.digitalgoldwallet.modules.vendor.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateBranchRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.UpdateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.BranchResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.VendorResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController // Marks this class as REST API controller
@RequestMapping("/api/v1") // Base URL for all endpoints
public class VendorController {

    @Autowired
    private VendorService vendorService; // Service layer dependency

    // ---------- CREATE VENDOR ----------
    @PostMapping("/vendors")
    public ResponseEntity<ApiResponse<VendorResponse>> createVendor(
            @Valid @RequestBody CreateVendorRequest request) {

        // Calls service to create vendor and returns 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vendor created", vendorService.createVendor(request)));
    }


    // ---------- GET VENDOR BY ID ----------
    @GetMapping("/vendors/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> getVendor(@PathVariable Long id) {

        // Fetch single vendor by ID
        return ResponseEntity.ok(
                ApiResponse.success(vendorService.getVendorById(id))
        );
    }


    // ---------- GET ALL VENDORS (WITH PAGINATION) ----------
    @GetMapping("/vendors")
    public ResponseEntity<ApiResponse<Page<VendorResponse>>> getAllVendors(Pageable pageable) {

        // Fetch all vendors with pagination support
        return ResponseEntity.ok(
                ApiResponse.success(vendorService.getAllVendors(pageable))
        );
    }


    // ---------- UPDATE VENDOR ----------
    @PutMapping("/vendors/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVendorRequest request) {

        // Updates vendor details
        return ResponseEntity.ok(
                ApiResponse.success("Vendor updated", vendorService.updateVendor(id, request))
        );
    }


    // ---------- GET VENDOR GOLD PRICE ----------
    @GetMapping("/vendors/{id}/price")
    public ResponseEntity<ApiResponse<BigDecimal>> getGoldPrice(@PathVariable Long id) {

        // Fetch current gold price of vendor
        return ResponseEntity.ok(
                ApiResponse.success(vendorService.getVendorGoldPrice(id))
        );
    }


    // ---------- ADD BRANCH TO VENDOR ----------
    @PostMapping("/vendors/{vendorId}/branches")
    public ResponseEntity<ApiResponse<BranchResponse>> addBranch(
            @PathVariable Long vendorId,
            @Valid @RequestBody CreateBranchRequest request) {

        // Adds new branch for a vendor
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Branch added", vendorService.addBranch(vendorId, request)));
    }


    // ---------- GET BRANCH BY ID ----------
    @GetMapping("/branches/{branchId}")
    public ResponseEntity<ApiResponse<BranchResponse>> getBranch(@PathVariable Long branchId) {

        // Fetch branch details by ID
        return ResponseEntity.ok(
                ApiResponse.success(vendorService.getBranchById(branchId))
        );
    }


    // ---------- GET ALL BRANCHES OF A VENDOR ----------
    @GetMapping("/vendors/{vendorId}/branches")
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getBranches(@PathVariable Long vendorId) {

        // Fetch all branches for a specific vendor
        return ResponseEntity.ok(
                ApiResponse.success(vendorService.getBranchesByVendor(vendorId))
        );
    }


    // ---------- GET BRANCH INVENTORY ----------
    @GetMapping("/branches/{branchId}/inventory")
    public ResponseEntity<ApiResponse<BigDecimal>> getBranchInventory(@PathVariable Long branchId) {

        // Fetch gold quantity available in a branch
        return ResponseEntity.ok(
                ApiResponse.success(vendorService.getBranchInventory(branchId))
        );
    }
}