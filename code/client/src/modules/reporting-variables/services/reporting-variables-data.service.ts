import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';
import { ReportingVariable } from '../models/reporting-variable.model';

const LOG_PREFIX: string = "[Reporting Variables Data Service]";
const API_PREFIX: string = "api/v1/reporting_variables";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class ReportingVariablesDataService {

  // The server's base url
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { reportingVariables: ReportingVariable[] } = { reportingVariables: [] };

  // The observables that allows subscribers to keep tabs of the current status 
  // of reportingVariables records in the data store
  private _reportingVariablesSubject$ = new BehaviorSubject<ReportingVariable[]>([]);
  readonly reportingVariables$ = this._reportingVariablesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("reportingVariables-data-channel");

  constructor(
    private http: HttpClient,
    private connectivityStatusService: ConnectivityStatusService,
    private messageService: MessageService,
    private zone: NgZone,
    private log: NGXLogger,) {

    // Subscribe to connectivity status notifications
    this.log.trace(`${LOG_PREFIX} Subscribing to connectivity status notifications`);

    this.connectivityStatusService.online$
      .pipe(takeUntil(this._done$))
      .subscribe(online => {

        // Check if the user is online
        this.log.trace(`${LOG_PREFIX} Checking if the user is online`);
        this.log.debug(`${LOG_PREFIX} User is online = ${online}`);

        if (online) {

          // Initialize the data store
          this.log.trace(`${LOG_PREFIX} Initializing the data store`);

          this.getAllReportingVariables()
            .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
            .subscribe((response => {

              // Data store initialization complete
              this.log.trace(`${LOG_PREFIX} Data store initialization complete`);

            }));

          // Unsubscribe from connectivity status notifications
          this.log.trace(`${LOG_PREFIX} Unsubscribing from connectivity status notifications`);
          this._done$.next();
          this._done$.complete();

        }

      });

    //Note: "bc.onmessage" isn't invoked on sender ui
    this.bc.onmessage = this.zone.run(() => this.handleEvent);
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._reportingVariablesSubject$.next(event.data.newValue));
  }


  /**
   * Creates and adds an instance of a new Reporting Variable record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingVariable The details of the Reporting Variable record to be created - with the id and version details missing
   */
  public createReportingVariable(reportingVariable: ReportingVariable): Observable<ReportingVariable> {

    this.log.trace(`${LOG_PREFIX} Entering createReportingVariable()`);
    this.log.debug(`${LOG_PREFIX} Reporting Variable = ${JSON.stringify(reportingVariable)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<ReportingVariable>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(reportingVariable), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingVariable) => {

          // Reporting Variable record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Reporting Variable record = ${JSON.stringify(data)}`);

          // Add the newly created Reporting Variable record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Reporting Variable record to the Local Cache`);
          this._cache.reportingVariables.push(data);

          // Create an up to date copy of the Reporting Variables records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Variables records`);
          const copy = Object.assign({}, this._cache).reportingVariables;

          // Broadcast the up to date copy of the Reporting Variables records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the current listener`);
          this._reportingVariablesSubject$.next(copy);

          // Broadcast the up to date copy of the Reporting Variables records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Reporting Variable record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Variable record Creation was successful" });

        }),

        catchError((error: any) => {

          // Reporting Variable record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Variable record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Variable record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Variable record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Reporting Variable record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingVariableId The Unique Identifier of the Reporting Variable record
   */
  getReportingVariable(reportingVariableId: number): Observable<ReportingVariable> {

    this.log.trace(`${LOG_PREFIX} Entering getReportingVariable()`);
    this.log.debug(`${LOG_PREFIX} Reporting Variable Id = ${reportingVariableId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${reportingVariableId} to retrieve the record`);

    return this.http.get<ReportingVariable>(`${this._baseUrl}/${API_PREFIX}/ids/${reportingVariableId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingVariable) => {

          // Reporting Variable record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Reporting Variable record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Reporting Variable record = ${JSON.stringify(data)}`);

          // Search for the Reporting Variable record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Reporting Variable record in the Local Cache and returning its index`);
          let index = this._cache.reportingVariables.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Reporting Variable record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Reporting Variable record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Reporting Variable record was found in the Local Cache`);

            // Update the local Reporting Variable record
            this.log.trace(`${LOG_PREFIX} Updating the local Reporting Variable record`);
            this._cache.reportingVariables[index] = data;

          } else {

            // The Reporting Variable record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Reporting Variable record was not found in the Local Cache`);

            // Add the Reporting Variable record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Reporting Variable record to the Local Cache`);
            this._cache.reportingVariables.push(data);
          }

          // Create an up to date copy of the Reporting Variables records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Variables records`);
          const copy = Object.assign({}, this._cache).reportingVariables;

          // Broadcast the up to date copy of the Reporting Variables records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the current listener`);
          this._reportingVariablesSubject$.next(copy);

          // Broadcast the up to date copy of the Reporting Variables records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Reporting Variable record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Variable record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Reporting Variable record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Variable record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Variable record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Variable record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Reporting Variables records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllReportingVariables(filters?: any): Observable<ReportingVariable[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllReportingVariables()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<ReportingVariable[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: ReportingVariable[]) => {

          // Reporting Variables records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Reporting Variables records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Reporting Variables records = ${JSON.stringify(data)}`);

          // Update the Reporting Variables records in the Local Cache to the newly pulled Reporting Variables records
          this.log.trace(`${LOG_PREFIX} Updating the Reporting Variables records in the Local Cache to the newly pulled Reporting Variables records`);
          this._cache.reportingVariables = data;

          // Create an up to date copy of the Reporting Variables records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Variables records`);
          const copy = Object.assign({}, this._cache).reportingVariables;

          // Broadcast the up to date copy of the Reporting Variables records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the current listener`);
          this._reportingVariablesSubject$.next(copy);

          // Broadcast the up to date copy of the Reporting Variables records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Reporting Variables records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variables records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Variables records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Reporting Variables records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Variables records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Variables records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variables records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Variables records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Reporting Variable record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingVariable The details of the Reporting Variable record to be updated
   */
  updateReportingVariable(reportingVariable: ReportingVariable): Observable<ReportingVariable> {

    this.log.trace(`${LOG_PREFIX} Entering updateReportingVariable()`);
    this.log.debug(`${LOG_PREFIX} Reporting Variable = ${JSON.stringify(reportingVariable)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<ReportingVariable>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(reportingVariable), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingVariable) => {

          // Reporting Variable record Update was successful
          this.log.trace(`${LOG_PREFIX} Reporting Variable record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Reporting Variable record = ${JSON.stringify(data)}`);

          // Search for the locally stored Reporting Variable record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Reporting Variable record`);
          let index = this._cache.reportingVariables.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Reporting Variable record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Reporting Variable record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Reporting Variable record`);
            this._cache.reportingVariables[index] = data;

            // Create an up to date copy of the Reporting Variables records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Variables records`);
            const copy = Object.assign({}, this._cache).reportingVariables;

            // Broadcast the up to date copy of the Reporting Variables records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the current listener`);
            this._reportingVariablesSubject$.next(copy);

            // Broadcast the up to date copy of the Reporting Variables records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Reporting Variable record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Variable record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Reporting Variable record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Reporting Variables records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Reporting Variable record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Variable record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Variable record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Variable record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Reporting Variable record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param reportingVariableId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteReportingVariable(reportingVariableId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteReportingVariable()`);
    this.log.debug(`${LOG_PREFIX} Reporting Variable Id = ${reportingVariableId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${reportingVariableId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${reportingVariableId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Reporting Variable record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Reporting Variable record Deletion was successful`);

            // Search for the deleted Reporting Variable record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Reporting Variable record in the Local Cache`);
            let index = this._cache.reportingVariables.findIndex(d => d.id == reportingVariableId);
            this.log.debug(`${LOG_PREFIX} Deleted Reporting Variable record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Reporting Variable record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Reporting Variable record from the Local Cache`);
              this._cache.reportingVariables.splice(index, 1);

              // Create an up to date copy of the Reporting Variables records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Variables records`);
              const copy = Object.assign({}, this._cache).reportingVariables;

              // Broadcast the up to date copy of the Reporting Variables records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the current listener`);
              this._reportingVariablesSubject$.next(copy);

              // Broadcast the up to date copy of the Reporting Variables records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Variables records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Reporting Variable record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Variable record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Reporting Variable record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Reporting Variables records Local Cache Update was unsuccessful" });
            }
          } else {

            // Reporting Variable record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Reporting Variable record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Reporting Variable record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Variable record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Reporting Variable record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Variable record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Variable record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Variable record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Variable record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._reportingVariablesSubject$.value;
  }
}
