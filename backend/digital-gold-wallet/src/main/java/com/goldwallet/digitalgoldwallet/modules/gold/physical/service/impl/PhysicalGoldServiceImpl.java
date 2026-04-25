package com.goldwallet.digitalgoldwallet.modules.gold.physical.service.impl;

import com.goldwallet.digitalgoldwallet.common.exception.BusinessException;
import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.request.ConvertToPhysicalRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.response.PhysicalGoldResponse;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.entity.PhysicalGoldTransaction;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.repository.PhysicalGoldTransactionRepository;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.service.PhysicalGoldService;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity.VirtualGoldHolding;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.repository.VirtualGoldHoldingRepository;
import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import com.goldwallet.digitalgoldwallet.modules.transaction.repository.TransactionHistoryRepository;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.AddressResponse;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PhysicalGoldServiceImpl implements PhysicalGoldService {

    @Autowired
    private PhysicalGoldTransactionRepository physicalRepo;

    @Autowired
    private VirtualGoldHoldingRepository holdingRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorBranchRepository branchRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Override
    @Transactional
    public PhysicalGoldResponse convertToPhysical(ConvertToPhysicalRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));
        VendorBranch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + request.getBranchId()));
        Address deliveryAddress = addressRepository.findById(request.getDeliveryAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Delivery address not found: " + request.getDeliveryAddressId()));

        VirtualGoldHolding holding = holdingRepo.findByUserUserIdAndBranchBranchId(user.getUserId(), branch.getBranchId())
                .orElseThrow(() -> new BusinessException("No virtual gold holdings found for this user at this branch"));

        if (holding.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new BusinessException("Insufficient virtual gold. Available: " + holding.getQuantity());
        }

        holding.setQuantity(holding.getQuantity().subtract(request.getQuantity()));
        holdingRepo.save(holding);

        PhysicalGoldTransaction physical = PhysicalGoldTransaction.builder()
                .user(user).branch(branch)
                .quantity(request.getQuantity())
                .deliveryAddress(deliveryAddress)
                .build();
        physical = physicalRepo.save(physical);

        BigDecimal goldPrice = branch.getVendor().getCurrentGoldPrice();
        BigDecimal totalValue = goldPrice.multiply(request.getQuantity());

        transactionHistoryRepository.save(TransactionHistory.builder()
                .user(user).branch(branch)
                .transactionType(TransactionHistory.TransactionType.CONVERT_TO_PHYSICAL)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(request.getQuantity()).amount(totalValue).build());

        log.info("User {} converted {} g to physical from branch {}", user.getUserId(), request.getQuantity(), branch.getBranchId());
        return mapToResponse(physical);
    }

    @Override
    public Page<PhysicalGoldResponse> getUserPhysicalGold(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException("User not found: " + userId);

        return physicalRepo.findByUserUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::mapToResponse);
    }

    private PhysicalGoldResponse mapToResponse(PhysicalGoldTransaction p) {
        AddressResponse addressResponse = null;
        if (p.getDeliveryAddress() != null) {
            Address addr = p.getDeliveryAddress();
            addressResponse = AddressResponse.builder()
                    .addressId(addr.getAddressId())
                    .street(addr.getStreet())
                    .city(addr.getCity())
                    .state(addr.getState())
                    .postalCode(addr.getPostalCode())
                    .country(addr.getCountry())
                    .build();
        }

        return PhysicalGoldResponse.builder()
                .transactionId(p.getTransactionId())
                .userId(p.getUser().getUserId())
                .userName(p.getUser().getName())
                .branchId(p.getBranch().getBranchId())
                .quantity(p.getQuantity())
                .deliveryAddress(addressResponse)
                .createdAt(p.getCreatedAt())
                .build();
    }
}