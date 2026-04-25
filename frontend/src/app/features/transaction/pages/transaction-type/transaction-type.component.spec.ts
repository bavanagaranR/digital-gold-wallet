import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransactionByTypeComponent } from './transaction-type.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('TransactionByTypeComponent', () => {
  let component: TransactionByTypeComponent;
  let fixture: ComponentFixture<TransactionByTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionByTypeComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionByTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
