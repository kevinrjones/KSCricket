// app/services/config.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError} from "rxjs/operators";
import {lastValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private static readonly CONFIG_API_URL = '/api/config';
  private config: AppConfig | undefined = undefined;

  constructor(private http: HttpClient) {}

  async loadConfig(): Promise<AppConfig> {
    try {
      this.config = await lastValueFrom(this.http.get<AppConfig>(ConfigService.CONFIG_API_URL)
        .pipe(
          catchError((error) => {
            console.error('Failed to load configuration:', error);
            throw error;
          })
        ))

      return this.config ?? {authorityUrl: ""};
    } catch (error) {
      throw new Error('Configuration loading failed');
    }
  }

  getConfig(): AppConfig | undefined {
    return this.config;
  }
}
