import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddressGetComponent } from './address-get.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('AddressGetComponent', () => {
  let component: AddressGetComponent;
  let fixture: ComponentFixture<AddressGetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddressGetComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressGetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
