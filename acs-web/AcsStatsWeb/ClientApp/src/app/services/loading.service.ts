import {Injectable} from '@angular/core';
import {SetLoadingAction} from '../actions/loading.actions';
import {HttpRequest} from '@angular/common/http';
import {Store} from '@ngrx/store';
import {AppState} from '../models/app-state';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  loadingMap: Map<HttpRequest<any>, boolean> = new Map<HttpRequest<any>, boolean>();

  constructor(
    private store: Store<AppState>
  ) {
  }

  /**
   * This method is only called from the {@link HttpRequestInterceptor}
   * We can't just dispatch the action with the value of the loading argument, we
   * must ensure that there are no pending requests still loading in the loadingMap
   * @param loading {boolean}
   * @param request {string}
   */
  setLoading(loading: boolean, request: HttpRequest<any>) {
    if (loading) {
      this.loadingMap.set(request, loading);
      this.store.dispatch(SetLoadingAction({payload: true}));
    } else if (!loading && this.loadingMap.has(request)) {
      this.loadingMap.delete(request);
      if (this.loadingMap.size === 0) {
        this.store.dispatch(SetLoadingAction({payload: false}));
      }
    }
  }
}
