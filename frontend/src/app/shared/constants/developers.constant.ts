export interface EndpointDef {
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
  path: string;
  purpose: string;
  page: string;
}

export interface ModuleDef {
  name: string;
  packageName: string;
  description: string;
  endpoints: EndpointDef[];
}

export interface DeveloperDef {
  id: string;
  name: string;
  modules: ModuleDef[];
  color: string;
}

export const DEVELOPERS: DeveloperDef[] = [
  {
    id: 'pavithra',
    name: 'Pavithra',
    color: 'blue',
    modules: [
      {
        name: 'User',
        packageName: 'com.goldwallet.digitalgoldwallet.modules.user',
        description: 'Manages user creation, retrieval, update, wallet balance view, and address management. Supports creating new users, fetching users individually or as a paginated list, updating user details, checking a user\'s current wallet balance, and maintaining address records linked to users.',
        endpoints: [
          { method: 'POST', path: '/api/v1/users', purpose: 'Create a new user record', page: '/user/create' },
          { method: 'GET', path: '/api/v1/users/{id}', purpose: 'Fetch complete details of a single user by ID', page: '/user/get' },
          { method: 'GET', path: '/api/v1/users', purpose: 'Retrieve all users with pagination', page: '/user/list' },
          { method: 'PUT', path: '/api/v1/users/{id}', purpose: 'Update existing user details', page: '/user/update' },
          { method: 'GET', path: '/api/v1/users/{id}/balance', purpose: 'View a user\'s wallet balance', page: '/user/balance' },
          { method: 'POST', path: '/api/v1/addresses', purpose: 'Create an address record', page: '/user/address-create' },
          { method: 'GET', path: '/api/v1/addresses/{id}', purpose: 'Fetch a specific address by ID', page: '/user/address-get' },
          { method: 'PUT', path: '/api/v1/addresses/{id}', purpose: 'Update address information', page: '/user/address-update' },
        ]
      }
    ]
  },
  {
    id: 'caitlyn',
    name: 'Caitlyn Mary',
    color: 'purple',
    modules: [
      {
        name: 'Vendor & Branch',
        packageName: 'com.goldwallet.digitalgoldwallet.modules.vendor',
        description: 'Manages vendor information and their branches. Supports creating vendors, listing all vendors, fetching a vendor by ID, updating vendor details, checking vendor gold price, adding branches to vendors, viewing branch details, listing branches under a vendor, and checking branch inventory.',
        endpoints: [
          { method: 'POST', path: '/api/v1/vendors', purpose: 'Create a new vendor', page: '/vendor/create' },
          { method: 'GET', path: '/api/v1/vendors/{id}', purpose: 'Fetch vendor details by ID', page: '/vendor/get' },
          { method: 'GET', path: '/api/v1/vendors', purpose: 'Retrieve all vendors', page: '/vendor/list' },
          { method: 'PUT', path: '/api/v1/vendors/{id}', purpose: 'Update vendor information', page: '/vendor/update' },
          { method: 'GET', path: '/api/v1/vendors/{id}/price', purpose: 'Get the vendor\'s current gold price', page: '/vendor/price' },
          { method: 'POST', path: '/api/v1/vendors/{vendorId}/branches', purpose: 'Add a new branch under a vendor', page: '/vendor/branch-create' },
          { method: 'GET', path: '/api/v1/branches/{branchId}', purpose: 'Get branch details by branch ID', page: '/vendor/branch-get' },
          { method: 'GET', path: '/api/v1/vendors/{vendorId}/branches', purpose: 'List all branches belonging to a vendor', page: '/vendor/branch-list' },
          { method: 'GET', path: '/api/v1/branches/{branchId}/inventory', purpose: 'View branch inventory quantity', page: '/vendor/branch-inventory' },
        ]
      }
    ]
  },
  {
    id: 'suba',
    name: 'Suba Harini',
    color: 'yellow',
    modules: [
      {
        name: 'Gold (Virtual & Physical)',
        packageName: 'com.goldwallet.digitalgoldwallet.modules.gold',
        description: 'Handles virtual gold purchase and sale, viewing holdings, and converting virtual gold into physical gold. Allows users to buy and sell digital gold, inspect user-wise and branch-wise gold holdings, convert digital gold into physical gold requests, and view physical gold conversion records.',
        endpoints: [
          { method: 'POST', path: '/api/v1/gold/virtual/buy', purpose: 'Buy virtual gold', page: '/gold/buy' },
          { method: 'POST', path: '/api/v1/gold/virtual/sell', purpose: 'Sell virtual gold', page: '/gold/sell' },
          { method: 'GET', path: '/api/v1/users/{userId}/gold/virtual', purpose: 'View a user\'s virtual gold holdings', page: '/gold/user-virtual' },
          { method: 'GET', path: '/api/v1/branches/{branchId}/gold/virtual', purpose: 'View branch virtual gold holdings', page: '/gold/branch-virtual' },
          { method: 'POST', path: '/api/v1/gold/physical/convert', purpose: 'Convert virtual gold into physical gold', page: '/gold/convert' },
          { method: 'GET', path: '/api/v1/users/{userId}/gold/physical', purpose: 'View a user\'s physical gold conversion records', page: '/gold/user-physical' },
        ]
      }
    ]
  },
  {
    id: 'bavanagaran',
    name: 'Bavanagaran',
    color: 'green',
    modules: [
      {
        name: 'Payment',
        packageName: 'com.goldwallet.digitalgoldwallet.modules.payment',
        description: 'Manages payment processing. Supports initiating a payment, fetching a payment by ID, and listing all payments of a user.',
        endpoints: [
          { method: 'POST', path: '/api/v1/payments', purpose: 'Initiate a payment transaction', page: '/payment/create' },
          { method: 'GET', path: '/api/v1/payments/{id}', purpose: 'Fetch payment details by payment ID', page: '/payment/get' },
          { method: 'GET', path: '/api/v1/users/{userId}/payments', purpose: 'Retrieve all payments made by a user', page: '/payment/user-payments' },
        ]
      },
      {
        name: 'Wallet',
        packageName: 'com.goldwallet.digitalgoldwallet.modules.wallet',
        description: 'Manages wallet operations including crediting, debiting, and balance check.',
        endpoints: [
          { method: 'POST', path: '/api/v1/wallets/{userId}/credit', purpose: 'Add amount to the wallet', page: '/wallet/credit' },
          { method: 'POST', path: '/api/v1/wallets/{userId}/debit', purpose: 'Deduct amount from the wallet', page: '/wallet/debit' },
          { method: 'GET', path: '/api/v1/wallets/{userId}/balance', purpose: 'Get current wallet balance', page: '/wallet/balance' },
        ]
      }
    ]
  },
  {
    id: 'mirudhula',
    name: 'Mirudhula',
    color: 'red',
    modules: [
      {
        name: 'Transaction',
        packageName: 'com.goldwallet.digitalgoldwallet.modules.transaction',
        description: 'Provides comprehensive transaction history viewing features. Allows fetching a specific transaction, retrieving all transactions of a user, retrieving all transactions associated with a branch, and filtering transactions by status, type, or amount.',
        endpoints: [
          { method: 'GET', path: '/api/v1/transactions/{id}', purpose: 'Fetch transaction details by transaction ID', page: '/transaction/get' },
          { method: 'GET', path: '/api/v1/users/{userId}/transactions', purpose: 'List all transactions of a particular user', page: '/transaction/user' },
          { method: 'GET', path: '/api/v1/branches/{branchId}/transactions', purpose: 'List all transactions of a branch', page: '/transaction/branch' },
          { method: 'GET', path: '/api/v1/transactions/status/{status}', purpose: 'Filter transactions by status (SUCCESS/FAILED)', page: '/transaction/status' },
          { method: 'GET', path: '/api/v1/transactions/type/{type}', purpose: 'Filter transactions by type (BUY_GOLD, SELL_GOLD, etc.)', page: '/transaction/type' },
          { method: 'GET', path: '/api/v1/transactions/amount', purpose: 'List transactions greater than a specified amount', page: '/transaction/amount' },
        ]
      }
    ]
  }
];
