package com.goldwallet.digitalgoldwallet.modules.transaction.repository;

import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByUserUserIdOrderByCreatedAtDesc(Long userId);
    List<TransactionHistory> findByBranchBranchIdOrderByCreatedAtDesc(Long branchId);
}
