package com.goldwallet.digitalgoldwallet.modules.payment.controller;

import com.goldwallet.digitalgoldwallet.common.response.ApiResponse;
import com.goldwallet.digitalgoldwallet.modules.payment.dto.request.InitiatePaymentRequest;
import com.goldwallet.digitalgoldwallet.modules.payment.dto.response.PaymentResponse;
import com.goldwallet.digitalgoldwallet.modules.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<ApiResponse<PaymentResponse>> initiatePayment(@Valid @RequestBody InitiatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment initiated", paymentService.initiatePayment(request)));
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentById(id)));
    }

    @GetMapping("/users/{userId}/payments")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getUserPayments(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getUserPayments(userId, pageable)));
    }
}
