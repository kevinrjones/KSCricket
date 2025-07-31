import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GetBattingRecordsComponent} from './get-batting-records.component';

describe('GetBattingRecordsComponent', () => {
  let component: GetBattingRecordsComponent;
  let fixture: ComponentFixture<GetBattingRecordsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GetBattingRecordsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GetBattingRecordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
