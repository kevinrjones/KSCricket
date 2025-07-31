import {Injectable} from '@angular/core';

@Injectable({providedIn: 'root'})
export class FormHelperService {

  private readonly defaultMatchType = 't'

  getDefaultMatchType() {
    return this.defaultMatchType;
  }

  isNotMultiDay(matchType: string) {
    return !this.isMultiDay(matchType)
  }

  isMultiDay(matchType: string) {
    return (matchType === 't' || matchType === 'f' || matchType === 'wt' || matchType === 'wf' || matchType === 'sec');
  }

  isInternational(matchType: string) {
    return matchType === 't' || matchType == 'itt' || matchType == 'o' || matchType == 'witt' || matchType == 'wt' || matchType == 'wo'
  }

  isNotInternational(matchType: string) {
    return !this.isInternational(matchType);
  }

}
