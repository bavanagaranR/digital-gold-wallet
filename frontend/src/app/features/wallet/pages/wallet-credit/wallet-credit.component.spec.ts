import { ComponentFixture, TestBed } from '@angular/core/testing';
import { WalletCreditComponent } from './wallet-credit.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

describe('WalletCreditComponent', () => {
  let component: WalletCreditComponent;
  let fixture: ComponentFixture<WalletCreditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WalletCreditComponent, HttpClientTestingModule, ReactiveFormsModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WalletCreditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
