const fs = require('fs');
const path = require('path');

const files = [
  'vendor/pages/vendor-pages.component.ts',
  'wallet/pages/wallet-pages.component.ts',
  'transaction/pages/transaction-pages.component.ts',
  'user/pages/user-pages.component.ts',
  'gold/pages/gold-pages.component.ts',
  'payment/pages/payment-pages.component.ts'
];

const basePath = 'c:/Users/mirud/OneDrive/Desktop/testing/digital-gold-wallet-frontend (1)/frontend/src/app/features/';

files.forEach(f => {
  const p = path.join(basePath, f);
  if (!fs.existsSync(p)) return;
  
  let content = fs.readFileSync(p, 'utf8');
  
  // 1. Remove all standalone FormsModule imports
  content = content.replace(/import\s*{\s*FormsModule\s*}\s*from\s*'@angular\/forms';\n?/g, '');
  
  // 2. Remove FormsModule from any existing @angular/forms import
  content = content.replace(/FormsModule\s*,?\s*/g, '');
  
  // Cleanup dangling commas in imports array or import statements
  content = content.replace(/,\s*,/g, ',');
  content = content.replace(/{\s*,/g, '{ ');
  content = content.replace(/,\s*}/g, ' }');
  content = content.replace(/\[\s*,/g, '[');
  content = content.replace(/,\s*\]/g, ']');

  // Ensure @angular/forms is imported
  if (!content.includes("@angular/forms")) {
     content = "import { FormsModule } from '@angular/forms';\n" + content;
  } else {
     content = content.replace(/from\s*'@angular\/forms';/, ", FormsModule } from '@angular/forms';").replace(/}\s*,\s*FormsModule/, ", FormsModule }");
  }
  
  // Add to component imports:
  content = content.replace(/imports:\s*\[/g, "imports: [FormsModule, ");
  
  fs.writeFileSync(p, content);
  console.log('Cleaned and fixed:', f);
});
