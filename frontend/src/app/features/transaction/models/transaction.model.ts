export interface TransactionResponse {
  id: number;
  userId: number;
  branchId?: number;
  type: string;
  amount: number;
  status: string;
  createdAt?: string;
  description?: string;
}
