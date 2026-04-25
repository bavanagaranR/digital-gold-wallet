import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, PagedResponse } from '../../../core/models/api-response.model';
import { CreateVendorRequest, VendorResponse, CreateBranchRequest, BranchResponse } from '../models/vendor.model';

@Injectable({ providedIn: 'root' })
export class VendorService {
  private http = inject(HttpClient);
  private base = environment.apiBaseUrl;

  createVendor(req: CreateVendorRequest): Observable<ApiResponse<VendorResponse>> {
    return this.http.post<ApiResponse<VendorResponse>>(`${this.base}/api/v1/vendors`, req);
  }
  getVendorById(id: number): Observable<ApiResponse<VendorResponse>> {
    return this.http.get<ApiResponse<VendorResponse>>(`${this.base}/api/v1/vendors/${id}`);
  }
  getAllVendors(page = 0, size = 10): Observable<ApiResponse<PagedResponse<VendorResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<VendorResponse>>>(`${this.base}/api/v1/vendors`, { params });
  }
  updateVendor(id: number, req: Partial<CreateVendorRequest>): Observable<ApiResponse<VendorResponse>> {
    return this.http.put<ApiResponse<VendorResponse>>(`${this.base}/api/v1/vendors/${id}`, req);
  }
  getVendorPrice(id: number): Observable<ApiResponse<{ price: number }>> {
    return this.http.get<ApiResponse<{ price: number }>>(`${this.base}/api/v1/vendors/${id}/price`);
  }
  createBranch(vendorId: number, req: CreateBranchRequest): Observable<ApiResponse<BranchResponse>> {
    return this.http.post<ApiResponse<BranchResponse>>(`${this.base}/api/v1/vendors/${vendorId}/branches`, req);
  }
  getBranchById(branchId: number): Observable<ApiResponse<BranchResponse>> {
    return this.http.get<ApiResponse<BranchResponse>>(`${this.base}/api/v1/branches/${branchId}`);
  }
  getVendorBranches(vendorId: number, page = 0, size = 10): Observable<ApiResponse<PagedResponse<BranchResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<BranchResponse>>>(`${this.base}/api/v1/vendors/${vendorId}/branches`, { params });
  }
  getBranchInventory(branchId: number): Observable<ApiResponse<{ inventoryGrams: number }>> {
    return this.http.get<ApiResponse<{ inventoryGrams: number }>>(`${this.base}/api/v1/branches/${branchId}/inventory`);
  }
}
