package com.goldwallet.digitalgoldwallet.modules.gold.physical.service;

import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.request.ConvertToPhysicalRequest;
import com.goldwallet.digitalgoldwallet.modules.gold.physical.dto.response.PhysicalGoldResponse;

import java.util.List;

public interface PhysicalGoldService {
    PhysicalGoldResponse convertToPhysical(ConvertToPhysicalRequest request);
    List<PhysicalGoldResponse> getUserPhysicalGold(Long userId);
}