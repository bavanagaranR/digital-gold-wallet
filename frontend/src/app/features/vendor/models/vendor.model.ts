export interface CreateVendorRequest {
  name: string;
  email: string;
  phone: string;
  goldPricePerGram: number;
}

export interface VendorResponse {
  id: number;
  name: string;
  email: string;
  phone: string;
  goldPricePerGram: number;
}

export interface CreateBranchRequest {
  name: string;
  address: string;
  city: string;
  phone: string;
  inventoryGrams: number;
}

export interface BranchResponse {
  id: number;
  vendorId: number;
  name: string;
  address: string;
  city: string;
  phone: string;
  inventoryGrams: number;
}
