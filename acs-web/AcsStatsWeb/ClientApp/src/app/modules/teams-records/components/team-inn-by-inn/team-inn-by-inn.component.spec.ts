import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BattingInnByInnComponent} from './batting-inn-by-inn.component';

describe('BattingInnByInnComponent', () => {
  let component: BattingInnByInnComponent;
  let fixture: ComponentFixture<BattingInnByInnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BattingInnByInnComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BattingInnByInnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
