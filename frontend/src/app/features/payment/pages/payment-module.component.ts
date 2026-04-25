import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-payment-module',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './payment-module.component.html',
  styleUrl: './payment-module.component.css'
})
export class PaymentModuleComponent {
  paymentEps = [
    { method: 'POST', path: '/api/v1/payments', purpose: 'Initiate a payment transaction', route: '/payment/create' },
    { method: 'GET', path: '/api/v1/payments/{id}', purpose: 'Fetch payment by ID', route: '/payment/get' },
    { method: 'GET', path: '/api/v1/users/{userId}/payments', purpose: 'List all payments of a user', route: '/payment/user-payments' },
  ];
  walletEps = [
    { method: 'POST', path: '/api/v1/wallets/{userId}/credit', purpose: 'Add amount to wallet', route: '/wallet/credit' },
    { method: 'POST', path: '/api/v1/wallets/{userId}/debit', purpose: 'Deduct amount from wallet', route: '/wallet/debit' },
    { method: 'GET', path: '/api/v1/wallets/{userId}/balance', purpose: 'Get wallet balance', route: '/wallet/balance' },
  ];
}
