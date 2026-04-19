package com.goldwallet.digitalgoldwallet.modules.gold.virtual.controller;

import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.BuyGoldRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.SellGoldRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.response.VirtualGoldResponse;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.service.VirtualGoldService;
import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VirtualGoldController {

    @Autowired
    private VirtualGoldService virtualGoldService;

    @PostMapping("/api/v1/gold/virtual/buy")
    public ResponseEntity<ApiResponse<VirtualGoldResponse>> buyGold(
            @Valid @RequestBody BuyGoldRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Gold purchased successfully",
                        virtualGoldService.buyGold(request)));
    }

    @PostMapping("/api/v1/gold/virtual/sell")
    public ResponseEntity<ApiResponse<VirtualGoldResponse>> sellGold(
            @Valid @RequestBody SellGoldRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Gold sold successfully",
                        virtualGoldService.sellGold(request)));
    }

    @GetMapping("/api/v1/users/{userId}/gold/virtual")
    public ResponseEntity<ApiResponse<List<VirtualGoldResponse>>> getUserHoldings(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                ApiResponse.success(virtualGoldService.getUserGoldHoldings(userId)));
    }

    @GetMapping("/api/v1/branches/{branchId}/gold/virtual")
    public ResponseEntity<ApiResponse<List<VirtualGoldResponse>>> getBranchHoldings(
            @PathVariable Long branchId) {
        return ResponseEntity.ok(
                ApiResponse.success(virtualGoldService.getBranchGoldHoldings(branchId)));
    }
}