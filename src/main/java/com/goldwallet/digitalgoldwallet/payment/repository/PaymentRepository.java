package com.goldwallet.digitalgoldwallet.payment.repository;


import com.goldwallet.digitalgoldwallet.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}