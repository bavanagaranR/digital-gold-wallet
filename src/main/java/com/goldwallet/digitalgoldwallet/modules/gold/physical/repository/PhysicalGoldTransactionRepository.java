package com.goldwallet.digitalgoldwallet.modules.gold.physical.repository;

import com.goldwallet.digitalgoldwallet.modules.gold.physical.entity.PhysicalGoldTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//service added
@Repository
public interface PhysicalGoldTransactionRepository extends JpaRepository<PhysicalGoldTransaction, Long> {
    List<PhysicalGoldTransaction> findByUserUserIdOrderByCreatedAtDesc(Long userId);
}
