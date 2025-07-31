import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BattingDetailsComponent} from './batting-details.component';

describe('BattingDetailsComponent', () => {
  let component: BattingDetailsComponent;
  let fixture: ComponentFixture<BattingDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BattingDetailsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BattingDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
