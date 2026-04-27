package com.goldwallet.digitalgoldwallet.modules.gold.virtual.controller;

import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.BuyGoldRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.request.SellGoldRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.dto.response.VirtualGoldResponse;
import com.goldwallet.digitalgoldwallet.modules.gold.virtual.service.VirtualGoldService;
import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  //marks this class as spring mvc controller, Handles response → JSON
@Validated
public class VirtualGoldController {

    @Autowired      //field injection
    private VirtualGoldService virtualGoldService;

    @PostMapping("/api/v1/gold/virtual/buy")     //Used for creating data, alternate: @RequestMapping(value = "/api/v1/gold/virtual/buy", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<VirtualGoldResponse>> buyGold(
            @Valid @RequestBody BuyGoldRequest request) {  //@RequestBody: Handles JSON → request object, @Valid: Triggers validation like notnull
        return ResponseEntity.ok(
                ApiResponse.success("Gold purchased successfully",
                        virtualGoldService.buyGold(request)));
    }

    @PostMapping("/api/v1/gold/virtual/sell")
    public ResponseEntity<ApiResponse<VirtualGoldResponse>> sellGold(  //ResponseEntity is used to customize the full HTTP response, including body, status code, and headers.
            @Valid @RequestBody SellGoldRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Gold sold successfully",
                        virtualGoldService.sellGold(request)));
    }

    @GetMapping("/api/v1/users/{userId}/gold/virtual")
    public ResponseEntity<ApiResponse<List<VirtualGoldResponse>>> getUserHoldings(
            @PathVariable @Min(value = 1, message = "UserID should be greater than 0") Long userId) {           //“Return a list of user gold holdings wrapped inside ApiResponse, with HTTP response control”
        return ResponseEntity.ok(                  //PathVariable: Extracts value from URL
                ApiResponse.success(virtualGoldService.getUserGoldHoldings(userId)));
    }

    @GetMapping("/api/v1/branches/{branchId}/gold/virtual")
    public ResponseEntity<ApiResponse<List<VirtualGoldResponse>>> getBranchHoldings(
            @PathVariable @Min(value = 1, message = "BranchID should be greater than 0") Long branchId) {
        return ResponseEntity.ok(
                ApiResponse.success(virtualGoldService.getBranchGoldHoldings(branchId)));
    }
}