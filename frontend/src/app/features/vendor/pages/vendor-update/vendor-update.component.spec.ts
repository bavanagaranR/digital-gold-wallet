import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VendorUpdateComponent } from './vendor-update.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

describe('VendorUpdateComponent', () => {
  let component: VendorUpdateComponent;
  let fixture: ComponentFixture<VendorUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendorUpdateComponent, HttpClientTestingModule, ReactiveFormsModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendorUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
