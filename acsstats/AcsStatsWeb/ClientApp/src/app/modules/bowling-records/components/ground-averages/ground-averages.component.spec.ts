import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GroundAveragesComponent} from './ground-averages.component';

describe('GroundAveragesComponent', () => {
  let component: GroundAveragesComponent;
  let fixture: ComponentFixture<GroundAveragesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GroundAveragesComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GroundAveragesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
