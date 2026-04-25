//
//
//package com.goldwallet.digitalgoldwallet.modules.vendor.service.impl;
//
//import com.goldwallet.digitalgoldwallet.common.exception.BusinessException;
//import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
//import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
//import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
//import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateBranchRequest;
//import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.CreateVendorRequest;
//import com.goldwallet.digitalgoldwallet.modules.vendor.dto.request.UpdateVendorRequest;
//import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.BranchResponse;
//import com.goldwallet.digitalgoldwallet.modules.vendor.dto.response.VendorResponse;
//import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
//import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
//import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
//import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorRepository;
//import com.goldwallet.digitalgoldwallet.modules.vendor.service.VendorService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//public class VendorServiceImpl implements VendorService {
//
//    @Autowired
//    private VendorRepository vendorRepository;
//
//    @Autowired
//    private VendorBranchRepository branchRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Override
//    @Transactional
//    public VendorResponse createVendor(CreateVendorRequest request) {
//
//
//
//        if (vendorRepository.existsByContactEmail(request.getContactEmail())) {
//            throw new BusinessException("Vendor email already in use: " + request.getContactEmail());
//        }
//
//        Vendor vendor = Vendor.builder()
//                .vendorName(request.getVendorName())
//                .description(request.getDescription())
//                .contactPersonName(request.getContactPersonName())
//                .contactEmail(request.getContactEmail())
//                .contactPhone(request.getContactPhone())
//                .websiteUrl(request.getWebsiteUrl())
//                .currentGoldPrice(
//                        request.getCurrentGoldPrice() != null
//                                ? request.getCurrentGoldPrice()
//                                : new BigDecimal("5700.00"))
//                .totalGoldQuantity(BigDecimal.ZERO) // ✅ FIXED
//                .build();
//
//        return mapToVendorResponse(vendorRepository.save(vendor));
//    }
//
//    @Override
//    public VendorResponse getVendorById(Long vendorId) {
//        return mapToVendorResponse(findVendorOrThrow(vendorId));
//    }
//
//    @Override
//    public Page<VendorResponse> getAllVendors(Pageable pageable) {
//
//        Pageable sortedPageable = PageRequest.of(
//                pageable.getPageNumber(),
//                pageable.getPageSize(),
//                Sort.by("vendorId").ascending()
//        );
//
//        return vendorRepository.findAll(sortedPageable)
//                .map(this::mapToVendorResponse);
//    }
//
//    @Override
//    @Transactional
//    public VendorResponse updateVendor(Long vendorId, UpdateVendorRequest request) {
//
//        Vendor vendor = findVendorOrThrow(vendorId);
//
//        if (request.getVendorName() != null) {
//            if (!request.getVendorName().equalsIgnoreCase(vendor.getVendorName()) &&
//                    vendorRepository.existsByVendorName(request.getVendorName())) {
//                throw new BusinessException("Vendor name already in use: " + request.getVendorName());
//            }
//            vendor.setVendorName(request.getVendorName());
//        }
//
//        if (request.getDescription() != null) {
//            vendor.setDescription(request.getDescription());
//        }
//
//        if (request.getContactPersonName() != null) {
//            vendor.setContactPersonName(request.getContactPersonName());
//        }
//
//        if (request.getContactEmail() != null) {
//            if (!request.getContactEmail().equalsIgnoreCase(vendor.getContactEmail()) &&
//                    vendorRepository.existsByContactEmail(request.getContactEmail())) {
//                throw new BusinessException("Vendor email already in use: " + request.getContactEmail());
//            }
//            vendor.setContactEmail(request.getContactEmail());
//        }
//
//        if (request.getContactPhone() != null) {
//            vendor.setContactPhone(request.getContactPhone());
//        }
//
//        if (request.getWebsiteUrl() != null) {
//            vendor.setWebsiteUrl(request.getWebsiteUrl());
//        }
//
//        if (request.getCurrentGoldPrice() != null) {
//            vendor.setCurrentGoldPrice(request.getCurrentGoldPrice());
//        }
//
//        // ❌ NO totalGoldQuantity update
//
//        return mapToVendorResponse(vendorRepository.save(vendor));
//    }
//
//    @Override
//    public BigDecimal getVendorGoldPrice(Long vendorId) {
//        return findVendorOrThrow(vendorId).getCurrentGoldPrice();
//    }
//
//    @Override
//    @Transactional
//    public BranchResponse addBranch(Long vendorId, CreateBranchRequest request) {
//
//        Vendor vendor = findVendorOrThrow(vendorId);
//
//        Address address = null;
//        if (request.getAddressId() != null) {
//            address = addressRepository.findById(request.getAddressId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + request.getAddressId()));
//        }
//
//        VendorBranch branch = VendorBranch.builder()
//                .vendor(vendor)
//                .address(address)
//                .quantity(request.getQuantity() != null ? request.getQuantity() : BigDecimal.ZERO)
//                .build();
//
//        branchRepository.save(branch);
//
//        //  Recalculate total from all branches
//        BigDecimal totalFromBranches = branchRepository.findByVendorVendorId(vendorId)
//                .stream()
//                .map(b -> b.getQuantity() != null ? b.getQuantity() : BigDecimal.ZERO)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        vendor.setTotalGoldQuantity(totalFromBranches);
//        vendorRepository.save(vendor);
//
//        return mapToBranchResponse(branch);
//    }
//
//    @Override
//    public BranchResponse getBranchById(Long branchId) {
//        return mapToBranchResponse(findBranchOrThrow(branchId));
//    }
//
//    @Override
//    public List<BranchResponse> getBranchesByVendor(Long vendorId) {
//        findVendorOrThrow(vendorId);
//        return branchRepository.findByVendorVendorId(vendorId)
//                .stream()
//                .map(this::mapToBranchResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public BigDecimal getBranchInventory(Long branchId) {
//        return findBranchOrThrow(branchId).getQuantity();
//    }
//
//    private Vendor findVendorOrThrow(Long vendorId) {
//        return vendorRepository.findById(vendorId)
//                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + vendorId));
//    }
//
//    private VendorBranch findBranchOrThrow(Long branchId) {
//        return branchRepository.findById(branchId)
//                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + branchId));
//    }
//
//    public VendorResponse mapToVendorResponse(Vendor v) {
//        return VendorResponse.builder()
//                .vendorId(v.getVendorId())
//                .vendorName(v.getVendorName())
//                .description(v.getDescription())
//                .contactPersonName(v.getContactPersonName())
//                .contactEmail(v.getContactEmail())
//                .contactPhone(v.getContactPhone())
//                .websiteUrl(v.getWebsiteUrl())
//                .totalGoldQuantity(v.getTotalGoldQuantity())
//                .currentGoldPrice(v.getCurrentGoldPrice())
//                .createdAt(v.getCreatedAt())
//                .build();
//    }
//
//    public BranchResponse mapToBranchResponse(VendorBranch b) {
//        return BranchResponse.builder()
//                .branchId(b.getBranchId())
//                .vendorId(b.getVendor().getVendorId())
//                .vendorName(b.getVendor().getVendorName())
//                .addressId(b.getAddress() != null ? b.getAddress().getAddressId() : null)
//                .quantity(b.getQuantity())
//                .createdAt(b.getCreatedAt())
//                .build();
//    }
//}



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

