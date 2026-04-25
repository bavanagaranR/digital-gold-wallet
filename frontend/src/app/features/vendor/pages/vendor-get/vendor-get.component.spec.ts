import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VendorGetComponent } from './vendor-get.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('VendorGetComponent', () => {
  let component: VendorGetComponent;
  let fixture: ComponentFixture<VendorGetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendorGetComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendorGetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
