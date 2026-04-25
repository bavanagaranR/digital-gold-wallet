import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchGetComponent } from './branch-get.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('BranchGetComponent', () => {
  let component: BranchGetComponent;
  let fixture: ComponentFixture<BranchGetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchGetComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BranchGetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
