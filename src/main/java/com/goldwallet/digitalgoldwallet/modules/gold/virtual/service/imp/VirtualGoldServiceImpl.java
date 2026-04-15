package com.goldwallet.digitalgoldwallet.modules.gold.virtual.service.imp;

import com.goldwallet.digitalgoldwallet.common.exception.BusinessException;
import com.goldwallet.digitalgoldwallet.common.exception.InsufficientBalanceException;
import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.BuyGoldRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.SellGoldRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.response.VirtualGoldResponse;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity.VirtualGoldHolding;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.repository.VirtualGoldHoldingRepository;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.service.VirtualGoldService;
import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import com.goldwallet.digitalgoldwallet.modules.transaction.repository.TransactionHistoryRepository;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualGoldServiceImpl implements VirtualGoldService {

    private final VirtualGoldHoldingRepository holdingRepository;
    private final UserRepository userRepository;
    private final VendorBranchRepository branchRepository;
    private final VendorRepository vendorRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Override
    @Transactional
    public VirtualGoldResponse buyGold(BuyGoldRequest request) {
        User user = findUserOrThrow(request.getUserId());
        VendorBranch branch = findBranchOrThrow(request.getBranchId());
        Vendor vendor = branch.getVendor();

        BigDecimal price = vendor.getCurrentGoldPrice();
        BigDecimal totalCost = price.multiply(request.getQuantity());

        if (user.getBalance().compareTo(totalCost) < 0) {
            throw new InsufficientBalanceException("Insufficient wallet balance. Required: " + totalCost + ", Available: " + user.getBalance());
        }
        if (branch.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new BusinessException("Insufficient gold at branch. Available: " + branch.getQuantity());
        }

        // Deduct balance
        user.setBalance(user.getBalance().subtract(totalCost));
        userRepository.save(user);

        // Deduct from branch
        branch.setQuantity(branch.getQuantity().subtract(request.getQuantity()));
        branchRepository.save(branch);

        // Update or create holding
        Optional<VirtualGoldHolding> existingOpt = holdingRepository.findByUserUserIdAndBranchBranchId(user.getUserId(), branch.getBranchId());
        VirtualGoldHolding holding;
        if (existingOpt.isPresent()) {
            holding = existingOpt.get();
            holding.setQuantity(holding.getQuantity().add(request.getQuantity()));
        } else {
            holding = VirtualGoldHolding.builder()
                    .user(user).branch(branch).quantity(request.getQuantity()).build();
        }
        holding = holdingRepository.save(holding);

        // Record transaction
        transactionHistoryRepository.save(TransactionHistory.builder()
                .user(user).branch(branch)
                .transactionType(TransactionHistory.TransactionType.BUY)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(request.getQuantity()).amount(totalCost).build());

        log.info("User {} bought {} g gold from branch {}", user.getUserId(), request.getQuantity(), branch.getBranchId());
        return mapToResponse(holding, price);
    }

    @Override
    @Transactional
    public VirtualGoldResponse sellGold(SellGoldRequest request) {
        User user = findUserOrThrow(request.getUserId());
        VendorBranch branch = findBranchOrThrow(request.getBranchId());
        Vendor vendor = branch.getVendor();

        VirtualGoldHolding holding = holdingRepository.findByUserUserIdAndBranchBranchId(user.getUserId(), branch.getBranchId())
                .orElseThrow(() -> new BusinessException("No gold holdings found for this user at this branch"));

        if (holding.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new BusinessException("Insufficient gold holdings. Available: " + holding.getQuantity());
        }

        BigDecimal price = vendor.getCurrentGoldPrice();
        BigDecimal totalValue = price.multiply(request.getQuantity());

        holding.setQuantity(holding.getQuantity().subtract(request.getQuantity()));
        holdingRepository.save(holding);

        branch.setQuantity(branch.getQuantity().add(request.getQuantity()));
        branchRepository.save(branch);

        user.setBalance(user.getBalance().add(totalValue));
        userRepository.save(user);

        transactionHistoryRepository.save(TransactionHistory.builder()
                .user(user).branch(branch)
                .transactionType(TransactionHistory.TransactionType.SELL)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(request.getQuantity()).amount(totalValue).build());

        log.info("User {} sold {} g gold at branch {}", user.getUserId(), request.getQuantity(), branch.getBranchId());
        return mapToResponse(holding, price);
    }

    @Override
    public List<VirtualGoldResponse> getUserGoldHoldings(Long userId) {
        findUserOrThrow(userId);
        return holdingRepository.findByUserUserId(userId).stream()
                .map(h -> mapToResponse(h, h.getBranch().getVendor().getCurrentGoldPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<VirtualGoldResponse> getBranchGoldHoldings(Long branchId) {
        findBranchOrThrow(branchId);
        return holdingRepository.findByBranchBranchId(branchId).stream()
                .map(h -> mapToResponse(h, h.getBranch().getVendor().getCurrentGoldPrice()))
                .collect(Collectors.toList());
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private VendorBranch findBranchOrThrow(Long branchId) {
        return branchRepository.findById(branchId).orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + branchId));
    }

    private VirtualGoldResponse mapToResponse(VirtualGoldHolding h, BigDecimal goldPrice) {
        return VirtualGoldResponse.builder()
                .holdingId(h.getHoldingId())
                .userId(h.getUser().getUserId())
                .userName(h.getUser().getName())
                .branchId(h.getBranch().getBranchId())
                .quantity(h.getQuantity())
                .totalValue(h.getQuantity().multiply(goldPrice))
                .createdAt(h.getCreatedAt())
                .build();
    }
}
