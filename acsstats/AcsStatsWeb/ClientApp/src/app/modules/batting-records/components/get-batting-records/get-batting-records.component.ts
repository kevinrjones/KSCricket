import {ChangeDetectionStrategy, Component, ViewChild} from '@angular/core';
import {FormHelperService} from '../../../../services/form-helper.service';
import {FormGroup} from '@angular/forms';
import {RecordsUIComponent} from '../../../shared/components/records-ui/records-ui.component';
import {RadioButtonClickEvent} from "primeng/radiobutton";

@Component({
    selector: 'app-get-batting-records',
    templateUrl: './get-batting-records.component.html',
    styleUrls: ['./get-batting-records.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GetBattingRecordsComponent {

  @ViewChild(RecordsUIComponent) recordsUi!: RecordsUIComponent;

  constructor(
    private formHelperService: FormHelperService) {
  }

  isNotMultiDay(form: FormGroup) {
    let matchType = form.get('matchType')?.value;

    let value = this.formHelperService.isNotMultiDay(matchType)

    return value
  }

  isNotInternational(form: FormGroup) {
    let matchType = form.get('matchType')?.value;

    return this.formHelperService.isNotInternational(matchType)
  }

  selectionChanged($event: RadioButtonClickEvent) {
    if ($event.value == 2) {
      this.recordsUi.setLimit(0)
      this.recordsUi.setSort(17, 'ASC')
    } else {
      this.recordsUi.setLimit(100)
      this.recordsUi.setSort(4, 'DESC')
    }
  }
}
