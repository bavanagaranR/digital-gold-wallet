package com.goldwallet.digitalgoldwallet.modules.gold.physical.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.request.ConvertToPhysicalRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.response.PhysicalGoldResponse;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.service.PhysicalGoldService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PhysicalGoldController {

    private final PhysicalGoldService physicalGoldService;

    @PostMapping("/api/v1/gold/physical/convert")
    public ResponseEntity<ApiResponse<PhysicalGoldResponse>> convertToPhysical(
            @Valid @RequestBody ConvertToPhysicalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Gold converted to physical successfully", physicalGoldService.convertToPhysical(request)));
    }

    @GetMapping("/api/v1/users/{userId}/gold/physical")
    public ResponseEntity<ApiResponse<List<PhysicalGoldResponse>>> getUserPhysicalGold(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(physicalGoldService.getUserPhysicalGold(userId)));
    }
}
