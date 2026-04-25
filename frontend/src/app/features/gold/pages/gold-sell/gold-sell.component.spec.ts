import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GoldSellComponent } from './gold-sell.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('GoldSellComponent', () => {
  let component: GoldSellComponent;
  let fixture: ComponentFixture<GoldSellComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoldSellComponent, HttpClientTestingModule, ReactiveFormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GoldSellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
