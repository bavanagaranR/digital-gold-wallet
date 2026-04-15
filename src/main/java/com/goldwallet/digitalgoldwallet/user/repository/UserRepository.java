package com.goldwallet.digitalgoldwallet.user.repository;

import com.goldwallet.digitalgoldwallet.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
