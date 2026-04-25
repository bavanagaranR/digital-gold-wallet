import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchTransactionsComponent } from './branch-transactions.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('BranchTransactionsComponent', () => {
  let component: BranchTransactionsComponent;
  let fixture: ComponentFixture<BranchTransactionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchTransactionsComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BranchTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
