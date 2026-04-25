import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchInventoryComponent } from './branch-inventory.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('BranchInventoryComponent', () => {
  let component: BranchInventoryComponent;
  let fixture: ComponentFixture<BranchInventoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchInventoryComponent, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BranchInventoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
