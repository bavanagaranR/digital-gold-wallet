import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-module',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-module.component.html',
  styleUrl: './user-module.component.css'
})
export class UserModuleComponent {
  endpoints = [
    { method: 'POST', path: '/api/v1/users', purpose: 'Create a new user', route: '/user/create' },
    { method: 'GET', path: '/api/v1/users/{id}', purpose: 'Get user by ID', route: '/user/get' },
    { method: 'GET', path: '/api/v1/users', purpose: 'List all users (paginated)', route: '/user/list' },
    { method: 'PUT', path: '/api/v1/users/{id}', purpose: 'Update user by ID', route: '/user/update' },
    { method: 'GET', path: '/api/v1/users/{id}/balance', purpose: 'Get user wallet balance', route: '/user/balance' },
    { method: 'POST', path: '/api/v1/addresses', purpose: 'Create address record', route: '/user/address-create' },
    { method: 'GET', path: '/api/v1/addresses/{id}', purpose: 'Get address by ID', route: '/user/address-get' },
    { method: 'PUT', path: '/api/v1/addresses/{id}', purpose: 'Update address by ID', route: '/user/address-update' },
  ];
}
