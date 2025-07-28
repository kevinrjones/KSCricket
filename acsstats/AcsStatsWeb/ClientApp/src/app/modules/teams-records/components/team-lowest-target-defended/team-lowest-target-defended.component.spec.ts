import {ComponentFixture, TestBed} from '@angular/core/testing';
import {TeamLowestTargetDefendedComponent} from './team-lowest-target-defended.component';


describe('TeamLowestTargetDefendedComponent', () => {
  let component: TeamLowestTargetDefendedComponent;
  let fixture: ComponentFixture<TeamLowestTargetDefendedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeamLowestTargetDefendedComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TeamLowestTargetDefendedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
