import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ByOppositionComponent} from './by-opposition.component';

describe('ByOppositionComponent', () => {
  let component: ByOppositionComponent;
  let fixture: ComponentFixture<ByOppositionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ByOppositionComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ByOppositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
