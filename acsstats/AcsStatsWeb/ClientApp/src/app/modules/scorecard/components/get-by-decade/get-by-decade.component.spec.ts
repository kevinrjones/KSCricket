import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GetByDecadeComponent} from './get-by-decade.component';

describe('GetByDecadeComponent', () => {
  let component: GetByDecadeComponent;
  let fixture: ComponentFixture<GetByDecadeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GetByDecadeComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GetByDecadeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
