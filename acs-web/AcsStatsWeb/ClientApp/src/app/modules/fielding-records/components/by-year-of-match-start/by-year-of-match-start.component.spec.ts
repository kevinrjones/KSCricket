import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ByYearOfMatchStartComponent} from './by-year-of-match-start.component';

describe('ByYearOfMatchStartComponent', () => {
  let component: ByYearOfMatchStartComponent;
  let fixture: ComponentFixture<ByYearOfMatchStartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ByYearOfMatchStartComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ByYearOfMatchStartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
