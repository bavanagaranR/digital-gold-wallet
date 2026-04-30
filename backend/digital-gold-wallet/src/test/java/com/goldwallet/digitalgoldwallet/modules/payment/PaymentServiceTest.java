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

    private Payment buildPayment(Long paymentId, User user, BigDecimal amount) {
        return Payment.builder()
                .paymentId(paymentId)
                .user(user)
                .amount(amount)
                .paymentMethod(Payment.PaymentMethod.GOOGLE_PAY)
                .transactionType(Payment.TransactionType.CREDITED_TO_WALLET)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ================= POSITIVE TESTS (10) =================

    // 1 - Initiate payment returns non-null response
    @Test
    void testInitiatePayment_success_returnsResponse() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("500"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(buildPayment(1L, user, new BigDecimal("500")));

        PaymentResponse res = paymentService.initiatePayment(req);

        assertNotNull(res);
        assertEquals(1L, res.getPaymentId());
        assertEquals("Cathy", res.getUserName());
    }

    // 2 - Initiate payment maps amount correctly
    @Test
    void testInitiatePayment_success_amountMappedCorrectly() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("750"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(buildPayment(2L, user, new BigDecimal("750")));

        PaymentResponse res = paymentService.initiatePayment(req);

        assertEquals(new BigDecimal("750"), res.getAmount());
    }

    // 3 - Initiate payment maps payment method correctly
    @Test
    void testInitiatePayment_success_paymentMethodMapped() {

        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("500"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(buildPayment(3L, user, new BigDecimal("500")));

        PaymentResponse res = paymentService.initiatePayment(req);

        assertEquals(Payment.PaymentMethod.GOOGLE_PAY, res.getPaymentMethod());
    }

    // 4 - Initiate payment status defaults to SUCCESS
    @Test
    void testInitiatePayment_success_defaultStatusIsSuccess() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("400"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(buildPayment(4L, user, new BigDecimal("400")));

        PaymentResponse res = paymentService.initiatePayment(req);

        assertEquals(Payment.PaymentStatus.SUCCESS, res.getPaymentStatus());
    }

    // 5 - Initiate payment calls repository save exactly once
    @Test
    void testInitiatePayment_success_repositorySaveCalledOnce() {
        User user = buildUser(1L, "Cathy");

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);
        req.setAmount(new BigDecimal("300"));
        req.setPaymentMethod(Payment.PaymentMethod.GOOGLE_PAY);
        req.setTransactionType(Payment.TransactionType.CREDITED_TO_WALLET);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(buildPayment(5L, user, new BigDecimal("300")));

        paymentService.initiatePayment(req);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    // 6 - Get payment by ID returns correct payment
    @Test
    void testGetPaymentById_success_returnsPayment() {
        User user = buildUser(1L, "Cathy");
        Payment payment = buildPayment(7L, user, new BigDecimal("100"));

        when(paymentRepository.findById(7L)).thenReturn(Optional.of(payment));

        PaymentResponse res = paymentService.getPaymentById(7L);

        assertEquals(7L, res.getPaymentId());
        assertEquals("Cathy", res.getUserName());
    }

    // 7 - Get payment by ID maps amount correctly
    @Test
    void testGetPaymentById_success_amountMapped() {
        User user = buildUser(1L, "Cathy");
        Payment payment = buildPayment(9L, user, new BigDecimal("999"));

        when(paymentRepository.findById(9L)).thenReturn(Optional.of(payment));

        PaymentResponse res = paymentService.getPaymentById(9L);

        assertEquals(new BigDecimal("999"), res.getAmount());
    }

    // 8 - Get payment by ID maps createdAt correctly
    @Test
    void testGetPaymentById_success_createdAtMapped() {
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

    // 9 - Get user payments returns paginated results
    @Test
    void testGetUserPayments_success_returnsSinglePayment() {
        User user = buildUser(1L, "Cathy");
        Payment payment = buildPayment(11L, user, new BigDecimal("100"));

        when(userRepository.existsById(1L)).thenReturn(true);
        when(paymentRepository.findByUserUserIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(payment)));

        Page<PaymentResponse> result = paymentService.getUserPayments(1L, Pageable.unpaged());

        assertEquals(1, result.getContent().size());
    }

    // 10 - Get user payments returns multiple payments in order
    @Test
    void testGetUserPayments_success_returnsMultiplePayments() {
        User user = buildUser(1L, "Cathy");
        Payment payment1 = buildPayment(12L, user, new BigDecimal("100"));
        Payment payment2 = buildPayment(13L, user, new BigDecimal("200"));

        when(userRepository.existsById(1L)).thenReturn(true);
        when(paymentRepository.findByUserUserIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(payment1, payment2)));

        Page<PaymentResponse> result = paymentService.getUserPayments(1L, Pageable.unpaged());

        assertEquals(2, result.getContent().size());
        assertEquals(12L, result.getContent().get(0).getPaymentId());
        assertEquals(13L, result.getContent().get(1).getPaymentId());
    }

    // ================= NEGATIVE TESTS (5) =================

    // 11 - Initiate payment throws when user not found
    @Test
    void testInitiatePayment_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(1L);

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.initiatePayment(req));
    }

    // 12 - Initiate payment with null user ID throws exception
    @Test
    void testInitiatePayment_nullUserId_throwsException() {
        when(userRepository.findById(null)).thenReturn(Optional.empty());

        InitiatePaymentRequest req = new InitiatePaymentRequest();
        req.setUserId(null);

        assertThrows(Exception.class, () -> paymentService.initiatePayment(req));
    }

    // 13 - Get payment by ID throws when payment not found
    @Test
    void testGetPaymentById_notFound_throwsException() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getPaymentById(99L));
    }

    // 14 - Get user payments throws when user does not exist
    @Test
    void testGetUserPayments_userNotFound_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getUserPayments(1L, Pageable.unpaged()));
    }

    // 15 - Get user payments returns empty page for user with no payments
    @Test
    void testGetUserPayments_noPayments_returnsEmptyPage() {
        when(userRepository.existsById(2L)).thenReturn(true);
        when(paymentRepository.findByUserUserIdOrderByCreatedAtDesc(eq(2L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<PaymentResponse> result = paymentService.getUserPayments(2L, Pageable.unpaged());

        assertTrue(result.isEmpty());
    }
}