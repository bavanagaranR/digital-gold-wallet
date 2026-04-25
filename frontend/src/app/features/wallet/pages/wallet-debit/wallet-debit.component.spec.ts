import { ComponentFixture, TestBed } from '@angular/core/testing';
import { WalletDebitComponent } from './wallet-debit.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

describe('WalletDebitComponent', () => {
  let component: WalletDebitComponent;
  let fixture: ComponentFixture<WalletDebitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WalletDebitComponent, HttpClientTestingModule, ReactiveFormsModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WalletDebitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
