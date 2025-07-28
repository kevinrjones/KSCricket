import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SeriesAveragesComponent} from './series-averages.component';

describe('SeriesAveragesComponent', () => {
  let component: SeriesAveragesComponent;
  let fixture: ComponentFixture<SeriesAveragesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SeriesAveragesComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(SeriesAveragesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
