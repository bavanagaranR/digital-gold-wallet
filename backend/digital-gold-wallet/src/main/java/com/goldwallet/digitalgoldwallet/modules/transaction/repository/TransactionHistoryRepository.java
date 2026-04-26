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

// Repository interface for all database operations on transaction_history table
// Extends JpaRepository to get built-in CRUD and pagination support
@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    // Fetches all transactions for a specific user, sorted by most recent first (descending by createdAt)
    Page<TransactionHistory> findByUserUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // Fetches all transactions processed at a specific vendor branch, sorted by most recent first
    Page<TransactionHistory> findByBranchBranchIdOrderByCreatedAtDesc(Long branchId, Pageable pageable);

    // ---------- CUSTOM READ ----------

    // Finds all transactions matching a given transaction type (BUY / SELL / CONVERT_TO_PHYSICAL)
    @Query("SELECT t FROM TransactionHistory t WHERE t.transactionType = :type")
    Page<TransactionHistory> findByTransactionType(@Param("type") TransactionHistory.TransactionType type, Pageable pageable);

    // Finds all transactions matching a given status (SUCCESS / FAILED)
    @Query("SELECT t FROM TransactionHistory t WHERE t.transactionStatus = :status")
    Page<TransactionHistory> findByStatus(@Param("status") TransactionHistory.TransactionStatus status, Pageable pageable);

    // Finds all transactions where the amount is greater than or equal to the given threshold
    @Query("SELECT t FROM TransactionHistory t WHERE t.amount >= :amount")
    Page<TransactionHistory> findTransactionsGreaterThan(@Param("amount") BigDecimal amount, Pageable pageable);

    // ---------- UPDATE ----------

    // Updates the status of a specific transaction by ID — clears persistence context after execution
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE TransactionHistory t SET t.transactionStatus = :status WHERE t.transactionId = :id")
    int updateTransactionStatus(@Param("id") Long id,
                                @Param("status") TransactionHistory.TransactionStatus status);

    // ---------- DELETE ----------

    // Deletes a specific transaction record by ID using a custom JPQL query
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM TransactionHistory t WHERE t.transactionId = :id")
    int deleteTransactionByIdCustom(@Param("id") Long id);

    // ---------- AGGREGATION ----------

    // Calculates the total transaction amount for a user — returns 0 if no transactions exist (COALESCE)
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionHistory t WHERE t.user.userId = :userId")
    BigDecimal getTotalTransactionAmountByUser(@Param("userId") Long userId);
}
