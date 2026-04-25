import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-gold-module',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './gold-module.component.html',
  styleUrl: './gold-module.component.css'
})
export class GoldModuleComponent {
  endpoints = [
    { method: 'POST', path: '/api/v1/gold/virtual/buy', purpose: 'Buy virtual gold', route: '/gold/buy' },
    { method: 'POST', path: '/api/v1/gold/virtual/sell', purpose: 'Sell virtual gold', route: '/gold/sell' },
    { method: 'GET', path: '/api/v1/users/{userId}/gold/virtual', purpose: 'User virtual gold holdings', route: '/gold/user-virtual' },
    { method: 'GET', path: '/api/v1/branches/{branchId}/gold/virtual', purpose: 'Branch virtual gold holdings', route: '/gold/branch-virtual' },
    { method: 'POST', path: '/api/v1/gold/physical/convert', purpose: 'Convert virtual to physical gold', route: '/gold/convert' },
    { method: 'GET', path: '/api/v1/users/{userId}/gold/physical', purpose: 'User physical gold records', route: '/gold/user-physical' },
  ];
}
