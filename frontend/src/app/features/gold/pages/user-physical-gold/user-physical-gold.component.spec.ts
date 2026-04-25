import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserPhysicalGoldComponent } from './user-physical-gold.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('UserPhysicalGoldComponent', () => {
  let component: UserPhysicalGoldComponent;
  let fixture: ComponentFixture<UserPhysicalGoldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserPhysicalGoldComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserPhysicalGoldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
