import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BattingSummaryComponent} from './batting-summary.component';

describe('BattingSummaryComponent', () => {
  let component: BattingSummaryComponent;
  let fixture: ComponentFixture<BattingSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BattingSummaryComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BattingSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
