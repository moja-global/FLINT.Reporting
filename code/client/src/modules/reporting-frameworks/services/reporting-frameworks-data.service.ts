import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { ReportingFramework } from '../models/reporting-framework.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { MessageService } from '@common/services';
import { catchError, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Reporting Frameworks Data Service]";
const API_PREFIX: string = "api/v1/reporting_frameworks";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class ReportingFrameworksDataService {

  private _baseUrl: string = environment.baseUrl;
  private _cache: { reportingFrameworks: ReportingFramework[] } = { reportingFrameworks: [] };
  private _subject$ = new BehaviorSubject<ReportingFramework[]>([]);

  readonly reportingFrameworks$ = this._subject$.asObservable();

  constructor(
    private http: HttpClient,
    private log: NGXLogger,
    private messageService: MessageService) {

  }

  /**
   * Creates and adds an instance of a new Reporting Framework record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingFramework The details of the Reporting Framework record to be created - with the id and version details missing
   */
  public createReportingFramework(reportingFramework: ReportingFramework): Observable<ReportingFramework> {

    this.log.trace(`${LOG_PREFIX} Entering createReportingFramework()`);
    this.log.debug(`${LOG_PREFIX} ReportingFramework = ${JSON.stringify(reportingFramework)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<ReportingFramework>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(reportingFramework), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingFramework) => {

          // Reporting Framework record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Reporting Framework record = ${JSON.stringify(data)}`);

          // Add the newly created Reporting Framework record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Reporting Framework record to the Local Cache`);
          this._cache.reportingFrameworks.push(data);

          // Push a copy of the newly updated Reporting Frameworks records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Reporting Frameworks records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).reportingFrameworks);

          // Send a message that states that the Reporting Framework record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Framework record Creation was successful" });

        }),

        catchError((error: any) => {

          // Reporting Framework record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Framework record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Framework record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Framework record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Reporting Framework record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingFrameworkId The Unique Identifier of the Reporting Framework record
   */
  getReportingFramework(reportingFrameworkId: number): Observable<ReportingFramework> {

    this.log.trace(`${LOG_PREFIX} Entering getReportingFramework()`);
    this.log.debug(`${LOG_PREFIX} ReportingFramework Id = ${reportingFrameworkId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${reportingFrameworkId} to retrieve the record`);

    return this.http.get<ReportingFramework>(`${this._baseUrl}/${API_PREFIX}/ids/${reportingFrameworkId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingFramework) => {

          // Reporting Framework record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Reporting Framework record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Reporting Framework record = ${JSON.stringify(data)}`);

          // Search for the Reporting Framework record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Reporting Framework record in the Local Cache and returning its index`);
          let index = this._cache.reportingFrameworks.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Reporting Framework record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Reporting Framework record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Reporting Framework record was found in the Local Cache`);

            // Update the local Reporting Framework record
            this.log.trace(`${LOG_PREFIX} Updating the local Reporting Framework record`);
            this._cache.reportingFrameworks[index] = data;

          } else {

            // The Reporting Framework record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Reporting Framework record was not found in the Local Cache`);

            // Add the Reporting Framework record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Reporting Framework record to the Local Cache`);
            this._cache.reportingFrameworks.push(data);
          }

          // Push a copy of the newly updated Reporting Frameworks records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Reporting Frameworks records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).reportingFrameworks);

          // Send a message that states that the Reporting Framework record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Framework record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Reporting Framework record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Framework record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Framework record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Framework record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Reporting Frameworks records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllReportingFrameworks(filters?: any): Observable<ReportingFramework[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllReportingFrameworks()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<ReportingFramework[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: ReportingFramework[]) => {

          // Reporting Frameworks records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Reporting Frameworks records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Reporting Frameworks records = ${JSON.stringify(data)}`);

          // Update the Reporting Frameworks records in the Local Cache to the newly pulled Reporting Frameworks records
          this.log.trace(`${LOG_PREFIX} Updating the Reporting Frameworks records in the Local Cache to the newly pulled Reporting Frameworks records`);
          this._cache.reportingFrameworks = data;

          // Push a copy of the newly updated Reporting Frameworks records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Reporting Frameworks records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).reportingFrameworks);

          // Send a message that states that the Reporting Frameworks records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Frameworks records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Frameworks records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Reporting Frameworks records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Frameworks records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Frameworks records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Frameworks records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Frameworks records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Reporting Framework record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingFramework The details of the Reporting Framework record to be updated
   */
  updateReportingFramework(reportingFramework: ReportingFramework): Observable<ReportingFramework> {

    this.log.trace(`${LOG_PREFIX} Entering updateReportingFramework()`);
    this.log.debug(`${LOG_PREFIX} ReportingFramework = ${JSON.stringify(reportingFramework)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<ReportingFramework>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(reportingFramework), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingFramework) => {

          // Reporting Framework record Update was successful
          this.log.trace(`${LOG_PREFIX} Reporting Framework record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Reporting Framework record = ${JSON.stringify(data)}`);

          // Search for the locally stored Reporting Framework record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Reporting Framework record`);
          let index = this._cache.reportingFrameworks.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Reporting Framework record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Reporting Framework record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Reporting Framework record`);
            this._cache.reportingFrameworks[index] = data;

            // Push a copy of the newly updated Reporting Frameworks records to all Subscribers
            this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Reporting Frameworks records to all Subscribers`);
            this._subject$.next(Object.assign({}, this._cache).reportingFrameworks);

            // Send a message that states that the Reporting Framework record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Framework record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Reporting Framework record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Reporting Frameworks records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Reporting Framework record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Framework record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Framework record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Framework record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Reporting Framework record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param reportingFrameworkId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteReportingFramework(reportingFrameworkId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteReportingFramework()`);
    this.log.debug(`${LOG_PREFIX} ReportingFramework Id = ${reportingFrameworkId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${reportingFrameworkId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${reportingFrameworkId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Reporting Framework record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Reporting Framework record Deletion was successful`);

            // Search for the deleted Reporting Framework record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Reporting Framework record in the Local Cache`);
            let index = this._cache.reportingFrameworks.findIndex(d => d.id == reportingFrameworkId);
            this.log.debug(`${LOG_PREFIX} Deleted Reporting Framework record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Reporting Framework record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Reporting Framework record from the Local Cache`);
              this._cache.reportingFrameworks.splice(index, 1);

              // Push a copy of the newly updated Reporting Frameworks records to all Subscribers
              this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Reporting Frameworks records to all Subscribers`);
              this._subject$.next(Object.assign({}, this._cache).reportingFrameworks);

              // Send a message that states that the Reporting Framework record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Framework record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Reporting Framework record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Reporting Frameworks records Local Cache Update was unsuccessful" });
            }
          } else {

            // Reporting Framework record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Reporting Framework record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Reporting Framework record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Framework record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Reporting Framework record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Framework record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Framework record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Framework record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Framework record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._subject$.value;
  }
}
