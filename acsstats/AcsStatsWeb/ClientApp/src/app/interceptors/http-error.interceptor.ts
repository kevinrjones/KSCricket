import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, Observable} from 'rxjs';
import {Router} from '@angular/router';
import {LumberjackService} from "@ngworker/lumberjack";
import {MessageService} from 'primeng/api';

@Injectable()


export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private router: Router,
              private messageService: MessageService,
              private readonly lumberjack: LumberjackService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(
        catchError((err: HttpErrorResponse) => {

          if (err.error instanceof Error) {
            // A client-side or network error occurred. Handle it accordingly.
            this.lumberjack.logError(`Code: ${err.headers.get("X-Request-Id")} - Request to ${err.url} failed with status ${err.status} ${err.message}`);
          } else {
            // The backend returned an unsuccessful response code.
            // The response body may contain clues as to what went wrong,
            if (err.status == 401) {
              // todo - save current url in storage and then redirect to it after login?
              this.router.navigateByUrl("/")
            }

            var message = ""
            if (err.status >= 500) {
              message = "There was an error processing your request. Please report this error to the site administrator."
            } else {
              if (err.error.errorMessage === undefined) {
                console.log(`I expected the errorMessage to exist on the 'err.error' object but the object looks like this: ${err.error}`)
              }
              message = err.error.errorMessage
            }

            this.messageService.add({ severity: 'error', summary: 'Error', detail: message, life: 3000 });
            this.lumberjack.logError(`Code: ${err.headers.get("X-Request-Id")} - Request to ${err.url} failed with status ${err.status} ${err.message}`);
          }

          if (request.url.includes("bff/user"))
            return next.handle(request)

          return new Observable<HttpEvent<any>>();
        })
      )
  }
}
