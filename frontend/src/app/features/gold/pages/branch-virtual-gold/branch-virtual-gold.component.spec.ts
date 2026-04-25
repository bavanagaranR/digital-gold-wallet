import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchVirtualGoldComponent } from './branch-virtual-gold.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('BranchVirtualGoldComponent', () => {
  let component: BranchVirtualGoldComponent;
  let fixture: ComponentFixture<BranchVirtualGoldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchVirtualGoldComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BranchVirtualGoldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
