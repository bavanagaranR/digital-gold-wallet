    package com.goldwallet.digitalgoldwallet.modules.payment.entity;


    import com.fasterxml.jackson.annotation.JsonCreator;
    import com.goldwallet.digitalgoldwallet.modules.user.entity.User;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;





    import jakarta.validation.constraints.*;




// Entity class representing a Payment transaction.
// Maps to the 'payments' table in the database and stores details like amount, method, and status.

@Entity
@Table(name = "payments")

@Builder
public class Payment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "payment_id")
        private Long paymentId;

        @NotNull(message = "User cannot be null")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
        @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
        @Column(nullable = false, precision = 18, scale = 2)
        private BigDecimal amount;

        @NotNull(message = "Payment Method * required")
        @Enumerated(EnumType.STRING)
        @Column(name = "payment_method", length = 50)
        private PaymentMethod paymentMethod;

        @NotNull(message = "Transaction Type * requiered")
        @Enumerated(EnumType.STRING)
        @Column(name = "transaction_type", length = 50)
        private TransactionType transactionType;

        @NotNull(message = "Payment status is required")
        @Enumerated(EnumType.STRING)
        @Column(name = "payment_status", length = 20)
        private PaymentStatus paymentStatus;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        public Payment() {
        }

        public Payment(Long paymentId, User user, BigDecimal amount, PaymentMethod paymentMethod, TransactionType transactionType, PaymentStatus paymentStatus, LocalDateTime createdAt) {

            this.paymentId = paymentId;
            this.user = user;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.transactionType = transactionType;
            this.paymentStatus = paymentStatus;
            this.createdAt = createdAt;
        }

        public Long getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(Long paymentId) {
            this.paymentId = paymentId;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public TransactionType getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
        }

        public PaymentStatus getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public enum PaymentMethod {
            CREDIT_CARD, DEBIT_CARD, GOOGLE_PAY, AMAZON_PAY, PHONEPE, PAYTM, BANK_TRANSFER;

            @JsonCreator
            public static PaymentMethod fromString(String value) {
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }
                for (PaymentMethod method : PaymentMethod.values()) {
                    if (method.name().equalsIgnoreCase(value)) {
                        return method;
                    }
                }
                return null;
            }
        }

        public enum TransactionType {
            CREDITED_TO_WALLET, DEBITED_FROM_WALLET;

            @JsonCreator
            public static TransactionType fromString(String value) {
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }
                for (TransactionType type : TransactionType.values()) {
                    if (type.name().equalsIgnoreCase(value)) {
                        return type;
                    }
                }
                return null;
            }
        }

        public enum PaymentStatus {
            SUCCESS, FAILED
        }
    }