import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserGetComponent } from './user-get.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('UserGetComponent', () => {
  let component: UserGetComponent;
  let fixture: ComponentFixture<UserGetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserGetComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserGetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
