package com.goldwallet.digitalgoldwallet.modules.user.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.CreateAddressRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.CreateUserRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.UpdateAddressRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.UpdateUserRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.AddressResponse;
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.UserResponse;
import com.goldwallet.digitalgoldwallet.modules.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", userService.createUser(request)));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(pageable)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("User updated successfully", userService.updateUser(id, request))
        );
    }

    @GetMapping("/users/{id}/balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getWalletBalance(id)));
    }

    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address created successfully", userService.createAddress(request)));
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAddressById(id)));
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAddressRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Address updated successfully", userService.updateAddress(id, request))
        );
    }
}