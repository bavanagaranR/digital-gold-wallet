package com.goldwallet.digitalgoldwallet.modules.user;

import com.goldwallet.digitalgoldwallet.common.exception.BusinessException;
import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.user.dto.request.*;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import com.goldwallet.digitalgoldwallet.modules.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // ================= POSITIVE TESTS (10) =================

    // 1 - Create user successfully
    @Test
    void testCreateUser_success_returnsUserWithName() {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Cathy");
        req.setEmail("cathy@test.com");

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(
                User.builder().userId(1L).name("Cathy").email("cathy@test.com").build());

        assertEquals("Cathy", userService.createUser(req).getName());
    }

    // 2 - Create user with address links the address
    @Test
    void testCreateUser_withAddress_success() {
        CreateUserRequest req = new CreateUserRequest();
        req.setEmail("test@mail.com");
        req.setAddressId(1L);

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(new Address()));
        when(userRepository.save(any())).thenReturn(User.builder().userId(1L).build());

        assertNotNull(userService.createUser(req));
    }

    // 3 - Get user by ID returns correct user
    @Test
    void testGetUserById_success_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(
                Optional.of(User.builder().userId(1L).build()));

        assertEquals(1L, userService.getUserById(1L).getUserId());
    }

    // 4 - Get all users returns paginated list
    @Test
    void testGetAllUsers_success_returnsPage() {
        Page<User> page = new PageImpl<>(
                java.util.List.of(User.builder().userId(1L).build()));

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        assertEquals(1, userService.getAllUsers(PageRequest.of(0, 10)).getContent().size());
    }

    // 5 - Update user name successfully
    @Test
    void testUpdateUser_success_nameUpdated() {
        User user = User.builder().userId(1L).email("old@mail.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UpdateUserRequest req = new UpdateUserRequest();
        req.setName("Updated");

        assertEquals("Updated", userService.updateUser(1L, req).getName());
    }

    // 6 - Get wallet balance returns correct value
    @Test
    void testGetWalletBalance_success_returnsBalance() {
        User user = User.builder().balance(new BigDecimal("500")).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(new BigDecimal("500"), userService.getWalletBalance(1L));
    }

    // 7 - Create address returns non-null response
    @Test
    void testCreateAddress_success_returnsAddress() {
        when(addressRepository.save(any())).thenReturn(new Address());

        assertNotNull(userService.createAddress(new CreateAddressRequest()));
    }

    // 8 - Get address by ID returns correct address
    @Test
    void testGetAddressById_success_returnsAddress() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(new Address()));

        assertNotNull(userService.getAddressById(1L));
    }

    // 9 - Update address successfully
    @Test
    void testUpdateAddress_success_returnsUpdatedAddress() {
        Address address = new Address();

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any())).thenReturn(address);

        assertNotNull(userService.updateAddress(1L, new UpdateAddressRequest()));
    }

    // 10 - Get all users returns empty page when no users exist
    @Test
    void testGetAllUsers_success_emptyPage() {
        Page<User> emptyPage = new PageImpl<>(java.util.List.of());

        when(userRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertTrue(userService.getAllUsers(PageRequest.of(0, 10)).isEmpty());
    }

    // ================= NEGATIVE TESTS (5) =================

    // 11 - Create user throws when email already exists
    @Test
    void testCreateUser_emailAlreadyExists_throwsException() {
        CreateUserRequest req = new CreateUserRequest();
        req.setEmail("test@mail.com");

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.createUser(req));
    }

    // 12 - Create user throws when address ID is not found
    @Test
    void testCreateUser_addressNotFound_throwsException() {
        CreateUserRequest req = new CreateUserRequest();
        req.setEmail("test@mail.com");
        req.setAddressId(1L);

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.createUser(req));
    }

    // 13 - Get user by ID throws when user does not exist
    @Test
    void testGetUserById_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    // 14 - Update user throws when new email is already in use by another account
    @Test
    void testUpdateUser_emailAlreadyExists_throwsException() {
        User user = User.builder().userId(1L).email("old@mail.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(true);

        UpdateUserRequest req = new UpdateUserRequest();
        req.setEmail("new@mail.com");

        assertThrows(BusinessException.class, () -> userService.updateUser(1L, req));
    }

    // 15 - Get address by ID throws when address does not exist
    @Test
    void testGetAddressById_notFound_throwsException() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getAddressById(1L));
    }
}