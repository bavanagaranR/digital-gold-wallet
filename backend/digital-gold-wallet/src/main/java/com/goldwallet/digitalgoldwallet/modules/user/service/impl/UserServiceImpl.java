package com.goldwallet.digitalgoldwallet.modules.user.service.impl;

import com.goldwallet.digitalgoldwallet.common.exception.BusinessException;
import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.CreateAddressRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.CreateUserRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.UpdateAddressRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.UpdateUserRequest;
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.AddressResponse;
import com.goldwallet.digitalgoldwallet.modules.user.dto.response.UserResponse;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.user.service.UserService;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j// Enables logging using log.info(), log.error(), etc.
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional// If any error happens, DB changes will rollback
    // Check duplicate email before creating user
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already in use: " + request.getEmail());
        }

        // Fetch address using addressId from request
        Address address = null;
        if (request.getAddressId() != null) {
            address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + request.getAddressId()));//.orElseThrow->it is optional class,If value exists → give the value,If empty → throw exception
        }

        // Convert request DTO to User entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .address(address)
                .balance(BigDecimal.ZERO)
                .build();
        // Convert request DTO to User entity manually
//        User user = new User();
//
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setAddress(address);
//        user.setBalance(BigDecimal.ZERO);

// userId not set here
// Database will auto-generate during save()

        // Save user entity into database
        User saved = userRepository.save(user);
        log.info("Created user with ID: {}", saved.getUserId());

        // Convert saved entity to response DTO
        return mapToUserResponse(saved);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = findUserOrThrow(userId);
        return mapToUserResponse(user);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "userId")
        );

        return userRepository.findAll(sortedPageable)
                .map(this::mapToUserResponse);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = findUserOrThrow(userId);// Fetch existing user

        // Update only provided fields
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        // Check duplicate email before updating
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(user.getEmail()) &&
                    userRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException("Email already in use: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        // Update address if addressId is provided
        if (request.getAddressId() != null) {
            Address address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + request.getAddressId()));
            user.setAddress(address);
        }

        // Save updated user and return response
        return mapToUserResponse(userRepository.save(user));
    }

    @Override
    public BigDecimal getWalletBalance(Long userId) {
        return findUserOrThrow(userId).getBalance();
    }

    @Override
    @Transactional
    public AddressResponse createAddress(CreateAddressRequest request) {

        // Convert request DTO to Address entity
        Address address = Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .build();

//        Address address = new Address();
//        address.setStreet(request.getStreet());
//        address.setCity(request.getCity());
//        address.setState(request.getState());
//        address.setPostalCode(request.getPostalCode());
//        address.setCountry(request.getCountry());

        return mapToAddressResponse(addressRepository.save(address));
    }

    @Override
    public AddressResponse getAddressById(Long addressId) {
        return mapToAddressResponse(findAddressOrThrow(addressId));
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Long addressId, UpdateAddressRequest request) {
        Address address = findAddressOrThrow(addressId);

        // Update only provided fields
        if (request.getStreet() != null) {
            address.setStreet(request.getStreet());
        }
        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }
        if (request.getState() != null) {
            address.setState(request.getState());
        }
        if (request.getPostalCode() != null) {
            address.setPostalCode(request.getPostalCode());
        }
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }

        return mapToAddressResponse(addressRepository.save(address));
    }

    private User findUserOrThrow(Long userId) {
        // Common helper method to fetch user or throw not found error
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private Address findAddressOrThrow(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));
    }

    public UserResponse mapToUserResponse(User user) {
        // Convert User entity to UserResponse DTO
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .balance(user.getBalance())
                .address(user.getAddress() != null ? mapToAddressResponse(user.getAddress()) : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
//    UserResponse response = new UserResponse();
//response.setUserId(user.getUserId());
//response.setName(user.getName());
//response.setEmail(user.getEmail());
//response.setBalance(user.getBalance());
//response.setAddress(
//        user.getAddress() != null
//        ? mapToAddressResponse(user.getAddress())
//            : null
//            );
//response.setCreatedAt(user.getCreatedAt());
//return response;
//

    private AddressResponse mapToAddressResponse(Address address) {
        // Convert Address entity to AddressResponse DTO
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();

//        AddressResponse response = new AddressResponse();
//
//        response.setAddressId(address.getAddressId());
//        response.setStreet(address.getStreet());
//        response.setCity(address.getCity());
//        response.setState(address.getState());
//        response.setPostalCode(address.getPostalCode());
//        response.setCountry(address.getCountry());
//
//        return response;
    }
}