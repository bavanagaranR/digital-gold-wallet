import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddressUpdateComponent } from './address-update.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

describe('AddressUpdateComponent', () => {
  let component: AddressUpdateComponent;
  let fixture: ComponentFixture<AddressUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddressUpdateComponent, HttpClientTestingModule, ReactiveFormsModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
