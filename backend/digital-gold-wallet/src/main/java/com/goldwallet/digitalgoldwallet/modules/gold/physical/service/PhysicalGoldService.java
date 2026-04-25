package com.goldwallet.digitalgoldwallet.modules.gold.physical.service;

import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.request.ConvertToPhysicalRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.response.PhysicalGoldResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PhysicalGoldService {
    PhysicalGoldResponse convertToPhysical(ConvertToPhysicalRequest request);
    Page<PhysicalGoldResponse> getUserPhysicalGold(Long userId, Pageable pageable);
}