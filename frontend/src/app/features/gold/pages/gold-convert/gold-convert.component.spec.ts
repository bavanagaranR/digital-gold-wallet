import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GoldConvertComponent } from './gold-convert.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('GoldConvertComponent', () => {
  let component: GoldConvertComponent;
  let fixture: ComponentFixture<GoldConvertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoldConvertComponent, HttpClientTestingModule, ReactiveFormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GoldConvertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
