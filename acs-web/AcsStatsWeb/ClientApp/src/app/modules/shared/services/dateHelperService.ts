import {Injectable} from "@angular/core";
import {DateTime} from "luxon";

@Injectable({providedIn: 'root'})
export class DateHelperService {
  asDate(maybeDate: string, replacementDateText: string = '', localeOptions: Intl.DateTimeFormatOptions = DateTime.DATE_MED): string {
    if (maybeDate.length == 0) return replacementDateText;
    return DateTime.fromISO(maybeDate).toLocaleString(localeOptions)
  }
}
