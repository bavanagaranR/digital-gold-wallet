import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { DEVELOPERS } from '../../../../shared/constants/developers.constant';

@Component({
  selector: 'app-endpoint-explorer-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="page-container">
      <div class="mb-12 relative">
        <div class="absolute -top-6 -left-6 w-32 h-32 bg-gold-500/10 rounded-full blur-3xl animate-pulse"></div>
        <h1 class="font-display font-black text-5xl text-maroon-950 tracking-tighter">Endpoint <span class="text-transparent bg-clip-text bg-gradient-to-r from-gold-600 via-gold-400 to-gold-600">Explorer</span></h1>
        <p class="text-maroon-900/60 mt-4 text-sm font-mono tracking-tighter italic">"A comprehensive catalog of every protocol and gateway within the Digital Gold ecosystem."</p>
      </div>

      @for (dev of visibleDevelopers; track dev.id) {
        <div class="mb-16">
          <div class="flex items-center gap-4 mb-8">
            <h2 class="text-2xl font-display font-black text-maroon-950 tracking-tight uppercase tracking-widest">{{ dev.name }}</h2>
            <div class="h-px bg-gold-500/20 flex-1"></div>
            <span class="text-[10px] font-black bg-maroon-950 text-gold-500 px-3 py-1 rounded-full border border-gold-500/30 uppercase tracking-widest">{{ dev.modules.length }} Modules</span>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            @for (mod of dev.modules; track mod.name) {
              <div class="card !p-0 overflow-hidden border-gold-200/30 shadow-2xl group">
                <div class="bg-maroon-950 p-6 border-b border-gold-500/10">
                  <h3 class="text-gold-100 font-black text-lg tracking-tight mb-1">{{ mod.name }}</h3>
                  <p class="text-[9px] font-mono text-gold-500/50 uppercase tracking-widest">{{ mod.packageName }}</p>
                  <p class="text-gold-100/60 text-xs mt-3 leading-relaxed font-medium italic">{{ mod.description }}</p>
                </div>
                <div class="p-4 space-y-3 bg-white/50 backdrop-blur-sm">
                  @for (ep of mod.endpoints; track ep.path) {
                    <div class="flex items-center gap-4 p-4 bg-white border border-gold-100 rounded-xl hover:shadow-xl hover:border-gold-500/30 transition-all duration-300 group/item">
                      <span [class]="'badge-' + ep.method.toLowerCase() + ' !rounded-md !px-3 !py-1 !text-[10px] !font-black !tracking-widest shrink-0'">{{ ep.method }}</span>
                      <div class="flex-1 min-w-0">
                        <code class="text-[11px] font-bold text-maroon-950 break-all bg-maroon-50 px-2 py-0.5 rounded">{{ ep.path }}</code>
                        <p class="text-[10px] text-maroon-900/40 mt-1.5 font-bold uppercase tracking-widest">{{ ep.purpose }}</p>
                      </div>
                      <a [routerLink]="ep.page" class="w-10 h-10 rounded-full bg-maroon-50 flex items-center justify-center text-maroon-900 group-hover/item:bg-maroon-900 group-hover/item:text-gold-400 transition-all duration-500">
                        <span class="text-xs">→</span>
                      </a>
                    </div>
                  }
                </div>
              </div>
            }
          </div>
        </div>
      }
    </div>
  `
})
export class EndpointExplorerPageComponent {
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
