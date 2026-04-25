import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-transaction-module',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './transaction-module.component.html',
  styleUrl: './transaction-module.component.css'
})
export class TransactionModuleComponent {
  endpoints = [
    { method: 'GET', path: '/api/v1/transactions/{id}', purpose: 'Fetch transaction by ID', route: '/transaction/get' },
    { method: 'GET', path: '/api/v1/users/{userId}/transactions', purpose: 'List all transactions of a user', route: '/transaction/user' },
    { method: 'GET', path: '/api/v1/branches/{branchId}/transactions', purpose: 'List all transactions of a branch', route: '/transaction/branch' },
    { method: 'GET', path: '/api/v1/transactions/status/{status}', purpose: 'List transactions by status', route: '/transaction/status' },
    { method: 'GET', path: '/api/v1/transactions/type/{type}', purpose: 'List transactions by type', route: '/transaction/type' },
    { method: 'GET', path: '/api/v1/transactions/amount', purpose: 'List transactions above amount', route: '/transaction/amount' },
  ];
}
