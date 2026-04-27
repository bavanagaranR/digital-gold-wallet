package com.goldwallet.digitalgoldwallet.modules.gold.physical.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.request.ConvertToPhysicalRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.response.PhysicalGoldResponse;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.service.PhysicalGoldService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@Validated
public class PhysicalGoldController {

    @Autowired
    private PhysicalGoldService physicalGoldService;

    @PostMapping("/api/v1/gold/physical/convert")
    public ResponseEntity<ApiResponse<PhysicalGoldResponse>> convertToPhysical(
            @Valid @RequestBody ConvertToPhysicalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Gold converted to physical successfully",
                        physicalGoldService.convertToPhysical(request)));
    }

    @GetMapping("/api/v1/users/{userId}/gold/physical")
    public ResponseEntity<ApiResponse<Page<PhysicalGoldResponse>>> getUserPhysicalGold(
            @PathVariable @Min(value = 1, message = "UserID should be greater than 0") Long userId, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(physicalGoldService.getUserPhysicalGold(userId, pageable)));
    }
}