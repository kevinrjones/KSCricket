import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BattingAggregateRecordUiComponent} from './batting-aggregate-record-ui.component';

describe('BattingAggregateRecordUiComponent', () => {
  let component: BattingAggregateRecordUiComponent;
  let fixture: ComponentFixture<BattingAggregateRecordUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BattingAggregateRecordUiComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BattingAggregateRecordUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
