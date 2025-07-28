import {ChangeDetectionStrategy, Component, ViewChild} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {FormHelperService} from '../../../../services/form-helper.service';
import {RecordsUIComponent} from '../../../shared/components/records-ui/records-ui.component';
import {RadioButtonClickEvent} from "primeng/radiobutton";

@Component({
    selector: 'app-get-bowling-records',
    templateUrl: './get-bowling-records.component.html',
    styleUrls: ['./get-bowling-records.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GetBowlingRecordsComponent {

  @ViewChild(RecordsUIComponent) recordsUi!: RecordsUIComponent;

  constructor(
    private formHelperService: FormHelperService) {
  }

  isNotMultiDay(form: FormGroup) {
    return this.formHelperService.isNotMultiDay(form.get('matchType')?.value)
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
      this.recordsUi.setLimit(10)
      this.recordsUi.setSort(5, 'DESC')
    }
  }

}
