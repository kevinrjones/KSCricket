import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BowlingAggregateRecordUiComponent} from './bowling-aggregate-record-ui.component';

describe('BowlingAggregateRecordUiComponent', () => {
  let component: BowlingAggregateRecordUiComponent;
  let fixture: ComponentFixture<BowlingAggregateRecordUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BowlingAggregateRecordUiComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BowlingAggregateRecordUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
