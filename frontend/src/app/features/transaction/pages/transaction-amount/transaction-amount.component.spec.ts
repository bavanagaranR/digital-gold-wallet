import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransactionByAmountComponent } from './transaction-amount.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('TransactionByAmountComponent', () => {
  let component: TransactionByAmountComponent;
  let fixture: ComponentFixture<TransactionByAmountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionByAmountComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionByAmountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
