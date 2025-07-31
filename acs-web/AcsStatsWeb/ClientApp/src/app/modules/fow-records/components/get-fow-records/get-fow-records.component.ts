import {ChangeDetectionStrategy, Component, ViewChild} from '@angular/core';
import {FormHelperService} from '../../../../services/form-helper.service';
import {FormGroup} from '@angular/forms';
import {RecordsUIComponent} from '../../../shared/components/records-ui/records-ui.component';
import {RadioButtonClickEvent} from "primeng/radiobutton";
import {ToggleSwitchChangeEvent} from "primeng/toggleswitch";

@Component({
    selector: 'app-get-fow-records',
    templateUrl: './get-fow-records.component.html',
    styleUrls: ['./get-fow-records.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GetFowRecordsComponent {

  @ViewChild(RecordsUIComponent) recordsUi!: RecordsUIComponent;

  isForWicket = false;

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


  formatSelectionChangedEx($event: RadioButtonClickEvent) {
    this.recordsUi.setLimit(100)
    this.recordsUi.setSort(4, 'DESC')
  }

  getPartnershipSelectionClassForWicket(form: FormGroup) {
    let isForWicket: boolean = form.get("isForWicket")?.value
    return !isForWicket ? 'hideSelect' : '';
  }

  getPartnershipSelectionClassForTeam(form: FormGroup) {
    let isForWicket: boolean = form.get("isForWicket")?.value
    return isForWicket ? 'hideSelect' : '';
  }


  byWicketSelectionChanged($event: ToggleSwitchChangeEvent) {
    if ($event.checked) {
      this.recordsUi.recordsForm.patchValue({format: 101})
      this.recordsUi.recordsForm.patchValue({isForWicket: true})
    } else {
      this.recordsUi.recordsForm.patchValue({format: 1})
    }
  }

}
