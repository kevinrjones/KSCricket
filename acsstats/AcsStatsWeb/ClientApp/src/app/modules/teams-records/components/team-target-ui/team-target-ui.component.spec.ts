import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BattingByInningsUiComponent} from './batting-by-innings-ui.component';

describe('BattingByInningsUiComponent', () => {
  let component: BattingByInningsUiComponent;
  let fixture: ComponentFixture<BattingByInningsUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BattingByInningsUiComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BattingByInningsUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
