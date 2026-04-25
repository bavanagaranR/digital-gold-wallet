import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DeveloperDef } from '../../constants/developers.constant';

@Component({
  selector: 'app-developer-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './developer-card.component.html',
  styleUrl: './developer-card.component.css'
})
export class DeveloperCardComponent {
  @Input() dev!: DeveloperDef;

  moduleRoute(name: string): string {
    const map: Record<string, string> = {
      'User': 'user',
      'Vendor & Branch': 'vendor',
      'Gold (Virtual & Physical)': 'gold',
      'Payment': 'payment',
      'Wallet': 'wallet',
      'Transaction': 'transaction'
    };
    return map[name] || name.toLowerCase();
  }
}
