package com.goldwallet.digitalgoldwallet.transaction.repository;

import com.goldwallet.digitalgoldwallet.transaction.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionHistory,String> {
    List<TransactionHistory> findByUser_UserId(String userId);
    List<TransactionHistory> findByUser_UserIdOrderByCreatedAtDesc(String userId);
    List<TransactionHistory> findByTransactionType(String type);
    List<TransactionHistory> findByTransactionStatus(String status);
}
