package com.goldwallet.digitalgoldwallet.modules.gold.virtual.repository;

import com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity.VirtualGoldHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VirtualGoldHoldingRepository extends JpaRepository<VirtualGoldHolding, Long> {
    List<VirtualGoldHolding> findByUserUserId(Long userId);
    List<VirtualGoldHolding> findByBranchBranchId(Long branchId);
    Optional<VirtualGoldHolding> findByUserUserIdAndBranchBranchId(Long userId, Long branchId);
}
