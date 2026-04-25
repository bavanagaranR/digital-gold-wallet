package com.goldwallet.digitalgoldwallet.modules.gold.virtual.repository;

import com.goldwallet.digitalgoldwallet.modules.gold.virtual.entity.VirtualGoldHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VirtualGoldHoldingRepository extends JpaRepository<VirtualGoldHolding, Long> {

    // AUTO DERIVED (fix for your error)
    List<VirtualGoldHolding> findByUserUserId(Long userId);

    List<VirtualGoldHolding> findByBranchBranchId(Long branchId);

    Optional<VirtualGoldHolding> findByUserUserIdAndBranchBranchId(Long userId, Long branchId);

    // CUSTOM QUERIES
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE VirtualGoldHolding v SET v.quantity = :qty WHERE v.holdingId = :id")
    int updateHoldingQuantity(@Param("id") Long id, @Param("qty") BigDecimal qty);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM VirtualGoldHolding v WHERE v.holdingId = :id")
    int deleteHoldingById(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(v.quantity), 0) FROM VirtualGoldHolding v WHERE v.user.userId = :userId")
    BigDecimal getTotalGoldByUser(@Param("userId") Long userId);
}
