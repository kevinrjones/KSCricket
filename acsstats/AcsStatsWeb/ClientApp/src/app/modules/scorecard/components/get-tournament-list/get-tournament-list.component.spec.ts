import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GetTournamentListComponent} from './get-tournament-list.component';

describe('GetTournamentListComponent', () => {
  let component: GetTournamentListComponent;
  let fixture: ComponentFixture<GetTournamentListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GetTournamentListComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GetTournamentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
