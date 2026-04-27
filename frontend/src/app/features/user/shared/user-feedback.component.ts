import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { UserFeedbackState, createEmptyUserFeedbackState } from './user-form-support';

@Component({
  selector: 'app-user-feedback',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-feedback.component.html',
  styleUrl: './user-feedback.component.css',
})
export class UserFeedbackComponent {
  @Input() loading = false;
  @Input() feedback: UserFeedbackState = createEmptyUserFeedbackState();
}

