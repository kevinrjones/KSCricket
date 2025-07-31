import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GetBowlingRecordsComponent} from './get-bowling-records.component';

describe('GetBowlingRecordsComponent', () => {
  let component: GetBowlingRecordsComponent;
  let fixture: ComponentFixture<GetBowlingRecordsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GetBowlingRecordsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GetBowlingRecordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
