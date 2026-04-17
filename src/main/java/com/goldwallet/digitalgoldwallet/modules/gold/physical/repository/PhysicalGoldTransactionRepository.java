package com.goldwallet.digitalgoldwallet.modules.gold.physical.repository;

import com.goldwallet.digitalgoldwallet.modules.gold.physical.entity.PhysicalGoldTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//service added
@Repository
public interface PhysicalGoldTransactionRepository extends JpaRepository<PhysicalGoldTransaction, Long> {

    List<PhysicalGoldTransaction> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    List<PhysicalGoldTransaction> findByBranchBranchId(Long branchId);

    Optional<PhysicalGoldTransaction> findByUserUserIdAndBranchBranchId(Long userId, Long branchId);

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM PhysicalGoldTransaction p WHERE p.user.userId = :userId")
    BigDecimal getTotalPhysicalGoldByUser(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM PhysicalGoldTransaction p WHERE p.transactionId = :id")
    int deleteTransactionById(@Param("id") Long id);
}
