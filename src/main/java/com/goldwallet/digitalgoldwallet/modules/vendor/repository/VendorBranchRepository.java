package com.goldwallet.digitalgoldwallet.modules.vendor.repository;

import com.goldwallet.digitalgoldwallet.modules.vendor.entity.VendorBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VendorBranchRepository extends JpaRepository<VendorBranch, Long> {
    List<VendorBranch> findByVendorVendorId(Long vendorId);

    @Query("SELECT vb FROM VendorBranch vb WHERE vb.quantity > :qty")
    List<VendorBranch> findBranchesWithQuantityGreaterThan(@Param("qty") BigDecimal qty);

    @Query("SELECT vb FROM VendorBranch vb WHERE vb.address.city = :city")
    List<VendorBranch> findBranchesByCity(@Param("city") String city);

    @Query("SELECT SUM(vb.quantity) FROM VendorBranch vb WHERE vb.vendor.vendorId = :vendorId")
    BigDecimal getTotalGoldByVendor(@Param("vendorId") Long vendorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM VendorBranch vb WHERE vb.vendor.vendorId = :vendorId")
    void deleteByVendorId(@Param("vendorId") Long vendorId);
}