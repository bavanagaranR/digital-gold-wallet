package com.goldwallet.digitalgoldwallet.modules.user.repository;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
