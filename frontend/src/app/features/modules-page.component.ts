import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../core/auth/auth.service';

@Component({
  selector: 'app-modules-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="page-container">
      <h1 class="font-heading text-4xl text-slate-900 mb-2">All Modules</h1>
      <p class="text-slate-500 mb-8">Navigate directly to any module's endpoint pages</p>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        @for (m of visibleModules; track m.route) {
          <a [routerLink]="m.route" class="card hover:border-gold-600 transition-all duration-200 block group">
            <div class="text-3xl mb-3">{{ m.icon }}</div>
            <h3 class="text-slate-900 font-semibold group-hover:text-primary-600 transition-colors">{{ m.name }}</h3>
            <p class="text-xs text-slate-400 mt-1">{{ m.developer }}</p>
            <p class="text-sm text-slate-500 mt-2">{{ m.desc }}</p>
          </a>
        }
      </div>
    </div>
  `
})
export class ModulesPageComponent {
  auth = inject(AuthService);

  modules = [
    { name: 'User & Address', developer: 'Pavithra', icon: '👤', route: '/user', desc: 'User management and address CRUD', id: 'user' },
    { name: 'Vendor & Branch', developer: 'Caitlyn Mary', icon: '🏪', route: '/vendor', desc: 'Vendor registration and branch management', id: 'vendor' },
    { name: 'Gold (Virtual)', developer: 'Suba Harini', icon: '🥇', route: '/gold', desc: 'Buy, sell and view gold holdings', id: 'gold' },
    { name: 'Payment', developer: 'Bavanagaran', icon: '💳', route: '/payment', desc: 'Initiate and track payments', id: 'payment' },
    { name: 'Wallet', developer: 'Bavanagaran', icon: '👛', route: '/wallet', desc: 'Credit, debit, and balance checks', id: 'wallet' },
    { name: 'Transaction', developer: 'Mirudhula', icon: '📋', route: '/transaction', desc: 'Transaction history and records', id: 'transaction' },
  ];

  get visibleModules() {
    return this.modules.filter(m => this.auth.canAccessModule(m.id));
  }
}
