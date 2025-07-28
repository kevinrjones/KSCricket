import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BySeasonComponent} from './by-season.component';

describe('BySeasonComponent', () => {
  let component: BySeasonComponent;
  let fixture: ComponentFixture<BySeasonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BySeasonComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BySeasonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
