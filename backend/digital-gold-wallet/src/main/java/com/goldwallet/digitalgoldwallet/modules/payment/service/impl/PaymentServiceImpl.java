package com.goldwallet.digitalgoldwallet.modules.payment.service.impl;

import com.goldwallet.digitalgoldwallet.common.exception.BusinessException;
import com.goldwallet.digitalgoldwallet.common.exception.ResourceNotFoundException;
import com.goldwallet.digitalgoldwallet.modules.payment.dto.request.InitiatePaymentRequest;
import com.goldwallet.digitalgoldwallet.modules.payment.dto.response.PaymentResponse;
import com.goldwallet.digitalgoldwallet.modules.payment.entity.Payment;
import com.goldwallet.digitalgoldwallet.modules.payment.repository.PaymentRepository;
import com.goldwallet.digitalgoldwallet.modules.payment.service.PaymentService;
import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
import com.goldwallet.digitalgoldwallet.modules.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

// payment service implementation class
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public PaymentResponse initiatePayment(InitiatePaymentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        Payment payment = Payment.builder()
                .user(user)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionType(request.getTransactionType())
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .build();

        // Update user's wallet balance
        if (request.getTransactionType() == Payment.TransactionType.CREDITED_TO_WALLET) {
            user.setBalance(user.getBalance().add(request.getAmount()));
        } else if (request.getTransactionType() == Payment.TransactionType.DEBITED_FROM_WALLET) {
            if (user.getBalance().compareTo(request.getAmount()) < 0) {
                throw new BusinessException("Insufficient wallet balance for this transaction");
            }
            user.setBalance(user.getBalance().subtract(request.getAmount()));
        }
        userRepository.save(user);

        Payment saved = paymentRepository.save(payment);
        log.info("Payment {} initiated for user {}. New balance: {}", saved.getPaymentId(), user.getUserId(), user.getBalance());
        return mapToResponse(saved);
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        return mapToResponse(payment);
    }

    @Override
    public Page<PaymentResponse> getUserPayments(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
        return paymentRepository.findByUserUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::mapToResponse);
    }

    private PaymentResponse mapToResponse(Payment p) {
        return PaymentResponse.builder()
                .paymentId(p.getPaymentId())
                .userId(p.getUser().getUserId())
                .userName(p.getUser().getName())
                .amount(p.getAmount())
                .paymentMethod(p.getPaymentMethod())
                .transactionType(p.getTransactionType())
                .paymentStatus(p.getPaymentStatus())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
