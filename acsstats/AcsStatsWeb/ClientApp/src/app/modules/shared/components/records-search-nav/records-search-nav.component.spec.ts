import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RecordsHeaderComponent} from './records-header.component';

describe('RecordsHeaderComponent', () => {
  let component: RecordsHeaderComponent;
  let fixture: ComponentFixture<RecordsHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RecordsHeaderComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RecordsHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
