export interface CreateUserRequest {
  name: string;
  email: string;
  phone: string;
}

export interface UpdateUserRequest {
  name?: string;
  email?: string;
  phone?: string;
}

export interface UserResponse {
  id: number;
  name: string;
  email: string;
  phone: string;
  createdAt?: string;
}

export interface CreateAddressRequest {
  userId: number;
  street: string;
  city: string;
  state: string;
  pincode: string;
  country: string;
}

export interface AddressResponse {
  id: number;
  userId: number;
  street: string;
  city: string;
  state: string;
  pincode: string;
  country: string;
}
