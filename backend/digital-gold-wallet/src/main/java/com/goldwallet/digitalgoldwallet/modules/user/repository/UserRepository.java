package com.goldwallet.digitalgoldwallet.modules.user.repository;

import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {// save(user) -> takes User object,// findById(1L) -> takes Long id

    // EXISTING
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // ---------- CUSTOM READ ----------

    @Query("SELECT u FROM User u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<User> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.balance > :amount")
    List<User> findUsersWithBalanceGreaterThan(@Param("amount") BigDecimal amount);

    @Query("SELECT u FROM User u WHERE u.address.city = :city")
    List<User> findUsersByCity(@Param("city") String city);

    // ---------- UPDATE ----------

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE User u SET u.balance = :balance WHERE u.userId = :id")
    int updateUserBalance(@Param("id") Long id, @Param("balance") BigDecimal balance);

    // ---------- DELETE ----------

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM User u WHERE u.userId = :id")
    int deleteUserByIdCustom(@Param("id") Long id);

    // ---------- AGGREGATION ----------

    @Query("SELECT COUNT(u) FROM User u WHERE u.address.city = :city")
    long countUsersByCity(@Param("city") String city);
}

