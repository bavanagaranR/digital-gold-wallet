// payment/models/payment.model.ts
export interface InitiatePaymentRequest {
  userId: number;
  amount: number;
  paymentMethod: string;
  transactionType: string;
}
export interface PaymentResponse {
  paymentId: number;
  userId: number;
  amount: number;
  paymentMethod: string;
  transactionType: string;
  paymentStatus: string;
  createdAt?: string;
}
