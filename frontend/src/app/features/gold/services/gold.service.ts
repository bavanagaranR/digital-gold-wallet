import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, PagedResponse } from '../../../core/models/api-response.model';
import { BuyGoldRequest, SellGoldRequest, ConvertToPhysicalRequest, VirtualGoldResponse, PhysicalGoldResponse } from '../models/gold.model';

@Injectable({ providedIn: 'root' })
export class GoldService {
  private http = inject(HttpClient);
  private base = environment.apiBaseUrl;

  buyGold(req: BuyGoldRequest): Observable<ApiResponse<VirtualGoldResponse>> {
    return this.http.post<ApiResponse<VirtualGoldResponse>>(`${this.base}/api/v1/gold/virtual/buy`, req);
  }
  sellGold(req: SellGoldRequest): Observable<ApiResponse<VirtualGoldResponse>> {
    return this.http.post<ApiResponse<VirtualGoldResponse>>(`${this.base}/api/v1/gold/virtual/sell`, req);
  }
  getUserVirtualGold(userId: number, page = 0, size = 10): Observable<ApiResponse<any>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<any>>(`${this.base}/api/v1/users/${userId}/gold/virtual`, { params });
  }
  getBranchVirtualGold(branchId: number, page = 0, size = 10): Observable<ApiResponse<any>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<any>>(`${this.base}/api/v1/branches/${branchId}/gold/virtual`, { params });
  }
  convertToPhysical(req: ConvertToPhysicalRequest): Observable<ApiResponse<PhysicalGoldResponse>> {
    return this.http.post<ApiResponse<PhysicalGoldResponse>>(`${this.base}/api/v1/gold/physical/convert`, req);
  }
  getUserPhysicalGold(userId: number, page = 0, size = 10): Observable<ApiResponse<any>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<any>>(`${this.base}/api/v1/users/${userId}/gold/physical`, { params });
  }
}
