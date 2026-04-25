package com.goldwallet.digitalgoldwallet.modules.transaction.repository;

import com.goldwallet.digitalgoldwallet.modules.transaction.entity.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    // EXISTING
    Page<TransactionHistory> findByUserUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<TransactionHistory> findByBranchBranchIdOrderByCreatedAtDesc(Long branchId, Pageable pageable);

    // ---------- CUSTOM READ ----------

    @Query("SELECT t FROM TransactionHistory t WHERE t.transactionType = :type")
    Page<TransactionHistory> findByTransactionType(@Param("type") TransactionHistory.TransactionType type, Pageable pageable);

    @Query("SELECT t FROM TransactionHistory t WHERE t.transactionStatus = :status")
    Page<TransactionHistory> findByStatus(@Param("status") TransactionHistory.TransactionStatus status, Pageable pageable);

    @Query("SELECT t FROM TransactionHistory t WHERE t.amount >= :amount")
    Page<TransactionHistory> findTransactionsGreaterThan(@Param("amount") BigDecimal amount, Pageable pageable);

    // ---------- UPDATE ----------

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE TransactionHistory t SET t.transactionStatus = :status WHERE t.transactionId = :id")
    int updateTransactionStatus(@Param("id") Long id,
                                @Param("status") TransactionHistory.TransactionStatus status);

    // ---------- DELETE ----------

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM TransactionHistory t WHERE t.transactionId = :id")
    int deleteTransactionByIdCustom(@Param("id") Long id);

    // ---------- AGGREGATION ----------

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionHistory t WHERE t.user.userId = :userId")
    BigDecimal getTotalTransactionAmountByUser(@Param("userId") Long userId);
}

