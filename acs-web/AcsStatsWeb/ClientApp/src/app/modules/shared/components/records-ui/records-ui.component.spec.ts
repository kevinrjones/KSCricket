import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RecordsUIComponent} from './records-ui.component';

describe('RecordsUIComponent', () => {
  let component: RecordsUIComponent;
  let fixture: ComponentFixture<RecordsUIComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RecordsUIComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RecordsUIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
