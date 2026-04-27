package com.goldwallet.digitalgoldwallet.modules.user.controller;

// Common response wrapper for all APIs
import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
// Request DTOs (used to receive input JSON)
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.CreateAddressRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.CreateUserRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.UpdateAddressRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.UpdateUserRequest;
// Response DTOs (used to send output JSON)
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.AddressResponse;
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.UserResponse;
// Service layer contains business logic
import com.goldwallet.digitalgoldwallet.modules.user.service.UserService;
// Used for DTO validation annotations
import jakarta.validation.Valid;
// Dependency Injection
import org.springframework.beans.factory.annotation.Autowired;
// Pagination support
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// HTTP response handling
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// REST annotations
import org.springframework.web.bind.annotation.*;
// For wallet balance values
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;// Inject service object

    @PostMapping("/users")// Create new user
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {// Accepts JSON input and creates new user
        return ResponseEntity.status(HttpStatus.CREATED)// Return 201 Created
                .body(ApiResponse.success("User created successfully", userService.createUser(request)));
    }

    @GetMapping("/users/{id}")// Get single user by ID
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {// Read ID from URL
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

//    @GetMapping("/users/{id}") // Get single user by ID
//    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) { // Read ID from URL
//        return ResponseEntity.status(HttpStatus.OK) // Return 200 OK
//                .body(ApiResponse.success(userService.getUserById(id)));
//    }

    @GetMapping("/users")// Get all users with pagination
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(pageable)));
    }

    @PutMapping("/users/{id}")// Update user by ID
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("User updated successfully", userService.updateUser(id, request))
        );
    }

    @GetMapping("/users/{id}/balance")// Get wallet balance only
    public ResponseEntity<ApiResponse<BigDecimal>> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getWalletBalance(id)));
    }

    @PostMapping("/addresses")// Create new address
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address created successfully", userService.createAddress(request)));
    }

    @GetMapping("/addresses/{id}")// Get address by ID
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAddressById(id)));
    }

    @PutMapping("/addresses/{id}")// Update address by ID
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAddressRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Address updated successfully", userService.updateAddress(id, request))
        );
    }
}