import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BowlingDetailsComponent} from './bowling-details.component';

describe('BowlingDetailsComponent', () => {
  let component: BowlingDetailsComponent;
  let fixture: ComponentFixture<BowlingDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BowlingDetailsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BowlingDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
