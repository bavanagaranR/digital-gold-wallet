package com.goldwallet.digitalgoldwallet.modules.payment.repository;

import com.goldwallet.digitalgoldwallet.modules.payment.entity.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // EXISTING
    List<Payment> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    // ---------- CUSTOM READ ----------

    @Query("SELECT p FROM Payment p WHERE p.paymentStatus = :status")
    List<Payment> findByStatus(@Param("status") Payment.PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.transactionType = :type")
    List<Payment> findByTransactionType(@Param("type") Payment.TransactionType type);

    @Query("SELECT p FROM Payment p WHERE p.amount > :amount")
    List<Payment> findPaymentsGreaterThan(@Param("amount") BigDecimal amount);

    // ---------- UPDATE ----------

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Payment p SET p.paymentStatus = :status WHERE p.paymentId = :id")
    int updatePaymentStatus(@Param("id") Long id,
                            @Param("status") Payment.PaymentStatus status);

    // ---------- DELETE ----------

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Payment p WHERE p.paymentId = :id")
    int deletePaymentByIdCustom(@Param("id") Long id);

    // ---------- AGGREGATION ----------

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.user.userId = :userId")
    BigDecimal getTotalPaymentByUser(@Param("userId") Long userId);
}

