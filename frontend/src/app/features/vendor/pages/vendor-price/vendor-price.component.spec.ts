import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VendorPriceComponent } from './vendor-price.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('VendorPriceComponent', () => {
  let component: VendorPriceComponent;
  let fixture: ComponentFixture<VendorPriceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendorPriceComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendorPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
