import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RecordsSearchSelectComponent} from './records-search-select.component';

describe('SearchHeaderComponent', () => {
  let component: RecordsSearchSelectComponent;
  let fixture: ComponentFixture<RecordsSearchSelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RecordsSearchSelectComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RecordsSearchSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
