import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GetFieldingRecordsComponent} from './get-fielding-records.component';

describe('GetFieldingRecordsComponent', () => {
  let component: GetFieldingRecordsComponent;
  let fixture: ComponentFixture<GetFieldingRecordsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GetFieldingRecordsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GetFieldingRecordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
