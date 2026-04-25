import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { DEVELOPERS } from '../../../../shared/constants/developers.constant';
import { DeveloperCardComponent } from '../../../../shared/components/developer-card/developer-card.component';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, RouterLink, DeveloperCardComponent],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent {
  auth = inject(AuthService);
  developers = DEVELOPERS;

  get visibleDevelopers() {
    if (this.auth.currentUser?.role === 'admin') return this.developers;
    const moduleMap: Record<string, string> = {
      'User': 'user',
      'Vendor & Branch': 'vendor',
      'Gold (Virtual & Physical)': 'gold',
      'Payment': 'payment',
      'Wallet': 'wallet',
      'Transaction': 'transaction'
    };
    return this.developers
      .map(dev => ({
        ...dev,
        modules: dev.modules.filter(m => this.auth.canAccessModule(moduleMap[m.name] || m.name.toLowerCase()))
      }))
      .filter(dev => dev.modules.length > 0);
  }
}
