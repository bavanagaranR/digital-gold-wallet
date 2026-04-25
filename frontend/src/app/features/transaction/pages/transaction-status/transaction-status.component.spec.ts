import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransactionByStatusComponent } from './transaction-status.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('TransactionByStatusComponent', () => {
  let component: TransactionByStatusComponent;
  let fixture: ComponentFixture<TransactionByStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionByStatusComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionByStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
