// models/gold.model.ts
export interface BuyGoldRequest {
  userId: number;
  branchId: number;
  grams: number;
}
export interface SellGoldRequest {
  userId: number;
  branchId: number;
  grams: number;
}
export interface ConvertToPhysicalRequest {
  userId: number;
  branchId: number;
  grams: number;
  deliveryAddress: string;
}
export interface VirtualGoldResponse {
  id: number;
  userId: number;
  branchId: number;
  totalGrams: number;
  lastUpdated?: string;
}
export interface PhysicalGoldResponse {
  id: number;
  userId: number;
  branchId: number;
  grams: number;
  deliveryAddress: string;
  status: string;
  createdAt?: string;
}
