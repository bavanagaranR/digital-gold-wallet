package com.goldwallet.digitalgoldwallet.modules.user.repository;

import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.city = :city")
    List<Address> findByCity(@Param("city") String city);
}
