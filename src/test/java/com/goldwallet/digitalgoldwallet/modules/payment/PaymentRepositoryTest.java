package com.goldwallet.digitalgoldwallet.modules.payment;

import com.goldwallet.digitalgoldwallet.modules.payment.entity.Payment;
import com.goldwallet.digitalgoldwallet.modules.payment.repository.PaymentRepository;
import com.goldwallet.digitalgoldwallet.modules.user.entity.Address;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.AddressRepository;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
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

class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    //  Helper Method
    private User createUser(String email) {
        Address address = addressRepository.save(Address.builder()
                .street("Street")
                .city("Chennai")
                .state("TN")
                .postalCode("600001")
                .country("India")
                .build());

        return userRepository.save(User.builder()
                .name("User " + email)
                .email(email)
                .address(address)
                .balance(BigDecimal.ZERO)
                .build());
    }

    // Create Payment
    @Test
    void testCreatePayment() {
        User user = createUser("pay1@gmail.com");

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .user(user)
                        .amount(new BigDecimal("1000"))
                        .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                        .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                        .paymentStatus(Payment.PaymentStatus.SUCCESS)
                        .build()
        );

        assertNotNull(payment.getPaymentId());
    }

    //  Find Payment by ID
    @Test
    void testFindPaymentById() {
        User user = createUser("pay2@gmail.com");

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .user(user)
                        .amount(new BigDecimal("500"))
                        .paymentMethod(Payment.PaymentMethod.PAYTM)
                        .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                        .paymentStatus(Payment.PaymentStatus.SUCCESS)
                        .build()
        );

        Optional<Payment> found = paymentRepository.findById(payment.getPaymentId());

        assertTrue(found.isPresent());
    }

    //  Update Payment Status
    @Test
    void testUpdatePaymentStatus() {
        User user = createUser("pay3@gmail.com");

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .user(user)
                        .amount(new BigDecimal("200"))
                        .paymentMethod(Payment.PaymentMethod.PHONEPE)
                        .transactionType(Payment.TransactionType.DEBITED_FROM_WALLET)
                        .paymentStatus(Payment.PaymentStatus.FAILED)
                        .build()
        );

        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
        Payment updated = paymentRepository.save(payment);

        assertEquals(Payment.PaymentStatus.SUCCESS, updated.getPaymentStatus());
    }

    //  Delete Payment
    @Test
    void testDeletePayment() {
        User user = createUser("pay4@gmail.com");

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .user(user)
                        .amount(new BigDecimal("300"))
                        .paymentMethod(Payment.PaymentMethod.AMAZON_PAY)
                        .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                        .paymentStatus(Payment.PaymentStatus.SUCCESS)
                        .build()
        );

        paymentRepository.delete(payment);

        assertFalse(paymentRepository.findById(payment.getPaymentId()).isPresent());
    }

    //  Enum Validation (Payment Method)
    @Test
    void testPaymentMethodEnum() {
        User user = createUser("pay5@gmail.com");

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .user(user)
                        .amount(new BigDecimal("150"))
                        .paymentMethod(Payment.PaymentMethod.CREDIT_CARD)
                        .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                        .paymentStatus(Payment.PaymentStatus.SUCCESS)
                        .build()
        );

        assertEquals(Payment.PaymentMethod.CREDIT_CARD, payment.getPaymentMethod());
    }

    //  Enum Validation (Transaction Type)
    @Test
    void testTransactionTypeEnum() {
        User user = createUser("pay6@gmail.com");

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .user(user)
                        .amount(new BigDecimal("250"))
                        .paymentMethod(Payment.PaymentMethod.DEBIT_CARD)
                        .transactionType(Payment.TransactionType.DEBITED_FROM_WALLET)
                        .paymentStatus(Payment.PaymentStatus.SUCCESS)
                        .build()
        );

        assertEquals(Payment.TransactionType.DEBITED_FROM_WALLET, payment.getTransactionType());
    }

    //  Enum Validation (Payment Status)
    @Test
    void testPaymentStatusEnum() {
        User user = createUser("pay7@gmail.com");

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .user(user)
                        .amount(new BigDecimal("400"))
                        .paymentMethod(Payment.PaymentMethod.BANK_TRANSFER)
                        .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                        .paymentStatus(Payment.PaymentStatus.FAILED)
                        .build()
        );

        assertEquals(Payment.PaymentStatus.FAILED, payment.getPaymentStatus());
    }

    //  User Mapping Check
    @Test
    void testUserPaymentMapping() {
        User user = createUser("pay8@gmail.com");

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .user(user)
                        .amount(new BigDecimal("600"))
                        .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                        .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                        .paymentStatus(Payment.PaymentStatus.SUCCESS)
                        .build()
        );

        Optional<Payment> found = paymentRepository.findById(payment.getPaymentId());

        assertEquals("User pay8@gmail.com", found.get().getUser().getName());
    }

    //  Multiple Payments
    @Test
    void testMultiplePayments() {
        User user = createUser("pay9@gmail.com");

        paymentRepository.save(Payment.builder()
                .user(user)
                .amount(new BigDecimal("100"))
                .paymentMethod(Payment.PaymentMethod.PAYTM)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .build());

        paymentRepository.save(Payment.builder()
                .user(user)
                .amount(new BigDecimal("200"))
                .paymentMethod(Payment.PaymentMethod.PHONEPE)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .build());

        List<Payment> list = paymentRepository.findAll();

        assertEquals(2, list.size());
    }

    //  Invalid Case (Null Amount)
    @Test
    void testNullAmount() {
        User user = createUser("pay10@gmail.com");

        Exception exception = assertThrows(Exception.class, () -> {
            paymentRepository.save(Payment.builder()
                    .user(user)
                    .amount(null)
                    .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                    .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                    .paymentStatus(Payment.PaymentStatus.SUCCESS)
                    .build());
        });

        assertNotNull(exception);
    }
}
