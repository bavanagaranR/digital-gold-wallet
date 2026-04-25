import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, PagedResponse } from '../../../core/models/api-response.model';
import { InitiatePaymentRequest, PaymentResponse } from '../models/payment.model';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private http = inject(HttpClient);
  private base = environment.apiBaseUrl;

  initiatePayment(req: InitiatePaymentRequest): Observable<ApiResponse<PaymentResponse>> {
    return this.http.post<ApiResponse<PaymentResponse>>(`${this.base}/api/v1/payments`, req);
  }
  getPaymentById(id: number): Observable<ApiResponse<PaymentResponse>> {
    return this.http.get<ApiResponse<PaymentResponse>>(`${this.base}/api/v1/payments/${id}`);
  }
  getUserPayments(userId: number, page = 0, size = 10): Observable<ApiResponse<PagedResponse<PaymentResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<PaymentResponse>>>(`${this.base}/api/v1/users/${userId}/payments`, { params });
  }
}
