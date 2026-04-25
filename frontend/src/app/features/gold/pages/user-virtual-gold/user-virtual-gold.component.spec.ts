import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserVirtualGoldComponent } from './user-virtual-gold.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('UserVirtualGoldComponent', () => {
  let component: UserVirtualGoldComponent;
  let fixture: ComponentFixture<UserVirtualGoldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserVirtualGoldComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserVirtualGoldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
