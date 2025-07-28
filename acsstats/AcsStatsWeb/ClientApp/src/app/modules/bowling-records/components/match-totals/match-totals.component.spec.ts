import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MatchTotalsComponent} from './match-totals.component';

describe('MatchTotalsComponent', () => {
  let component: MatchTotalsComponent;
  let fixture: ComponentFixture<MatchTotalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MatchTotalsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MatchTotalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
