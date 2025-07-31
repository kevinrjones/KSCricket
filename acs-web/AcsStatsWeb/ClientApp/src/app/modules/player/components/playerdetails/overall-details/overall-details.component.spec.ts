import {ComponentFixture, TestBed} from '@angular/core/testing';
import {OverallDetailsComponent} from './overall-details.component';


describe('OverallDetailsComponent', () => {
  let component: OverallDetailsComponent;
  let fixture: ComponentFixture<OverallDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OverallDetailsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OverallDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
