export interface CreateUserRequest {
  name: string;
  email: string;
  addressId: number;
}

export interface UpdateUserRequest {
  name?: string;
  email?: string;
  addressId?: number;
}

export interface UserResponse {
  userId: number;
  name: string;
  email: string;
  balance: number;
  address?: AddressResponse;
  createdAt?: string;
}

export interface CreateAddressRequest {
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}

export interface AddressResponse {
  addressId: number;
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}
