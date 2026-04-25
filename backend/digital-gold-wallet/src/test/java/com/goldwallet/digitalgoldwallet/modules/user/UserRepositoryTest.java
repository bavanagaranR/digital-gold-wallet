package com.goldwallet.digitalgoldwallet.modules.user;

import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EntityManager entityManager;

    // ---------- Helper ----------
    private Address createAddress() {
        return addressRepository.save(Address.builder()
                .street("Anna Nagar")
                .city("Chennai")
                .state("TN")
                .postalCode("600040")
                .country("India")
                .build());
    }

    // ---------- BASIC CRUD ----------

    @Test
    void testCreateUser() {
        Address address = createAddress();

        User user = userRepository.save(User.builder()
                .name("Subha")
                .email("subha@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        assertNotNull(user.getUserId());
    }

    @Test
    void testFindUserById() {
        Address address = createAddress();

        User saved = userRepository.save(User.builder()
                .name("Test")
                .email("test@gmail.com")
                .address(address)
                .balance(BigDecimal.TEN)
                .build());

        assertTrue(userRepository.findById(saved.getUserId()).isPresent());
    }

    @Test
    void testFindUserByEmail() {
        Address address = createAddress();

        userRepository.save(User.builder()
                .name("Subha")
                .email("subha@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        Optional<User> user = userRepository.findByEmail("subha@gmail.com");

        assertTrue(user.isPresent());
        assertEquals("Subha", user.get().getName());
    }

    @Test
    void testUpdateUserBalance() {
        Address address = createAddress();

        User user = userRepository.save(User.builder()
                .name("Subha")
                .email("subha@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        user.setBalance(BigDecimal.valueOf(1000));
        User updated = userRepository.save(user);

        assertEquals(0, updated.getBalance().compareTo(new BigDecimal("1000")));
    }

    @Test
    void testDeleteUser() {
        Address address = createAddress();

        User user = userRepository.save(User.builder()
                .name("Subha")
                .email("subha@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        userRepository.delete(user);

        assertFalse(userRepository.findById(user.getUserId()).isPresent());
    }

    // ---------- CUSTOM QUERY TESTS ----------

    @Test
    void testFindByNameIgnoreCase() {
        Address address = createAddress();

        userRepository.save(User.builder()
                .name("Subha")
                .email("case@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        assertTrue(userRepository.findByNameIgnoreCase("subha").isPresent());
    }

    @Test
    void testFindUsersWithBalanceGreaterThan() {
        Address address = createAddress();

        userRepository.save(User.builder()
                .name("User1")
                .email("u1@gmail.com")
                .address(address)
                .balance(new BigDecimal("500"))
                .build());

        userRepository.save(User.builder()
                .name("User2")
                .email("u2@gmail.com")
                .address(address)
                .balance(new BigDecimal("2000"))
                .build());

        List<User> users =
                userRepository.findUsersWithBalanceGreaterThan(new BigDecimal("1000"));

        assertEquals(1, users.size());
    }

    @Test
    void testFindUsersByCity() {
        Address address = createAddress();

        userRepository.save(User.builder()
                .name("CityUser")
                .email("city@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        List<User> users = userRepository.findUsersByCity("Chennai");

        assertFalse(users.isEmpty());
    }

    @Test
    void testCustomUpdateBalance() {
        Address address = createAddress();

        User user = userRepository.save(User.builder()
                .name("UpdateUser")
                .email("update@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        userRepository.updateUserBalance(user.getUserId(), new BigDecimal("3000"));

        entityManager.flush();
        entityManager.clear();

        User updated = userRepository.findById(user.getUserId()).get();

        assertEquals(0, updated.getBalance().compareTo(new BigDecimal("3000")));
    }

    @Test
    void testCustomDeleteUser() {
        Address address = createAddress();

        User user = userRepository.save(User.builder()
                .name("DeleteUser")
                .email("delete@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        userRepository.deleteUserByIdCustom(user.getUserId());

        entityManager.flush();
        entityManager.clear();

        assertFalse(userRepository.findById(user.getUserId()).isPresent());
    }

    @Test
    void testCountUsersByCity() {
        Address address = createAddress();

        userRepository.save(User.builder()
                .name("User1")
                .email("c1@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        userRepository.save(User.builder()
                .name("User2")
                .email("c2@gmail.com")
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());

        long count = userRepository.countUsersByCity("Chennai");

        assertEquals(2, count);
    }
}

