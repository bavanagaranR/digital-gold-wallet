package com.goldwallet.digitalgoldwallet.modules.vendor.repository;

import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorBranchRepository extends JpaRepository<VendorBranch, Long> {
    List<VendorBranch> findByVendorVendorId(Long vendorId);
}
