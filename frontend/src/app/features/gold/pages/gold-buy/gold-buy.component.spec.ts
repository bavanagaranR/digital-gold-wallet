import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GoldBuyComponent } from './gold-buy.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('GoldBuyComponent', () => {
  let component: GoldBuyComponent;
  let fixture: ComponentFixture<GoldBuyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoldBuyComponent, HttpClientTestingModule, ReactiveFormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GoldBuyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
