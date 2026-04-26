package com.goldwallet.digitalgoldwallet.modules.vendor.service.impl;

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
import com.goldwallet.digitalgoldwallet.modules.vendor.service.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // Enables logging
@Service // Marks this as a service layer component
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository; // Handles vendor DB operations

    @Autowired
    private VendorBranchRepository branchRepository; // Handles branch DB operations

    @Autowired
    private AddressRepository addressRepository; // Handles address DB operations

    @Override
    @Transactional // Ensures all DB operations are atomic
    public VendorResponse createVendor(CreateVendorRequest request) {

        // Check duplicate vendor name
        if (vendorRepository.existsByVendorNameIgnoreCase(request.getVendorName())) {
            throw new BusinessException("Vendor name already in use: " + request.getVendorName());
        }

        // Check duplicate email
        if (request.getContactEmail() != null &&
                vendorRepository.existsByContactEmail(request.getContactEmail())) {
            throw new BusinessException("Vendor email already in use: " + request.getContactEmail());
        }

        // Create vendor entity using builder
        Vendor vendor = Vendor.builder()
                .vendorName(request.getVendorName())
                .description(request.getDescription())
                .contactPersonName(request.getContactPersonName())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .websiteUrl(request.getWebsiteUrl())
                .currentGoldPrice(request.getCurrentGoldPrice())
                .totalGoldQuantity(BigDecimal.ZERO) // initial quantity
                .build();

        // Save and return response DTO
        return mapToVendorResponse(vendorRepository.save(vendor));
    }

    @Override
    public VendorResponse getVendorById(Long vendorId) {
        // Fetch vendor or throw exception
        return mapToVendorResponse(findVendorOrThrow(vendorId));
    }

    @Override
    public Page<VendorResponse> getAllVendors(Pageable pageable) {

        // Apply sorting by vendorId
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("vendorId").ascending()
        );

        // Fetch and map to response
        return vendorRepository.findAll(sortedPageable)
                .map(this::mapToVendorResponse);
    }

    @Override
    @Transactional
    public VendorResponse updateVendor(Long vendorId, UpdateVendorRequest request) {

        // Get existing vendor
        Vendor vendor = findVendorOrThrow(vendorId);

        // Update name with duplicate check
        if (request.getVendorName() != null) {
            String newName = request.getVendorName();
            if (!newName.equalsIgnoreCase(vendor.getVendorName()) &&
                    vendorRepository.existsByVendorNameIgnoreCase(newName)) {
                throw new BusinessException("Vendor name already in use: " + newName);
            }
            vendor.setVendorName(newName);
        }

        // Update optional fields
        if (request.getDescription() != null) {
            vendor.setDescription(request.getDescription());
        }

        if (request.getContactPersonName() != null) {
            vendor.setContactPersonName(request.getContactPersonName());
        }

        // Update email with duplicate check
        if (request.getContactEmail() != null) {
            if (!request.getContactEmail().equalsIgnoreCase(vendor.getContactEmail()) &&
                    vendorRepository.existsByContactEmail(request.getContactEmail())) {
                throw new BusinessException("Vendor email already in use: " + request.getContactEmail());
            }
            vendor.setContactEmail(request.getContactEmail());
        }

        if (request.getContactPhone() != null) {
            vendor.setContactPhone(request.getContactPhone());
        }

        if (request.getWebsiteUrl() != null) {
            vendor.setWebsiteUrl(request.getWebsiteUrl());
        }

        if (request.getCurrentGoldPrice() != null) {
            vendor.setCurrentGoldPrice(request.getCurrentGoldPrice());
        }

        // Save updated vendor
        return mapToVendorResponse(vendorRepository.save(vendor));
    }

    @Override
    public BigDecimal getVendorGoldPrice(Long vendorId) {
        // Return current gold price
        return findVendorOrThrow(vendorId).getCurrentGoldPrice();
    }

    @Override
    @Transactional
    public BranchResponse addBranch(Long vendorId, CreateBranchRequest request) {

        // Fetch vendor and address
        Vendor vendor = findVendorOrThrow(vendorId);
        Address address = findAddressOrThrow(request.getAddressId());

        // Create branch entity
        VendorBranch branch = VendorBranch.builder()
                .vendor(vendor)
                .address(address)
                .quantity(request.getQuantity())
                .build();

        // Save branch
        VendorBranch savedBranch = branchRepository.save(branch);

        // Fetch all branches of vendor
        List<VendorBranch> branches = branchRepository.findByVendorVendorId(vendorId);

        // Calculate total gold quantity
        BigDecimal totalFromBranches = branches.stream()
                .map(VendorBranch::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Update vendor total gold
        vendor.setTotalGoldQuantity(totalFromBranches);
        vendorRepository.save(vendor);

        // Return response
        return mapToBranchResponse(savedBranch);
    }

    @Override
    public BranchResponse getBranchById(Long branchId) {
        // Fetch branch or throw exception
        return mapToBranchResponse(findBranchOrThrow(branchId));
    }

    @Override
    public List<BranchResponse> getBranchesByVendor(Long vendorId) {

        // Ensure vendor exists
        findVendorOrThrow(vendorId);

        // Fetch branches and map to response list
        return branchRepository.findByVendorVendorId(vendorId)
                .stream()
                .map(this::mapToBranchResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getBranchInventory(Long branchId) {
        // Return quantity of branch
        return findBranchOrThrow(branchId).getQuantity();
    }

    // Helper method to fetch vendor or throw exception
    private Vendor findVendorOrThrow(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + vendorId));
    }

    // Helper method to fetch branch or throw exception
    private VendorBranch findBranchOrThrow(Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + branchId));
    }

    // Helper method to fetch address or throw exception
    private Address findAddressOrThrow(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + addressId));
    }

    // Maps Vendor entity to VendorResponse DTO
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

    // Maps VendorBranch entity to BranchResponse DTO
    public BranchResponse mapToBranchResponse(VendorBranch b) {
        return BranchResponse.builder()
                .branchId(b.getBranchId())
                .vendorId(b.getVendor().getVendorId())
                .vendorName(b.getVendor().getVendorName())
                .addressId(b.getAddress().getAddressId())
                .quantity(b.getQuantity())
                .createdAt(b.getCreatedAt())
                .build();
    }
}