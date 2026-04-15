package com.goldwallet.digitalgoldwallet.gold.repository;

import com.goldwallet.digitalgoldwallet.gold.entity.PhysicalGoldTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalGoldRepository extends JpaRepository<PhysicalGoldTransaction, Long> {
}
