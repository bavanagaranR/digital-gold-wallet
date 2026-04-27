package com.goldwallet.digitalgoldwallet.modules.gold.virtual.service.impl;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j                            //automatically creates logger object, logger is tool to print structured messages used instead of s.o.pln
@Service                         //denotes it as business logic layer (create and manage it as a bean)
public class VirtualGoldServiceImpl implements VirtualGoldService {

    @Autowired //injects spring bean automatically
    private VirtualGoldHoldingRepository holdingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorBranchRepository branchRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Override
    @Transactional
    public VirtualGoldResponse buyGold(BuyGoldRequest request) {

        //flow: Holding → Branch → Vendor → Price
        User user = findUserOrThrow(request.getUserId());  //request carries user_id so this checks whether user exists or not
        VendorBranch branch = findBranchOrThrow(request.getBranchId()); //checks whether requested branch exists or not
        Vendor vendor = branch.getVendor();  //checks which vendor belongs to the requested branch

        BigDecimal price = vendor.getCurrentGoldPrice();   //gets the price per gram quoted by vendor
//        BigDecimal totalCost = price.multiply(request.getQuantity());  //calculating cost needed to buy requested quantity of gold
        BigDecimal totalCost = price.multiply(request.getQuantity())
                .setScale(2, RoundingMode.HALF_UP);
        if (user.getBalance().compareTo(totalCost) < 0) {          //in compareTo: < 0(i.e. a=b) means a<b(i.e. -1), >0 means a>b (i.e. 1)
            throw new InsufficientBalanceException("Insufficient wallet balance. Required: " + totalCost + ", Available: " + user.getBalance());  //throw a custom runtime exception called InsufficientBalanceException. Since this is inside a @Transactional method, throwing this exception also triggers a rollback, ensuring no partial database updates happen
        }
        if (branch.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new BusinessException("Insufficient gold at branch. Available: " + branch.getQuantity());
        }

        user.setBalance(user.getBalance().subtract(totalCost));  //after successful purchase, update user balance
        userRepository.save(user);   //updates changes in db

        branch.setQuantity(branch.getQuantity().subtract(request.getQuantity()));
        branchRepository.save(branch);

        Optional<VirtualGoldHolding> existingOpt =      //checks whether the user already has a holding id or not
                holdingRepository.findByUserUserIdAndBranchBranchId(user.getUserId(), branch.getBranchId());

        VirtualGoldHolding holding;            //declaring local variable to store or update the holding record
        if (existingOpt.isPresent()) {        //if user already has a holding record
            holding = existingOpt.get();      //if exists, it extracts the holding id
            holding.setQuantity(holding.getQuantity().add(request.getQuantity()));  //in that holding id, add the new brought qty with existing qty (prev 4, now bought 3, then update qty as 7g)
        }
        //User has never bought from this branch so create new record
        else {                                //creates new holding id for that user
            holding = VirtualGoldHolding.builder()
                    .user(user)
                    .branch(branch)
                    .quantity(request.getQuantity()) //takes cur user, branch and requested/purchased qty
                    .build();                        //sets them to create a new record
        }

        holding = holdingRepository.save(holding);   //saves the new record to db

        transactionHistoryRepository.save(TransactionHistory.builder()              //1. Service creates TransactionHistory object to stores this gold transaction in transaction table
                .user(user).branch(branch)                                          // 2. Calls repository.save()
                .transactionType(TransactionHistory.TransactionType.BUY)            //3. Spring JPA converts object → SQL
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)    //4. SQL executes INSERT query
                .quantity(request.getQuantity())
                .amount(totalCost)
                .build());        // 5. Record stored in DB

        log.info("User {} bought {} g gold from branch {}", user.getUserId(), request.getQuantity(), branch.getBranchId());

        return mapToResponse(holding, price);
    }

    @Override
    @Transactional
    public VirtualGoldResponse sellGold(SellGoldRequest request) {
        User user = findUserOrThrow(request.getUserId());
        VendorBranch branch = findBranchOrThrow(request.getBranchId());
        Vendor vendor = branch.getVendor();

        VirtualGoldHolding holding = holdingRepository  //before selling checks whether the user has holdings only if so we can sell
                .findByUserUserIdAndBranchBranchId(user.getUserId(), branch.getBranchId())
                .orElseThrow(() -> new BusinessException("No gold holdings found for this user at this branch"));

        if (holding.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new BusinessException("Insufficient gold holdings. Available: " + holding.getQuantity());
        }

        BigDecimal price = vendor.getCurrentGoldPrice();

        BigDecimal totalValue = price.multiply(request.getQuantity())
                .setScale(2, RoundingMode.HALF_UP);

        holding.setQuantity(holding.getQuantity().subtract(request.getQuantity())); //here qty is subtracted because of selling
        holdingRepository.save(holding);

        branch.setQuantity(branch.getQuantity().add(request.getQuantity())); //user sold gold to a branch, so branch qty should be updated
        branchRepository.save(branch);

        user.setBalance(user.getBalance().add(totalValue));   //since it's selling, user's bal has to be increased
        userRepository.save(user);

        transactionHistoryRepository.save(TransactionHistory.builder()
                .user(user).branch(branch)
                .transactionType(TransactionHistory.TransactionType.SELL)
                .transactionStatus(TransactionHistory.TransactionStatus.SUCCESS)
                .quantity(request.getQuantity()).amount(totalValue).build());

        log.info("User {} sold {} g gold at branch {}", user.getUserId(), request.getQuantity(), branch.getBranchId());

        return mapToResponse(holding, price);
    }

    @Override                  //gets data from db -> convert it -> sends to frontend
    public List<VirtualGoldResponse> getUserGoldHoldings(Long userId) {
        findUserOrThrow(userId);   //validates whether the user exists
        List<VirtualGoldHolding> holdings = holdingRepository.findByUserUserId(userId); //fetches the amount of gold one user holds
        List<VirtualGoldResponse> result = new ArrayList<>();
        for (VirtualGoldHolding h : holdings) {    //Take each holding → convert it → add to result list
            BigDecimal price = h.getBranch().getVendor().getCurrentGoldPrice();   //Holding → Branch → Vendor → Gold Price
            VirtualGoldResponse response = mapToResponse(h, price);  //converts Database object → API response object
            result.add(response);
        }
        return result;
    }
    @Override
    public List<VirtualGoldResponse> getBranchGoldHoldings(Long branchId) {
        findBranchOrThrow(branchId); // validate branch exists
        List<VirtualGoldHolding> holdings = holdingRepository.findByBranchBranchId(branchId); //Fetch all holdings for that branch
        List<VirtualGoldResponse> result = new ArrayList<>(); // prepare result list
        for (VirtualGoldHolding h : holdings) { // loop each holding
            BigDecimal price = h.getBranch().getVendor().getCurrentGoldPrice(); // get price from vendor
            VirtualGoldResponse response = mapToResponse(h, price); // convert entity → response
            result.add(response); // add to result list
        }
        return result; // return final list
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private VendorBranch findBranchOrThrow(Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + branchId));
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

    //Frontend → Controller → Service → Repository → DB
    //                                 ↓
    //                           mapToResponse()
    //                                 ↓
    //                         Frontend (response)
}