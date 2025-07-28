import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, map} from "rxjs/operators";
import {HttpUrlBuilderService} from "../../../services/http-url-builder.service";
import {FindRecords} from "../../../models/find-records.model";
import * as fs from "file-saver";


@Injectable({providedIn: 'root'})
export class FileService {

  constructor(private httpClient: HttpClient, private httpUrlBuilderService: HttpUrlBuilderService) {

  }

  downloadFile(rootUrl: string, params: FindRecords, fileName?: string) {


    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(params)

    let lastSegment = rootUrl.split('/').reverse()[0];

    let headers = new HttpHeaders();

    // @ts-ignore
    options["observe"] = 'response'
    // @ts-ignore
    options["responseType"] = 'arrayBuffer'

    if (lastSegment == "csv") {
      headers = headers.set('Accept', 'text/csv, application/json, text/plain, */*');
      // @ts-ignore
      options["headers"] = headers
    } else if (lastSegment == "xls") {
      headers = headers.set('Accept', 'application/vnd.ms-excel, application/json, text/plain, */*');
      // @ts-ignore
      options["headers"] = headers
    }

    this.httpClient.get(rootUrl, options).pipe(map((response, b) => {
        this.download(response)
      }),
      catchError((err) => {
        return err
      })).subscribe();
  }

  private download(response: any, fileName?: string) {
    const blob = new Blob([response.body], {type: response.headers.get('content-type')});

    fileName = fileName || (response.headers.get('content-disposition').split(';')[0] || "unknown") as string;
    const file = new File([blob], fileName, {type: response.headers.get('content-type')});

    console.debug("download", fileName)

    fs.saveAs(file);
  }
}
