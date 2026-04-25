import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../core/models/api-response.model';
import { WalletTransactionRequest, WalletResponse } from '../models/wallet.model';

@Injectable({ providedIn: 'root' })
export class WalletService {
  private http = inject(HttpClient);
  private base = environment.apiBaseUrl;

  credit(userId: number, req: WalletTransactionRequest): Observable<ApiResponse<WalletResponse>> {
    return this.http.post<ApiResponse<WalletResponse>>(`${this.base}/api/v1/wallets/${userId}/credit`, req);
  }
  debit(userId: number, req: WalletTransactionRequest): Observable<ApiResponse<WalletResponse>> {
    return this.http.post<ApiResponse<WalletResponse>>(`${this.base}/api/v1/wallets/${userId}/debit`, req);
  }
  getBalance(userId: number): Observable<ApiResponse<WalletResponse>> {
    return this.http.get<ApiResponse<WalletResponse>>(`${this.base}/api/v1/wallets/${userId}/balance`);
  }
}
