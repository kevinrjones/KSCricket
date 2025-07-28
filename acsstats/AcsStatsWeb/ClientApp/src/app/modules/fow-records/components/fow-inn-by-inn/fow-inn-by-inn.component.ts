import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {InningsByInningsUiModel} from '../../models/fow-overall-ui.model';
import {Observable} from 'rxjs';
import {FowOverallState} from '../../models/app-state';
import {LoadInnByInnForWicketFowRecordsAction, LoadInnByInnFowRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-fow-inn-by-inn',
    templateUrl: './fow-inn-by-inn.component.html',
    styleUrls: ['./fow-inn-by-inn.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class FowInnByInnComponent implements OnInit {

  title!: string;
  baseTitle = "Innings by Innings Partnership Figures";
  fowInnByInn$!: Observable<InningsByInningsUiModel>;
  private isAllPartnerships!: boolean;

  constructor(private fowStore: Store<FowOverallState>) {
  }

  ngOnInit(): void {
    this.fowInnByInn$ = this.fowStore.select(s => {
        return s.fowrecords.inningsByInnings
      }
    )
  }

  dispatch(findRecordsPayload: { payload: FindRecords }) {
    let partnershipWicket = parseInt(findRecordsPayload.payload.partnershipWicket)
    if (partnershipWicket > 0) {
      this.title = `${this.baseTitle} for the ${this.getPartnershipWicket(partnershipWicket)} Wicket`
      this.fowStore.dispatch(LoadInnByInnForWicketFowRecordsAction(findRecordsPayload))
      this.isAllPartnerships = false
    } else {
      this.title = `${this.baseTitle}  for All Wickets`
      this.fowStore.dispatch(LoadInnByInnFowRecordsAction(findRecordsPayload))
      this.isAllPartnerships = true
    }
  }

  getSearchType() {
    return this.isAllPartnerships ? 'inningsByInnings' : 'inningsByInningsForWicket'
  }

  private getPartnershipWicket(partnershipWicket: number): string {
    switch (partnershipWicket) {
      case 1:
        return '1st'
      case 2:
        return '2nd'
      case 3:
        return '3rd'
      default:
        return `${partnershipWicket}th`
    }
  }
}
