package com.goldwallet.digitalgoldwallet.gold.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalGoldRepository extends JpaRepository<PhysicalGoldTransaction, Long> {
}

