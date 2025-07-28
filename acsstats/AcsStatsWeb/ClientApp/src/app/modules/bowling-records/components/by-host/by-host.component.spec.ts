import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ByHostComponent} from './by-host.component';

describe('ByHostComponent', () => {
  let component: ByHostComponent;
  let fixture: ComponentFixture<ByHostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ByHostComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ByHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
