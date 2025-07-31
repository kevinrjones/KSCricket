import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BowlingByInningsUiComponent} from './bowling-by-innings-ui.component';

describe('BowlingByInningsUiComponent', () => {
  let component: BowlingByInningsUiComponent;
  let fixture: ComponentFixture<BowlingByInningsUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BowlingByInningsUiComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BowlingByInningsUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
