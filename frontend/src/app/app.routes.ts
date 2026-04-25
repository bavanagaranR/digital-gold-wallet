import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { roleGuard } from './core/auth/role.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login-page/login-page.component').then(m => m.LoginPageComponent)
  },
  {
    path: 'home',
    canActivate: [authGuard],
    loadComponent: () => import('./features/home/pages/home-page/home-page.component').then(m => m.HomePageComponent)
  },
  {
    path: 'endpoints',
    canActivate: [authGuard],
    loadComponent: () => import('./features/endpoint-explorer/pages/endpoint-explorer-page/endpoint-explorer-page.component').then(m => m.EndpointExplorerPageComponent)
  },
  {
    path: 'modules',
    canActivate: [authGuard],
    loadComponent: () => import('./features/modules-page.component').then(m => m.ModulesPageComponent)
  },

  // ── User Module ──────────────────────────────────────────────
  {
    path: 'user',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/user-module.component').then(m => m.UserModuleComponent)
  },
  {
    path: 'user/create',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/user-create/user-create.component').then(m => m.UserCreateComponent)
  },
  {
    path: 'user/get',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/user-get/user-get.component').then(m => m.UserGetComponent)
  },
  {
    path: 'user/list',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/user-list/user-list.component').then(m => m.UserListComponent)
  },
  {
    path: 'user/update',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/user-update/user-update.component').then(m => m.UserUpdateComponent)
  },
  {
    path: 'user/balance',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/user-balance/user-balance.component').then(m => m.UserBalanceComponent)
  },
  {
    path: 'user/address-create',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/address-create/address-create.component').then(m => m.AddressCreateComponent)
  },
  {
    path: 'user/address-get',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/address-get/address-get.component').then(m => m.AddressGetComponent)
  },
  {
    path: 'user/address-update',
    canActivate: [authGuard, roleGuard],
    data: { module: 'user' },
    loadComponent: () => import('./features/user/pages/address-update/address-update.component').then(m => m.AddressUpdateComponent)
  },

  // ── Vendor Module ────────────────────────────────────────────
  {
    path: 'vendor',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/vendor-module.component').then(m => m.VendorModuleComponent)
  },
  {
    path: 'vendor/create',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/vendor-create/vendor-create.component').then(m => m.VendorCreateComponent)
  },
  {
    path: 'vendor/get',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/vendor-get/vendor-get.component').then(m => m.VendorGetComponent)
  },
  {
    path: 'vendor/list',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/vendor-list/vendor-list.component').then(m => m.VendorListComponent)
  },
  {
    path: 'vendor/update',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/vendor-update/vendor-update.component').then(m => m.VendorUpdateComponent)
  },
  {
    path: 'vendor/price',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/vendor-price/vendor-price.component').then(m => m.VendorPriceComponent)
  },
  {
    path: 'vendor/branch-create',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/branch-create/branch-create.component').then(m => m.BranchCreateComponent)
  },
  {
    path: 'vendor/branch-get',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/branch-get/branch-get.component').then(m => m.BranchGetComponent)
  },
  {
    path: 'vendor/branch-list',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/branch-list/branch-list.component').then(m => m.BranchListComponent)
  },
  {
    path: 'vendor/branch-inventory',
    canActivate: [authGuard, roleGuard],
    data: { module: 'vendor' },
    loadComponent: () => import('./features/vendor/pages/branch-inventory/branch-inventory.component').then(m => m.BranchInventoryComponent)
  },

  // ── Gold Module ──────────────────────────────────────────────
  {
    path: 'gold',
    canActivate: [authGuard, roleGuard],
    data: { module: 'gold' },
    loadComponent: () => import('./features/gold/pages/gold-module.component').then(m => m.GoldModuleComponent)
  },
  {
    path: 'gold/buy',
    canActivate: [authGuard, roleGuard],
    data: { module: 'gold' },
    loadComponent: () => import('./features/gold/pages/gold-buy/gold-buy.component').then(m => m.GoldBuyComponent)
  },
  {
    path: 'gold/sell',
    canActivate: [authGuard, roleGuard],
    data: { module: 'gold' },
    loadComponent: () => import('./features/gold/pages/gold-sell/gold-sell.component').then(m => m.GoldSellComponent)
  },
  {
    path: 'gold/user-virtual',
    canActivate: [authGuard, roleGuard],
    data: { module: 'gold' },
    loadComponent: () => import('./features/gold/pages/user-virtual-gold/user-virtual-gold.component').then(m => m.UserVirtualGoldComponent)
  },
  {
    path: 'gold/branch-virtual',
    canActivate: [authGuard, roleGuard],
    data: { module: 'gold' },
    loadComponent: () => import('./features/gold/pages/branch-virtual-gold/branch-virtual-gold.component').then(m => m.BranchVirtualGoldComponent)
  },
  {
    path: 'gold/convert',
    canActivate: [authGuard, roleGuard],
    data: { module: 'gold' },
    loadComponent: () => import('./features/gold/pages/gold-convert/gold-convert.component').then(m => m.GoldConvertComponent)
  },
  {
    path: 'gold/user-physical',
    canActivate: [authGuard, roleGuard],
    data: { module: 'gold' },
    loadComponent: () => import('./features/gold/pages/user-physical-gold/user-physical-gold.component').then(m => m.UserPhysicalGoldComponent)
  },

  // ── Payment Module ───────────────────────────────────────────
  {
    path: 'payment',
    canActivate: [authGuard, roleGuard],
    data: { module: 'payment' },
    loadComponent: () => import('./features/payment/pages/payment-module.component').then(m => m.PaymentModuleComponent)
  },
  {
    path: 'payment/create',
    canActivate: [authGuard, roleGuard],
    data: { module: 'payment' },
    loadComponent: () => import('./features/payment/pages/payment-create/payment-create.component').then(m => m.PaymentCreateComponent)
  },
  {
    path: 'payment/get',
    canActivate: [authGuard, roleGuard],
    data: { module: 'payment' },
    loadComponent: () => import('./features/payment/pages/payment-get/payment-get.component').then(m => m.PaymentGetComponent)
  },
  {
    path: 'payment/user-payments',
    canActivate: [authGuard, roleGuard],
    data: { module: 'payment' },
    loadComponent: () => import('./features/payment/pages/user-payments/user-payments.component').then(m => m.UserPaymentsComponent)
  },

  // ── Wallet Module ────────────────────────────────────────────
  {
    path: 'wallet',
    redirectTo: 'payment',
    pathMatch: 'full'
  },
  {
    path: 'wallet/credit',
    canActivate: [authGuard, roleGuard],
    data: { module: 'wallet' },
    loadComponent: () => import('./features/wallet/pages/wallet-credit/wallet-credit.component').then(m => m.WalletCreditComponent)
  },
  {
    path: 'wallet/debit',
    canActivate: [authGuard, roleGuard],
    data: { module: 'wallet' },
    loadComponent: () => import('./features/wallet/pages/wallet-debit/wallet-debit.component').then(m => m.WalletDebitComponent)
  },
  {
    path: 'wallet/balance',
    canActivate: [authGuard, roleGuard],
    data: { module: 'wallet' },
    loadComponent: () => import('./features/wallet/pages/wallet-balance/wallet-balance.component').then(m => m.WalletBalanceComponent)
  },

  // ── Transaction Module ───────────────────────────────────────
  {
    path: 'transaction',
    canActivate: [authGuard, roleGuard],
    data: { module: 'transaction' },
    loadComponent: () => import('./features/transaction/pages/transaction-module.component').then(m => m.TransactionModuleComponent)
  },
  {
    path: 'transaction/get',
    canActivate: [authGuard, roleGuard],
    data: { module: 'transaction' },
    loadComponent: () => import('./features/transaction/pages/transaction-get/transaction-get.component').then(m => m.TransactionGetComponent)
  },
  {
    path: 'transaction/user',
    canActivate: [authGuard, roleGuard],
    data: { module: 'transaction' },
    loadComponent: () => import('./features/transaction/pages/user-transactions/user-transactions.component').then(m => m.UserTransactionsComponent)
  },
  {
    path: 'transaction/branch',
    canActivate: [authGuard, roleGuard],
    data: { module: 'transaction' },
    loadComponent: () => import('./features/transaction/pages/branch-transactions/branch-transactions.component').then(m => m.BranchTransactionsComponent)
  },
  {
    path: 'transaction/status',
    canActivate: [authGuard, roleGuard],
    data: { module: 'transaction' },
    loadComponent: () => import('./features/transaction/pages/transaction-status/transaction-status.component').then(m => m.TransactionByStatusComponent)
  },
  {
    path: 'transaction/type',
    canActivate: [authGuard, roleGuard],
    data: { module: 'transaction' },
    loadComponent: () => import('./features/transaction/pages/transaction-type/transaction-type.component').then(m => m.TransactionByTypeComponent)
  },
  {
    path: 'transaction/amount',
    canActivate: [authGuard, roleGuard],
    data: { module: 'transaction' },
    loadComponent: () => import('./features/transaction/pages/transaction-amount/transaction-amount.component').then(m => m.TransactionByAmountComponent)
  },

  { path: '**', redirectTo: 'home' }
];
