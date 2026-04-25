import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DeveloperCardComponent } from './developer-card.component';
import { RouterModule } from '@angular/router';

describe('DeveloperCardComponent', () => {
  let component: DeveloperCardComponent;
  let fixture: ComponentFixture<DeveloperCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeveloperCardComponent, RouterModule.forRoot([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeveloperCardComponent);
    component = fixture.componentInstance;
    // Mock input
    component.dev = { name: 'Test', modules: [] } as any;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
