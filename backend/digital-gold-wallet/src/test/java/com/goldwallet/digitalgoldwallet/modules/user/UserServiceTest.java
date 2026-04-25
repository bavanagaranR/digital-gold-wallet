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

    // -------- USER TESTS --------

    @Test
    void testCreateUser_success() {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Cathy");
        req.setEmail("cathy@test.com");

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(User.builder().userId(1L).name("Cathy").email("cathy@test.com").build());

        assertEquals("Cathy", userService.createUser(req).getName());
    }

    @Test
    void testCreateUser_emailExists() {
        CreateUserRequest req = new CreateUserRequest();
        req.setEmail("test@mail.com");

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.createUser(req));
    }

    @Test
    void testCreateUser_withAddress() {
        CreateUserRequest req = new CreateUserRequest();
        req.setEmail("test@mail.com");
        req.setAddressId(1L);

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(new Address()));
        when(userRepository.save(any())).thenReturn(User.builder().userId(1L).build());

        assertNotNull(userService.createUser(req));
    }

    @Test
    void testCreateUser_addressNotFound() {
        CreateUserRequest req = new CreateUserRequest();
        req.setEmail("test@mail.com");
        req.setAddressId(1L);

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.createUser(req));
    }

    @Test
    void testGetUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().userId(1L).build()));

        assertEquals(1L, userService.getUserById(1L).getUserId());
    }

    @Test
    void testGetUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetAllUsers() {
        Page<User> page = new PageImpl<>(java.util.List.of(User.builder().userId(1L).build()));

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        assertEquals(1, userService.getAllUsers(PageRequest.of(0, 10)).getContent().size());
    }

    @Test
    void testUpdateUser_success() {
        User user = User.builder().userId(1L).email("old@mail.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UpdateUserRequest req = new UpdateUserRequest();
        req.setName("Updated");

        assertEquals("Updated", userService.updateUser(1L, req).getName());
    }

    @Test
    void testUpdateUser_emailExists() {
        User user = User.builder().userId(1L).email("old@mail.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(true);

        UpdateUserRequest req = new UpdateUserRequest();
        req.setEmail("new@mail.com");

        assertThrows(BusinessException.class, () -> userService.updateUser(1L, req));
    }

    @Test
    void testUpdateUser_addressNotFound() {
        User user = User.builder().userId(1L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateUserRequest req = new UpdateUserRequest();
        req.setAddressId(1L);

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, req));
    }

    @Test
    void testGetWalletBalance() {
        User user = User.builder().balance(new BigDecimal("500")).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(new BigDecimal("500"), userService.getWalletBalance(1L));
    }

    // -------- ADDRESS TESTS --------

    @Test
    void testCreateAddress() {
        when(addressRepository.save(any())).thenReturn(new Address());

        assertNotNull(userService.createAddress(new CreateAddressRequest()));
    }

    @Test
    void testGetAddressById_success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(new Address()));

        assertNotNull(userService.getAddressById(1L));
    }

    @Test
    void testGetAddressById_notFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getAddressById(1L));
    }

    @Test
    void testUpdateAddress() {
        Address address = new Address();

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any())).thenReturn(address);

        assertNotNull(userService.updateAddress(1L, new UpdateAddressRequest()));
    }
}