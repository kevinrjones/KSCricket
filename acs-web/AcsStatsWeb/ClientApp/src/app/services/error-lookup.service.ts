import {Injectable} from '@angular/core';

@Injectable({providedIn: 'root'})
export class ErrorLookupService {

  errors = new Map<number, string>()

  constructor() {
    this.errors.set(1, 'Unable to connect to the server')
    this.errors.set(2, 'Server Error')
    this.errors.set(3, 'Http Error')
  }


  getErrorForCode(id: number): string {
    // @ts-ignore
    return this.errors.has(id) ? this.errors.get(id) : 'Unknown error'
  }

}
