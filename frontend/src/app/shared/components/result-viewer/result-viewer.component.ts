import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-result-viewer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './result-viewer.component.html',
  styleUrl: './result-viewer.component.css'
})
export class ResultViewerComponent {
  @Input() data: unknown = null;
  @Input() error = '';
  @Input() errorTitle = 'Operation Failed';
  @Input() errorType: 'warning' | 'danger' | null = null;
  @Input() statusCode: number | null = null;
  @Input() loading = false;
  @Input() errorStatusCode: number | null = null;
}
