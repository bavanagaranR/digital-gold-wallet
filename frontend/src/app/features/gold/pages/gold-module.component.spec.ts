import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GoldModuleComponent } from './gold-module.component';
import { RouterModule } from '@angular/router';

describe('GoldModuleComponent', () => {
  let component: GoldModuleComponent;
  let fixture: ComponentFixture<GoldModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoldModuleComponent, RouterModule.forRoot([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GoldModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
