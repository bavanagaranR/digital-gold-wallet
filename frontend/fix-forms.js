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
  const filePath = path.join(basePath, f);
  if (!fs.existsSync(filePath)) return;
  
  let content = fs.readFileSync(filePath, 'utf8');
  
  // Guarantee FormsModule import
  if (!content.includes('import { FormsModule }')) {
    if (content.includes('import { FormsModule,')) {
      // already there
    } else if (content.includes('import { ReactiveFormsModule, FormsModule }')) {
      // already there
    } else if (content.includes('import { ReactiveFormsModule')) {
       content = content.replace(/import { ReactiveFormsModule(.*?)} from '@angular\/forms';/, "import { ReactiveFormsModule$1, FormsModule } from '@angular/forms';");
    } else {
       // Just prepend it
       content = "import { FormsModule } from '@angular/forms';\n" + content;
    }
  }

  // Ensure it's in the component imports array
  // Some might have been partially changed
  
  fs.writeFileSync(filePath, content);
  console.log('Fixed imports:', f);
});
