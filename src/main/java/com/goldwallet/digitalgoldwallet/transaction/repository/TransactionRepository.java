package com.goldwallet.digitalgoldwallet.transaction.repository;

import com.goldwallet.digitalgoldwallet.transaction.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionHistory, Long> {
}
