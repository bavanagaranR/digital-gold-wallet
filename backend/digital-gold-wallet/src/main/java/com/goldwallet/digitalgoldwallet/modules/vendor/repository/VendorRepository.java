package com.goldwallet.digitalgoldwallet.modules.vendor.repository;

import com.goldwallet.digitalgoldwallet.modules.vendor.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByVendorName(String vendorName);

    boolean existsByVendorNameIgnoreCase(String vendorName);
    
    boolean existsByContactEmail(String contactEmail);

    @Query("SELECT v FROM Vendor v WHERE LOWER(v.vendorName) = LOWER(:name)")
    Optional<Vendor> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT v FROM Vendor v WHERE v.currentGoldPrice > :price")
    List<Vendor> findVendorsWithGoldPriceGreaterThan(@Param("price") BigDecimal price);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Vendor v SET v.currentGoldPrice = :price WHERE v.vendorId = :id")
    int updateGoldPrice(@Param("id") Long id, @Param("price") BigDecimal price);

    // Optional custom delete
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE FROM Vendor v WHERE v.vendorId = :id")
    void deleteVendorById(@Param("id") Long id);
}

