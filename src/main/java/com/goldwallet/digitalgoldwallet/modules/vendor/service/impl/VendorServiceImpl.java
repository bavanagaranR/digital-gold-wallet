package com.goldwallet.digitalgoldwallet.modules.vendor.service.impl;

import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateBranchRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateVendorRequest;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.BranchResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.VendorResponse;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorBranchRepository branchRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public VendorResponse createVendor(CreateVendorRequest request) {
        Vendor vendor = Vendor.builder()
                .vendorName(request.getVendorName())
                .description(request.getDescription())
                .contactPersonName(request.getContactPersonName())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .websiteUrl(request.getWebsiteUrl())
                .currentGoldPrice(request.getCurrentGoldPrice() != null ? request.getCurrentGoldPrice() : new BigDecimal("5700.00"))
                .build();
        return mapToVendorResponse(vendorRepository.save(vendor));
    }

    @Override
    public VendorResponse getVendorById(Long vendorId) {
        return mapToVendorResponse(findVendorOrThrow(vendorId));
    }

    @Override
    public List<VendorResponse> getAllVendors() {
        return vendorRepository.findAll().stream().map(this::mapToVendorResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VendorResponse updateVendor(Long vendorId, CreateVendorRequest request) {
        Vendor vendor = findVendorOrThrow(vendorId);
        if (request.getVendorName() != null) vendor.setVendorName(request.getVendorName());
        if (request.getDescription() != null) vendor.setDescription(request.getDescription());
        if (request.getContactPersonName() != null) vendor.setContactPersonName(request.getContactPersonName());
        if (request.getContactEmail() != null) vendor.setContactEmail(request.getContactEmail());
        if (request.getContactPhone() != null) vendor.setContactPhone(request.getContactPhone());
        if (request.getWebsiteUrl() != null) vendor.setWebsiteUrl(request.getWebsiteUrl());
        if (request.getCurrentGoldPrice() != null) vendor.setCurrentGoldPrice(request.getCurrentGoldPrice());
        return mapToVendorResponse(vendorRepository.save(vendor));
    }

    @Override
    public BigDecimal getVendorGoldPrice(Long vendorId) {
        return findVendorOrThrow(vendorId).getCurrentGoldPrice();
    }

    @Override
    @Transactional
    public BranchResponse addBranch(Long vendorId, CreateBranchRequest request) {
        Vendor vendor = findVendorOrThrow(vendorId);
        Address address = null;
        if (request.getAddressId() != null) {
            address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + request.getAddressId()));
        }
        VendorBranch branch = VendorBranch.builder()
                .vendor(vendor)
                .address(address)
                .quantity(BigDecimal.ZERO)
                .build();
        return mapToBranchResponse(branchRepository.save(branch));
    }

    @Override
    public BranchResponse getBranchById(Long branchId) {
        return mapToBranchResponse(findBranchOrThrow(branchId));
    }

    @Override
    public List<BranchResponse> getBranchesByVendor(Long vendorId) {
        findVendorOrThrow(vendorId);
        return branchRepository.findByVendorVendorId(vendorId).stream().map(this::mapToBranchResponse).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getBranchInventory(Long branchId) {
        return findBranchOrThrow(branchId).getQuantity();
    }

    private Vendor findVendorOrThrow(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + vendorId));
    }

    private VendorBranch findBranchOrThrow(Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + branchId));
    }

    public VendorResponse mapToVendorResponse(Vendor v) {
        return VendorResponse.builder()
                .vendorId(v.getVendorId())
                .vendorName(v.getVendorName())
                .description(v.getDescription())
                .contactPersonName(v.getContactPersonName())
                .contactEmail(v.getContactEmail())
                .contactPhone(v.getContactPhone())
                .websiteUrl(v.getWebsiteUrl())
                .totalGoldQuantity(v.getTotalGoldQuantity())
                .currentGoldPrice(v.getCurrentGoldPrice())
                .createdAt(v.getCreatedAt())
                .build();
    }

    public BranchResponse mapToBranchResponse(VendorBranch b) {
        return BranchResponse.builder()
                .branchId(b.getBranchId())
                .vendorId(b.getVendor().getVendorId())
                .vendorName(b.getVendor().getVendorName())
                .addressId(b.getAddress() != null ? b.getAddress().getAddressId() : null)
                .quantity(b.getQuantity())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
