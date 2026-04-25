package com.goldwallet.digitalgoldwallet.modules.gold.virtual.service;

import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.BuyGoldRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.SellGoldRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.response.VirtualGoldResponse;

import java.util.List;

public interface VirtualGoldService {
    VirtualGoldResponse buyGold(BuyGoldRequest request);
    VirtualGoldResponse sellGold(SellGoldRequest request);
    List<VirtualGoldResponse> getUserGoldHoldings(Long userId);
    List<VirtualGoldResponse> getBranchGoldHoldings(Long branchId);
}