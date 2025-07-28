import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';
import {LoadingService} from '../services/loading.service';


/**
 * Intercept all http requests
 * @class {HttpRequestInterceptor}
 */
@Injectable()
export class HttpRequestInterceptor implements HttpRequestInterceptor {

  constructor(
    private loading: LoadingService
  ) {
  }

  /**
   * When an http request starts, set loading to true. When the request is finished, set loading to false.
   * If an error is thrown be sure loading is set to false.
   * @param {HttpRequest} request
   * @param {HttpHandler} next
   * @returns {Observable<HttpEvent<any>>}
   */
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    var timer = setTimeout(() => this.loading.setLoading(true, request), 100);

    return next.handle(request)
      .pipe(
        finalize(() => {
          clearTimeout(timer)
          this.loading.setLoading(false, request);
        })
      );
  }
}
