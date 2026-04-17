package com.goldwallet.digitalgoldwallet.modules.user;

import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

    @DataJpaTest
    @ActiveProfiles("test")

    public class UserRepositoryTest {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private AddressRepository addressRepository;

        // Helper method
        private Address createAddress() {
            Address address = Address.builder()
                    .street("Anna Nagar")
                    .city("Chennai")
                    .state("TN")
                    .postalCode("600040")
                    .country("India")
                    .build();
            return addressRepository.save(address);
        }

        // Create User
        @Test
        void testCreateUser() {
            Address address = createAddress();

            User user = User.builder()
                    .name("Subha")
                    .email("subha@gmail.com")
                    .address(address)
                    .balance(BigDecimal.ZERO)
                    .build();

            User savedUser = userRepository.save(user);

            assertNotNull(savedUser.getUserId());
        }

        //  Find User by ID
        @Test
        void testFindUserById() {
            Address address = createAddress();

            User saved = userRepository.save(User.builder()
                    .name("Test")
                    .email("test@gmail.com")
                    .address(address)
                    .balance(BigDecimal.TEN)
                    .build());

            Optional<User> found = userRepository.findById(saved.getUserId());

            assertTrue(found.isPresent());
        }

        //  Find User by Email (FIXED HERE)
        @Test
        void testFindUserByEmail() {
            Address address = createAddress();

            userRepository.save(User.builder()
                    .name("Subha")
                    .email("subha@gmail.com")
                    .address(address)
                    .balance(BigDecimal.ZERO)
                    .build());

            Optional<User> userOptional = userRepository.findByEmail("subha@gmail.com");

            assertTrue(userOptional.isPresent());

            User user = userOptional.get();
            assertNotNull(user);
            assertEquals("Subha", user.getName());
        }

        //  Update User Balance
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

            assertEquals(BigDecimal.valueOf(1000), updated.getBalance());
        }

        //  Delete User
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

            Optional<User> found = userRepository.findById(user.getUserId());
            assertFalse(found.isPresent());
        }

        //  Duplicate Email Constraint
        @Test
        void testDuplicateEmail() {
            Address address = createAddress();

            userRepository.save(User.builder()
                    .name("User1")
                    .email("same@gmail.com")
                    .address(address)
                    .balance(BigDecimal.ZERO)
                    .build());

            Exception exception = assertThrows(Exception.class, () -> {
                userRepository.save(User.builder()
                        .name("User2")
                        .email("same@gmail.com")
                        .address(address)
                        .balance(BigDecimal.ZERO)
                        .build());
            });

            assertNotNull(exception);
        }

        //  Null Name Validation
        @Test
        void testNullName() {
            Address address = createAddress();

            Exception exception = assertThrows(Exception.class, () -> {
                userRepository.save(User.builder()
                        .name(null)
                        .email("null@gmail.com")
                        .address(address)
                        .balance(BigDecimal.ZERO)
                        .build());
            });

            assertNotNull(exception);
        }

        //  Address Mapping Check
        @Test
        void testUserAddressMapping() {
            Address address = createAddress();

            User user = userRepository.save(User.builder()
                    .name("Subha")
                    .email("map@gmail.com")
                    .address(address)
                    .balance(BigDecimal.ZERO)
                    .build());

            Optional<User> found = userRepository.findById(user.getUserId());

            assertTrue(found.isPresent());
            assertEquals("Chennai", found.get().getAddress().getCity());
        }

        //  Multiple Users
        @Test
        void testMultipleUsers() {
            Address address = createAddress();

            userRepository.save(User.builder()
                    .name("User1")
                    .email("u1@gmail.com")
                    .address(address)
                    .balance(BigDecimal.ZERO)
                    .build());

            userRepository.save(User.builder()
                    .name("User2")
                    .email("u2@gmail.com")
                    .address(address)
                    .balance(BigDecimal.ZERO)
                    .build());

            assertEquals(2, userRepository.findAll().size());
        }

        //  Address Save Test
        @Test
        void testCreateAddress() {
            Address address = Address.builder()
                    .street("T Nagar")
                    .city("Chennai")
                    .state("TN")
                    .postalCode("600017")
                    .country("India")
                    .build();

            Address saved = addressRepository.save(address);

            assertNotNull(saved.getAddressId());
        }
    }



