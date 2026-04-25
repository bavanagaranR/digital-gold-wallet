import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, PagedResponse } from '../../../core/models/api-response.model';
import { CreateUserRequest, UpdateUserRequest, UserResponse, CreateAddressRequest, AddressResponse } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private base = environment.apiBaseUrl;

  createUser(req: CreateUserRequest): Observable<ApiResponse<UserResponse>> {
    return this.http.post<ApiResponse<UserResponse>>(`${this.base}/api/v1/users`, req);
  }

  getUserById(id: number): Observable<ApiResponse<UserResponse>> {
    return this.http.get<ApiResponse<UserResponse>>(`${this.base}/api/v1/users/${id}`);
  }

  getAllUsers(page = 0, size = 10): Observable<ApiResponse<PagedResponse<UserResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<UserResponse>>>(`${this.base}/api/v1/users`, { params });
  }

  updateUser(id: number, req: UpdateUserRequest): Observable<ApiResponse<UserResponse>> {
    return this.http.put<ApiResponse<UserResponse>>(`${this.base}/api/v1/users/${id}`, req);
  }

  getUserBalance(id: number): Observable<ApiResponse<{ balance: number }>> {
    return this.http.get<ApiResponse<{ balance: number }>>(`${this.base}/api/v1/users/${id}/balance`);
  }

  createAddress(req: CreateAddressRequest): Observable<ApiResponse<AddressResponse>> {
    return this.http.post<ApiResponse<AddressResponse>>(`${this.base}/api/v1/addresses`, req);
  }

  getAddressById(id: number): Observable<ApiResponse<AddressResponse>> {
    return this.http.get<ApiResponse<AddressResponse>>(`${this.base}/api/v1/addresses/${id}`);
  }

  updateAddress(id: number, req: Partial<CreateAddressRequest>): Observable<ApiResponse<AddressResponse>> {
    return this.http.put<ApiResponse<AddressResponse>>(`${this.base}/api/v1/addresses/${id}`, req);
  }
}
