import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransactionGetComponent } from './transaction-get.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('TransactionGetComponent', () => {
  let component: TransactionGetComponent;
  let fixture: ComponentFixture<TransactionGetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionGetComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionGetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
