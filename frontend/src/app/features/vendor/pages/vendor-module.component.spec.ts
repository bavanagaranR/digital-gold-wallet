import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VendorModuleComponent } from './vendor-module.component';
import { RouterModule } from '@angular/router';

describe('VendorModuleComponent', () => {
  let component: VendorModuleComponent;
  let fixture: ComponentFixture<VendorModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendorModuleComponent, RouterModule.forRoot([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendorModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
