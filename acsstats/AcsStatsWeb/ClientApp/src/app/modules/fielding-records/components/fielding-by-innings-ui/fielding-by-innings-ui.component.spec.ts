import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FieldingByInningsUiComponent} from './fielding-by-innings-ui.component';

describe('FieldingByInningsUiComponent', () => {
  let component: FieldingByInningsUiComponent;
  let fixture: ComponentFixture<FieldingByInningsUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FieldingByInningsUiComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FieldingByInningsUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
