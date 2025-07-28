import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BattingOverallComponent} from './batting-overall.component';

describe('BattingOverallComponent', () => {
  let component: BattingOverallComponent;
  let fixture: ComponentFixture<BattingOverallComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BattingOverallComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BattingOverallComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
