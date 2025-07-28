import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GetScorecardListComponent} from './get-scorecard-list.component';

describe('GetScorecardListComponent', () => {
  let component: GetScorecardListComponent;
  let fixture: ComponentFixture<GetScorecardListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GetScorecardListComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GetScorecardListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
