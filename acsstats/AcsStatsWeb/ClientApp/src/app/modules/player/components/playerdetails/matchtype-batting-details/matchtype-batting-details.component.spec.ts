import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MatchtypeBattingDetailsComponent} from './matchtype-batting-details.component';

describe('BattingDetailsComponent', () => {
  let component: MatchtypeBattingDetailsComponent;
  let fixture: ComponentFixture<MatchtypeBattingDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MatchtypeBattingDetailsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchtypeBattingDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
