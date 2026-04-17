package com.goldwallet.digitalgoldwallet.modules.payment.service;

import com.goldwallet.digitalgoldwallet.modules.payment.dto.request.InitiatePaymentRequest;
import com.goldwallet.digitalgoldwallet.modules.payment.dto.response.PaymentResponse;

import java.util.List;

//payment interface

public interface PaymentService {
    PaymentResponse initiatePayment(InitiatePaymentRequest request);
    PaymentResponse getPaymentById(Long paymentId);
    List<PaymentResponse> getUserPayments(Long userId);
}
