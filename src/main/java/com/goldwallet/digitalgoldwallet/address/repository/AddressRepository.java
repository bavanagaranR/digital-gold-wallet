package com.goldwallet.digitalgoldwallet.address.repository;

import com.goldwallet.digitalgoldwallet.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
