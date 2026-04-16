package com.goldwallet.digitalgoldwallet.modules.transaction.repository;

import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
}
