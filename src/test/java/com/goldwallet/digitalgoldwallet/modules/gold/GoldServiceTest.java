package com.goldwallet.digitalgoldwallet.modules.gold;

import com.goldwallet.digitalgoldwallet.common.exception.*;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.*;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity.VirtualGoldHolding;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.service.imp.VirtualGoldServiceImpl;
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

    // ================= BUY =================

    @Test
    void testBuyGold_success() {
        User user = createUser();
        VendorBranch branch = createBranch();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L,1L))
                .thenReturn(Optional.empty());
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertNotNull(service.buyGold(req));
    }

    @Test
    void testBuyGold_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);

        assertThrows(ResourceNotFoundException.class, () -> service.buyGold(req));
    }

    @Test
    void testBuyGold_branchNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(createUser()));
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);

        assertThrows(ResourceNotFoundException.class, () -> service.buyGold(req));
    }

    @Test
    void testBuyGold_insufficientBalance() {
        User user = createUser();
        user.setBalance(new BigDecimal("100"));

        VendorBranch branch = createBranch();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertThrows(InsufficientBalanceException.class, () -> service.buyGold(req));
    }

    @Test
    void testBuyGold_insufficientBranchGold() {
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

    @Test
    void testBuyGold_existingHoldingUpdate() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding existing = createHolding(user, branch, "2");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L,1L))
                .thenReturn(Optional.of(existing));
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BuyGoldRequest req = new BuyGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertNotNull(service.buyGold(req));
    }

    // ================= SELL =================

    @Test
    void testSellGold_success() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(user, branch, "5");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L,1L))
                .thenReturn(Optional.of(holding));
        when(holdingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertNotNull(service.sellGold(req));
    }

    @Test
    void testSellGold_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);

        assertThrows(ResourceNotFoundException.class, () -> service.sellGold(req));
    }

    @Test
    void testSellGold_branchNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(createUser()));
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);

        assertThrows(ResourceNotFoundException.class, () -> service.sellGold(req));
    }

    @Test
    void testSellGold_holdingNotFound() {
        User user = createUser();
        VendorBranch branch = createBranch();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L,1L))
                .thenReturn(Optional.empty());

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("1"));

        assertThrows(BusinessException.class, () -> service.sellGold(req));
    }

    @Test
    void testSellGold_insufficientHolding() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(user, branch, "1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByUserUserIdAndBranchBranchId(1L,1L))
                .thenReturn(Optional.of(holding));

        SellGoldRequest req = new SellGoldRequest();
        req.setUserId(1L);
        req.setBranchId(1L);
        req.setQuantity(new BigDecimal("5"));

        assertThrows(BusinessException.class, () -> service.sellGold(req));
    }

    // ================= FETCH =================

    @Test
    void testGetUserHoldings_success() {
        User user = createUser();
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(user, branch, "2");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(holdingRepository.findByUserUserId(1L)).thenReturn(List.of(holding));

        assertEquals(1, service.getUserGoldHoldings(1L).size());
    }

    @Test
    void testGetUserHoldings_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getUserGoldHoldings(1L));
    }

    @Test
    void testGetBranchHoldings_success() {
        VendorBranch branch = createBranch();
        VirtualGoldHolding holding = createHolding(createUser(), branch, "2");

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(holdingRepository.findByBranchBranchId(1L))
                .thenReturn(List.of(holding));

        assertEquals(1, service.getBranchGoldHoldings(1L).size());
    }

    @Test
    void testGetBranchHoldings_branchNotFound() {
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getBranchGoldHoldings(1L));
    }
}