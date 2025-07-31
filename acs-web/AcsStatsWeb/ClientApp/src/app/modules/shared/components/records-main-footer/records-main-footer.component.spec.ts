import {ComponentFixture, TestBed} from '@angular/core/testing';
import {RecordsMainNavComponent} from '../records-main-nav/records-main-nav.component';


describe('RecordsHeaderComponent', () => {
  let component: RecordsMainNavComponent;
  let fixture: ComponentFixture<RecordsMainNavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RecordsMainNavComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RecordsMainNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
