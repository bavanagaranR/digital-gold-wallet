import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-page-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './page-header.component.html',
  styleUrl: './page-header.component.css'
})
export class PageHeaderComponent {
  @Input() title = '';
  @Input() developer = '';
  @Input() moduleName = '';
  @Input() method: 'GET'|'POST'|'PUT'|'DELETE' = 'GET';
  @Input() endpoint = '';
  @Input() description = '';
}
