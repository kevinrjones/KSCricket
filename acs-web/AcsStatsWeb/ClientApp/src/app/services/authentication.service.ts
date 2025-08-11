import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, filter, map, mergeWith, Observable, of, shareReplay} from 'rxjs';

const ANONYMOUS: Session = null;
const CACHE_SIZE = 1;

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private session$: Observable<Session> | null = null

  constructor(private http: HttpClient) {
  }

  public getSession(ignoreCache: boolean = false): Observable<Session> {
    if (!this.session$ || ignoreCache) {
      this.session$ = this.http.get<Session>('bff/user').pipe(
        catchError(err => {
          return of(ANONYMOUS);
        }),
        shareReplay(CACHE_SIZE)
      );
    }
    return this.session$;
  }

  public getIsAuthenticated(ignoreCache: boolean = false) {
    return this.getSession(ignoreCache).pipe(
      map(UserIsAuthenticated)
    );
  }

  public getIsAnonymous(ignoreCache: boolean = false) {
    return this.getSession(ignoreCache).pipe(
      map(UserIsAnonymous)
    );
  }

  public getUsername(ignoreCache: boolean = false) {

    return this.getSession(ignoreCache).pipe(
      filter(UserIsAuthenticated),
      map(s => s.find(c => c.type === 'email')?.value),
      mergeWith(this.getSession(ignoreCache).pipe(
        filter(UserIsAnonymous),
        map(s => "Anonymous")
      ))
    )
  }

  public getLogoutUrl(ignoreCache: boolean = false) {
    return this.getSession(ignoreCache).pipe(
      filter(UserIsAuthenticated),
      map(s => s.find(c => c.type === 'bff:logout_url')?.value)
    );
  }
}

export interface Claim {
  type: string;
  value: string;
}

export type Session = Claim[] | null;

function UserIsAuthenticated(s: Session): s is Claim[] {
  return s !== null;
}

function UserIsAnonymous(s: Session): s is null {
  return s === null;
}
