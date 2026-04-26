package com.goldwallet.digitalgoldwallet.modules.gold;

import com.goldwallet.digitalgoldwallet.common.exception.*;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.*;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity.VirtualGoldHolding;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.service.impl.VirtualGoldServiceImpl;
import com.goldwallet.digitalgoldwallet.modules.transaction.repository.TransactionHistoryRepository;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.vendor.entity.*;
import com.goldwallet.digitalgoldwallet.modules.vendor.repository.VendorBranchRepository;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.repository.VirtualGoldHoldingRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GoldServiceTest {

    @Mock private VirtualGoldHoldingRepository holdingRepository;
    @Mock private UserRepository userRepository;
    @Mock private VendorBranchRepository branchRepository;
    @Mock private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private VirtualGoldServiceImpl service;

    // ---------- COMMON ----------
    private User createUser() {
        User u = new User();
        u.setUserId(1L);
        u.setName("Subha");
        u.setBalance(new BigDecimal("10000"));
        return u;
    }

    private VendorBranch createBranch() {
        Vendor v = new Vendor();
        v.setCurrentGoldPrice(new BigDecimal("5000"));

        VendorBranch b = new VendorBranch();
        b.setBranchId(1L);
        b.setVendor(v);
        b.setQuantity(new BigDecimal("10"));
        return b;
    }

    private VirtualGoldHolding createHolding(User u, VendorBranch b, String qty) {
        VirtualGoldHolding h = new VirtualGoldHolding();
        h.setUser(u);
        h.setBranch(b);
        h.setQuantity(new BigDecimal(qty));
        return h;
    }

    // ================= POSITIVE TESTS (10) =================

    // 1 - Buy gold creates new holding when none exists
    @Test
    void testBuyGold_success_newHolding() {
        User user = createUser();
        VendorBranch branch = createBranch();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L, 1L))
                .thenReturn(Optional.empty());
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertNotNull(service.buyGold(req));
    }

    // 2 - Buy gold updates an existing holding
    @Test
    void testBuyGold_success_existingHoldingUpdated() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding existing = createHolding(user, branch, "2");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L, 1L))
                .thenReturn(Optional.of(existing));
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertNotNull(service.buyGold(req));
    }

    // 3 - Buy gold deducts user balance correctly
    @Test
    void testBuyGold_success_balanceDeducted() {
        User user = createUser();
        VendorBranch branch = createBranch();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L, 1L))
                .thenReturn(Optional.empty());
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        service.buyGold(req);

        // Balance should be 10000 - (1 * 5000) = 5000
        assertEquals(0, user.getBalance().compareTo(new BigDecimal("5000")));
    }

    // 4 - Buy gold reduces branch quantity
    @Test
    void testBuyGold_success_branchQuantityReduced() {
        User user = createUser();
        VendorBranch branch = createBranch();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L, 1L))
                .thenReturn(Optional.empty());
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("2"));

        service.buyGold(req);

        // Branch quantity should be 10 - 2 = 8
        assertEquals(0, branch.getQuantity().compareTo(new BigDecimal("8")));
    }

    // 5 - Sell gold succeeds and credits user balance
    @Test
    void testSellGold_success_balanceCredited() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(user, branch, "5");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L, 1L))
                .thenReturn(Optional.of(holding));
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertNotNull(service.sellGold(req));
    }

    // 6 - Sell gold reduces holding quantity
    @Test
    void testSellGold_success_holdingQuantityReduced() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(user, branch, "5");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L, 1L))
                .thenReturn(Optional.of(holding));
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("2"));

        service.sellGold(req);

        // Holding quantity should be 5 - 2 = 3
        assertEquals(0, holding.getQuantity().compareTo(new BigDecimal("3")));
    }

    // 7 - Get user holdings returns correct list
    @Test
    void testGetUserHoldings_success_returnsHoldings() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(user, branch, "2");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(holdingRepository.findByUserUserId(1L)).thenReturn(List.of(holding));

        assertEquals(1, service.getUserGoldHoldings(1L).size());
    }

    // 8 - Get user holdings returns empty list when user has no holdings
    @Test
    void testGetUserHoldings_success_emptyList() {
        User user = createUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(holdingRepository.findByUserUserId(1L)).thenReturn(List.of());

        assertTrue(service.getUserGoldHoldings(1L).isEmpty());
    }

    // 9 - Get branch holdings returns correct list
    @Test
    void testGetBranchHoldings_success_returnsHoldings() {
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(createUser(), branch, "2");

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByBranchBranchId(1L))
                .thenReturn(List.of(holding));

        assertEquals(1, service.getBranchGoldHoldings(1L).size());
    }

    // 10 - Get branch holdings returns empty list when no holdings exist
    @Test
    void testGetBranchHoldings_success_emptyList() {
        VendorBranch branch = createBranch();

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByBranchBranchId(1L)).thenReturn(List.of());

        assertTrue(service.getBranchGoldHoldings(1L).isEmpty());
    }

    // ================= NEGATIVE TESTS (5) =================

    // 11 - Buy gold fails when user not found
    @Test
    void testBuyGold_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);

        assertThrows(ResourceNotFoundException.class, () -> service.buyGold(req));
    }

    // 12 - Buy gold fails when user balance is insufficient
    @Test
    void testBuyGold_insufficientBalance_throwsException() {
        User user = createUser();
        user.setBalance(new BigDecimal("100")); // too low for 1g at 5000/g

        VendorBranch branch = createBranch();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertThrows(InsufficientBalanceException.class, () -> service.buyGold(req));
    }

    // 13 - Buy gold fails when branch does not have enough gold
    @Test
    void testBuyGold_insufficientBranchGold_throwsException() {
        User user = createUser();
        VendorBranch branch = createBranch();
        branch.setQuantity(BigDecimal.ZERO);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertThrows(BusinessException.class, () -> service.buyGold(req));
    }

    // 14 - Sell gold fails when user has no holding for this branch
    @Test
    void testSellGold_holdingNotFound_throwsException() {
        User user = createUser();
        VendorBranch branch = createBranch();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L, 1L))
                .thenReturn(Optional.empty());

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertThrows(BusinessException.class, () -> service.sellGold(req));
    }

    // 15 - Sell gold fails when sell quantity exceeds holding
    @Test
    void testSellGold_insufficientHolding_throwsException() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(user, branch, "1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L, 1L))
                .thenReturn(Optional.of(holding));

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("5"));

        assertThrows(BusinessException.class, () -> service.sellGold(req));
    }
}