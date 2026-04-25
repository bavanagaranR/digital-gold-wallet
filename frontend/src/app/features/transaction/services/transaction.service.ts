import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, PagedResponse } from '../../../core/models/api-response.model';
import { TransactionResponse } from '../models/transaction.model';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  private http = inject(HttpClient);
  private base = environment.apiBaseUrl;

  getTransactionById(id: number): Observable<ApiResponse<TransactionResponse>> {
    return this.http.get<ApiResponse<TransactionResponse>>(`${this.base}/api/v1/transactions/${id}`);
  }
  getUserTransactions(userId: number, page = 0, size = 10): Observable<ApiResponse<PagedResponse<TransactionResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<TransactionResponse>>>(`${this.base}/api/v1/users/${userId}/transactions`, { params });
  }
  getBranchTransactions(branchId: number, page = 0, size = 10): Observable<ApiResponse<PagedResponse<TransactionResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<TransactionResponse>>>(`${this.base}/api/v1/branches/${branchId}/transactions`, { params });
  }
  getTransactionsByStatus(status: string, page = 0, size = 10): Observable<ApiResponse<PagedResponse<TransactionResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<TransactionResponse>>>(`${this.base}/api/v1/transactions/status/${status}`, { params });
  }
  getTransactionsByType(type: string, page = 0, size = 10): Observable<ApiResponse<PagedResponse<TransactionResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<TransactionResponse>>>(`${this.base}/api/v1/transactions/type/${type}`, { params });
  }
  getTransactionsGreaterThanAmount(amount: number, page = 0, size = 10): Observable<ApiResponse<PagedResponse<TransactionResponse>>> {
    const params = new HttpParams().set('amount', amount.toString()).set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<TransactionResponse>>>(`${this.base}/api/v1/transactions/amount`, { params });
  }
}
