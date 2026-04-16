package com.goldwallet.digitalgoldwallet.modules.vendor.repository;

import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
