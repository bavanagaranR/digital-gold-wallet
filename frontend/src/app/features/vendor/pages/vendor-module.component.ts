import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-vendor-module',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './vendor-module.component.html',
  styleUrl: './vendor-module.component.css'
})
export class VendorModuleComponent {
  endpoints = [
    { method: 'POST', path: '/api/v1/vendors', purpose: 'Create a new vendor', route: '/vendor/create' },
    { method: 'GET', path: '/api/v1/vendors/{id}', purpose: 'Get vendor by ID', route: '/vendor/get' },
    { method: 'GET', path: '/api/v1/vendors', purpose: 'List all vendors (paginated)', route: '/vendor/list' },
    { method: 'PUT', path: '/api/v1/vendors/{id}', purpose: 'Update vendor information', route: '/vendor/update' },
    { method: 'GET', path: '/api/v1/vendors/{id}/price', purpose: 'Get vendor gold price', route: '/vendor/price' },
    { method: 'POST', path: '/api/v1/vendors/{vendorId}/branches', purpose: 'Add branch to vendor', route: '/vendor/branch-create' },
    { method: 'GET', path: '/api/v1/branches/{branchId}', purpose: 'Get branch by ID', route: '/vendor/branch-get' },
    { method: 'GET', path: '/api/v1/vendors/{vendorId}/branches', purpose: 'List all branches of a vendor', route: '/vendor/branch-list' },
    { method: 'GET', path: '/api/v1/branches/{branchId}/inventory', purpose: 'View branch inventory', route: '/vendor/branch-inventory' },
  ];
}
