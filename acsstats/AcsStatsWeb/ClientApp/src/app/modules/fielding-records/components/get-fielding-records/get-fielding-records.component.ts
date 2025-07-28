import {ChangeDetectionStrategy, Component, ViewChild} from '@angular/core';
import {FormHelperService} from '../../../../services/form-helper.service';
import {FormGroup} from '@angular/forms';
import {RecordsUIComponent} from '../../../shared/components/records-ui/records-ui.component';
import {RadioButtonClickEvent} from "primeng/radiobutton";
import {SortOrder} from "../../../../models/sortorder.model";

@Component({
    selector: 'app-get-fielding-records',
    templateUrl: './get-fielding-records.component.html',
    styleUrls: ['./get-fielding-records.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GetFieldingRecordsComponent {

  @ViewChild(RecordsUIComponent) recordsUi!: RecordsUIComponent;

  constructor(
    private formHelperService: FormHelperService) {
  }

  isNotFirstClass(form: FormGroup) {
    let matchType = form.get('matchType')?.value;

    let value = this.formHelperService.isNotMultiDay(matchType)

    return value
  }

  isNotInternational(form: FormGroup) {
    let matchType = form.get('matchType')?.value;

    return this.formHelperService.isNotInternational(matchType)
  }

  selectionChanged($event: RadioButtonClickEvent) {
    this.recordsUi.setSort(SortOrder.dismissals, 'DESC')
    if ($event.value == 1) {
      this.recordsUi.setLimit(100)
    } else if ($event.value == 2) {
      this.recordsUi.setLimit(0)
      this.recordsUi.setSort(17, 'ASC')
    } else if ($event.value == 3) {
      this.recordsUi.setLimit(0)
    } else if ($event.value == 4) {
      this.recordsUi.setLimit(0)
    } else if ($event.value == 5) {
      this.recordsUi.setLimit(10)
    } else {
      this.recordsUi.setLimit(25)
    }
  }

}
