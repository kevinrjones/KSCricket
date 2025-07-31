import {ErrorDetails} from '../models/error.model';
import {HttpErrorResponse} from '@angular/common/http';


export function createError(id: number, message: string | null = null): ErrorDetails {
  return {id: id, message: message}
}

export function handleError(err: any, optionalMessage: string): ErrorDetails {
  if (err instanceof HttpErrorResponse) {
    return createError(3, 'Invalid HTTP Request');
  }
  return createError(1, optionalMessage);
}
