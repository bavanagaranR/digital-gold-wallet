package com.goldwallet.digitalgoldwallet.gold.repository;
import com.goldwallet.digitalgoldwallet.gold.entity.VirtualGoldHolding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualGoldRepository extends JpaRepository<VirtualGoldHolding, Long> {
}
