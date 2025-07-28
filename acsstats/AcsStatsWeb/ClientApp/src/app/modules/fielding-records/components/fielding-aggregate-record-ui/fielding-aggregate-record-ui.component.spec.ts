import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FieldingAggregateRecordUiComponent} from './fielding-aggregate-record-ui.component';

describe('FieldingAggregateRecordUiComponent', () => {
  let component: FieldingAggregateRecordUiComponent;
  let fixture: ComponentFixture<FieldingAggregateRecordUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FieldingAggregateRecordUiComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FieldingAggregateRecordUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
