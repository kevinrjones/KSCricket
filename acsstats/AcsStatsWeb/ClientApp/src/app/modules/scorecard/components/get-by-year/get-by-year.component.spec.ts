import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GetByYearComponent} from './get-by-year.component';

describe('GetByYearComponent', () => {
  let component: GetByYearComponent;
  let fixture: ComponentFixture<GetByYearComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GetByYearComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GetByYearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
