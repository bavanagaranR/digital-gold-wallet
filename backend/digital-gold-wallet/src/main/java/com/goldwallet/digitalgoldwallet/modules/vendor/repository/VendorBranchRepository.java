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

@Repository // Marks this interface as a Spring Data repository (DAO layer)
public interface VendorBranchRepository extends JpaRepository<VendorBranch, Long> {

    // Finds all branches belonging to a specific vendor using vendorId
    List<VendorBranch> findByVendorVendorId(Long vendorId);


    // Custom query to get branches where quantity is greater than given value
    @Query("SELECT vb FROM VendorBranch vb WHERE vb.quantity > :qty")
    List<VendorBranch> findBranchesWithQuantityGreaterThan(@Param("qty") BigDecimal qty);


    // Custom query to get branches based on city (via Address entity)
    @Query("SELECT vb FROM VendorBranch vb WHERE vb.address.city = :city")
    List<VendorBranch> findBranchesByCity(@Param("city") String city);



    // Returns total gold quantity of a vendor (SUM of all branch quantities)
    // COALESCE ensures it returns 0 instead of null if no records exist
    @Query("SELECT COALESCE(SUM(vb.quantity), 0) FROM VendorBranch vb WHERE vb.vendor.vendorId = :vendorId")
    BigDecimal getTotalGoldByVendor(@Param("vendorId") Long vendorId);



    // Deletes all branches of a specific vendor
    @Modifying // Required for update/delete queries
    @Transactional // Ensures operation runs inside a transaction
    @Query("DELETE FROM VendorBranch vb WHERE vb.vendor.vendorId = :vendorId")
    void deleteByVendorId(@Param("vendorId") Long vendorId);
}