@Slf4j
@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorBranchRepository branchRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public VendorResponse createVendor(CreateVendorRequest request) {

        if (request.getContactEmail() != null &&
                vendorRepository.existsByContactEmail(request.getContactEmail())) {
            throw new BusinessException("Vendor email already in use: " + request.getContactEmail());
        }

        Vendor vendor = Vendor.builder()
                .vendorName(request.getVendorName())
                .description(request.getDescription())
                .contactPersonName(request.getContactPersonName())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .websiteUrl(request.getWebsiteUrl())
                .currentGoldPrice(
                        request.getCurrentGoldPrice() != null
                                ? request.getCurrentGoldPrice()
                                : new BigDecimal("5700.00"))
                .totalGoldQuantity(BigDecimal.ZERO)
                .build();

        return mapToVendorResponse(vendorRepository.save(vendor));
    }

    @Override
    public VendorResponse getVendorById(Long vendorId) {
        return mapToVendorResponse(findVendorOrThrow(vendorId));
    }

    @Override
    public Page<VendorResponse> getAllVendors(Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("vendorId").ascending()
        );

        return vendorRepository.findAll(sortedPageable)
                .map(this::mapToVendorResponse);
    }

    @Override
    @Transactional
    public VendorResponse updateVendor(Long vendorId, UpdateVendorRequest request) {

        Vendor vendor = findVendorOrThrow(vendorId);

        if (request.getVendorName() != null) {
            if (!request.getVendorName().equalsIgnoreCase(vendor.getVendorName()) &&
                    vendorRepository.existsByVendorName(request.getVendorName())) {
                throw new BusinessException("Vendor name already in use: " + request.getVendorName());
            }
            vendor.setVendorName(request.getVendorName());
        }

        if (request.getDescription() != null) {
            vendor.setDescription(request.getDescription());
        }

        if (request.getContactPersonName() != null) {
            vendor.setContactPersonName(request.getContactPersonName());
        }

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

        // ✅ FIX: remove null check (addressId is mandatory)
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + request.getAddressId()));

        VendorBranch branch = VendorBranch.builder()
                .vendor(vendor)
                .address(address)
                .quantity(request.getQuantity() != null ? request.getQuantity() : BigDecimal.ZERO)
                .build();

        // ✅ FIX 1: capture saved entity
        VendorBranch savedBranch = branchRepository.save(branch);

        // ✅ FIX 2: prevent null pointer
        List<VendorBranch> branches = branchRepository.findByVendorVendorId(vendorId);

        BigDecimal totalFromBranches = branches == null ? BigDecimal.ZERO :
                branches.stream()
                        .map(b -> b.getQuantity() != null ? b.getQuantity() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        vendor.setTotalGoldQuantity(totalFromBranches);
        vendorRepository.save(vendor);

        // ✅ FIX 3: return saved entity
        return mapToBranchResponse(savedBranch);
    }
    @Override
    public BranchResponse getBranchById(Long branchId) {
        return mapToBranchResponse(findBranchOrThrow(branchId));
    }

    @Override
    public List<BranchResponse> getBranchesByVendor(Long vendorId) {
        findVendorOrThrow(vendorId);
        return branchRepository.findByVendorVendorId(vendorId)
                .stream()
                .map(this::mapToBranchResponse)
                .collect(Collectors.toList());
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