import {ComponentFixture, TestBed} from '@angular/core/testing';
import {TeamAggregateRecordUiComponent} from './team-aggregate-record-ui.component';

describe('TeamAggregateRecordUiComponent', () => {
  let component: TeamAggregateRecordUiComponent;
  let fixture: ComponentFixture<TeamAggregateRecordUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeamAggregateRecordUiComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TeamAggregateRecordUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
