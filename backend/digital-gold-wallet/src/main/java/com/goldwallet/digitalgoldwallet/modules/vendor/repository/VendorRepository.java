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
@Repository // Marks this interface as a Spring Data JPA repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {


    //---------------------- EXIST CHECK ------------------

    // Checks if vendor name exists (case-insensitive)
    boolean existsByVendorNameIgnoreCase(String vendorName);


    // Checks if email already exists
    boolean existsByContactEmail(String contactEmail);


    //---------- SELECT (CASE-INSENSITIVE) ------------------------

    // Custom query to find vendor by name ignoring case
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.vendorName) = LOWER(:name)")
    Optional<Vendor> findByNameIgnoreCase(@Param("name") String name);


    //---------- SELECT (FILTER BY PRICE) ---------------------------

    // Returns vendors whose gold price is greater than given value
    @Query("SELECT v FROM Vendor v WHERE v.currentGoldPrice > :price")
    List<Vendor> findVendorsWithGoldPriceGreaterThan(@Param("price") BigDecimal price);


    //-------------------- UPDATE --------------------------------------

    // Updates gold price for a specific vendor
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Vendor v SET v.currentGoldPrice = :price WHERE v.vendorId = :id")
    int updateGoldPrice(@Param("id") Long id, @Param("price") BigDecimal price);


    //-------------------- DELETE --------------------------------------

    // Deletes vendor by ID using custom query
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE FROM Vendor v WHERE v.vendorId = :id")
    void deleteVendorById(@Param("id") Long id);
}