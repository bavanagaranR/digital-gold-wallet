import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransactionModuleComponent } from './transaction-module.component';
import { RouterModule } from '@angular/router';

describe('TransactionModuleComponent', () => {
  let component: TransactionModuleComponent;
  let fixture: ComponentFixture<TransactionModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionModuleComponent, RouterModule.forRoot([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
