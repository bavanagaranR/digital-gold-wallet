export interface TransactionResponse {
  transactionId: number;
  userId: number;
  userName?: string;
  branchId?: number;
  transactionType: string;
  transactionStatus: string;
  quantity?: number;
  amount: number;
  createdAt?: string;
}
