package com.goldwallet.digitalgoldwallet.modules.user.service;

import com.goldwallet.digitalgoldwallet.modules.user.dto.request.CreateAddressRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.CreateUserRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.UpdateAddressRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.UpdateUserRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.AddressResponse;
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

//service interface

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUserById(Long userId);
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse updateUser(Long userId, UpdateUserRequest request);
    BigDecimal getWalletBalance(Long userId);
    AddressResponse createAddress(CreateAddressRequest request);
    AddressResponse getAddressById(Long addressId);
    AddressResponse updateAddress(Long addressId, UpdateAddressRequest request);
}
