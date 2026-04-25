package com.goldwallet.digitalgoldwallet.modules.payment;

import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.payment.dto.request.InitiatePaymentRequest;
import com.goldwallet.digitalgoldwallet.modules.payment.dto.response.PaymentResponse;
import com.goldwallet.digitalgoldwallet.modules.payment.entity.Payment;
import com.goldwallet.digitalgoldwallet.modules.payment.repository.PaymentRepository;
import com.goldwallet.digitalgoldwallet.modules.payment.service.impl.PaymentServiceImpl;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User buildUser(Long id, String name) {
        User user = new User();
        user.setUserId(id);
        user.setName(name);
        user.setBalance(java.math.BigDecimal.ZERO);
        return user;
    }

    // 1
    @Test
    void testInitiatePayment_success() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("500"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        Payment savedPayment = Payment.builder()
                .paymentId(1L)
                .user(user)
                .amount(new BigDecimal("500"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse res = paymentService.initiatePayment(req);

        assertNotNull(res);
        assertEquals(1L, res.getPaymentId());
        assertEquals(1L, res.getUserId());
        assertEquals("Cathy", res.getUserName());
    }

    // 2
    @Test
    void testInitiatePayment_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.initiatePayment(req));
    }

    // 3
    @Test
    void testInitiatePayment_amountMappedCorrectly() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("750"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        Payment savedPayment = Payment.builder()
                .paymentId(2L)
                .user(user)
                .amount(new BigDecimal("750"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse res = paymentService.initiatePayment(req);

        assertEquals(new BigDecimal("750"), res.getAmount());
    }

    // 4
    @Test
    void testInitiatePayment_paymentMethodMappedCorrectly() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("500"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        Payment savedPayment = Payment.builder()
                .paymentId(3L)
                .user(user)
                .amount(new BigDecimal("500"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse res = paymentService.initiatePayment(req);

        assertEquals(Payment.PaymentMethod.GOOGLE_PAY, res.getPaymentMethod());
    }

    // 5
    @Test
    void testInitiatePayment_transactionTypeMappedCorrectly() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("300"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        Payment savedPayment = Payment.builder()
                .paymentId(4L)
                .user(user)
                .amount(new BigDecimal("300"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse res = paymentService.initiatePayment(req);

        assertEquals(Payment.TransactionType.CREDITED_TO_WALLET, res.getTransactionType());
    }

    // 6
    @Test
    void testInitiatePayment_defaultStatusSuccess() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("400"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        Payment savedPayment = Payment.builder()
                .paymentId(5L)
                .user(user)
                .amount(new BigDecimal("400"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse res = paymentService.initiatePayment(req);

        assertEquals(Payment.PaymentStatus.SUCCESS, res.getPaymentStatus());
    }

    // 7
    @Test
    void testInitiatePayment_repositorySaveCalledOnce() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("300"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        Payment savedPayment = Payment.builder()
                .paymentId(6L)
                .user(user)
                .amount(new BigDecimal("300"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        paymentService.initiatePayment(req);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    // 8
    @Test
    void testGetPaymentById_success() {
        User user = buildUser(1L, "Cathy");

        Payment payment = Payment.builder()
                .paymentId(7L)
                .user(user)
                .amount(new BigDecimal("100"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(paymentRepository.findById(7L)).thenReturn(Optional.of(payment));

        PaymentResponse res = paymentService.getPaymentById(7L);

        assertEquals(7L, res.getPaymentId());
    }

    // 9
    @Test
    void testGetPaymentById_notFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getPaymentById(1L));
    }

    // 10
    @Test
    void testGetPaymentById_userNameMappedCorrectly() {
        User user = buildUser(10L, "Cathy");

        Payment payment = Payment.builder()
                .paymentId(8L)
                .user(user)
                .amount(new BigDecimal("200"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(paymentRepository.findById(8L)).thenReturn(Optional.of(payment));

        PaymentResponse res = paymentService.getPaymentById(8L);

        assertEquals("Cathy", res.getUserName());
    }

    // 11
    @Test
    void testGetPaymentById_amountReturnedCorrectly() {
        User user = buildUser(1L, "Cathy");

        Payment payment = Payment.builder()
                .paymentId(9L)
                .user(user)
                .amount(new BigDecimal("999"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(paymentRepository.findById(9L)).thenReturn(Optional.of(payment));

        PaymentResponse res = paymentService.getPaymentById(9L);

        assertEquals(new BigDecimal("999"), res.getAmount());
    }

    // 12
    @Test
    void testGetPaymentById_createdAtMappedCorrectly() {
        User user = buildUser(1L, "Cathy");
        LocalDateTime createdTime = LocalDateTime.now();

        Payment payment = Payment.builder()
                .paymentId(10L)
                .user(user)
                .amount(new BigDecimal("111"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(createdTime)
                .build();

        when(paymentRepository.findById(10L)).thenReturn(Optional.of(payment));

        PaymentResponse res = paymentService.getPaymentById(10L);

        assertEquals(createdTime, res.getCreatedAt());
    }

    // 13
    @Test
    void testGetUserPayments_success() {
        User user = buildUser(1L, "Cathy");

        Payment payment = Payment.builder()
                .paymentId(11L)
                .user(user)
                .amount(new BigDecimal("100"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(paymentRepository.findByUserUserIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(payment)));

        Page<PaymentResponse> result = paymentService.getUserPayments(1L, Pageable.unpaged());

        assertEquals(1, result.getContent().size());
    }

    // 14
    @Test
    void testGetUserPayments_userNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getUserPayments(1L, Pageable.unpaged()));
    }

    // 15
    @Test
    void testGetUserPayments_multiplePayments() {
        User user = buildUser(1L, "Cathy");

        Payment payment1 = Payment.builder()
                .paymentId(12L)
                .user(user)
                .amount(new BigDecimal("100"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        Payment payment2 = Payment.builder()
                .paymentId(13L)
                .user(user)
                .amount(new BigDecimal("200"))
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(paymentRepository.findByUserUserIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(payment1, payment2)));

        Page<PaymentResponse> result = paymentService.getUserPayments(1L, Pageable.unpaged());

        assertEquals(2, result.getContent().size());
        assertEquals(12L, result.getContent().get(0).getPaymentId());
        assertEquals(13L, result.getContent().get(1).getPaymentId());
    }
}