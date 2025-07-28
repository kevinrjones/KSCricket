import {AfterViewInit, ChangeDetectionStrategy, Component, ViewChild} from '@angular/core';
import {FormHelperService} from '../../../../services/form-helper.service';
import {FormGroup} from '@angular/forms';
import {RecordsUIComponent} from '../../../shared/components/records-ui/records-ui.component';
import {RadioButtonClickEvent} from "primeng/radiobutton";

@Component({
    selector: 'app-get-team-records',
    templateUrl: './get-team-records.component.html',
    styleUrls: ['./get-team-records.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GetTeamRecordsComponent implements AfterViewInit {

  @ViewChild(RecordsUIComponent) recordsUi!: RecordsUIComponent;

  constructor(
    private formHelperService: FormHelperService) {
  }

  ngAfterViewInit() {
    this.recordsUi.initialLimit = 100
  }

  isNotMultiDay(form: FormGroup) {
    return this.formHelperService.isNotMultiDay(form.get('matchType')?.value)
  }

  isFirstClass(form: FormGroup) {
    return !this.isNotMultiDay(form)
  }

  isNotInternational(form: FormGroup) {
    let matchType = form.get('matchType')?.value;

    return this.formHelperService.isNotInternational(matchType)
  }

  isInternational(form: FormGroup) {
    return !this.isNotInternational(form)
  }

  selectionChanged($event: RadioButtonClickEvent) {
    switch ($event.value) {
      case 3:
        this.recordsUi.setLimit(0)
        this.recordsUi.setSort(4, 'DESC')
        break;
      case 4:
      case 8:
      case 9:
        this.recordsUi.setLimit(100)
        this.recordsUi.setSort(9, 'DESC')
        break;
      case 2:
      case 10:
        this.recordsUi.setLimit(0)
        this.recordsUi.setSort(17, 'ASC')
        break;
      case 11:
      case 12:
        this.recordsUi.setLimit(0)
        this.recordsUi.setSort(24, 'DESC')
        break;
      default:
        this.recordsUi.setLimit(100)
        this.recordsUi.setSort(4, 'DESC')
    }
  }

}
