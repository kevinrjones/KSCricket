import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BowlingInnByInnComponent} from './bowling-inn-by-inn.component';

describe('BowlingInnByInnComponent', () => {
  let component: BowlingInnByInnComponent;
  let fixture: ComponentFixture<BowlingInnByInnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BowlingInnByInnComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BowlingInnByInnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
