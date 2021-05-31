import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';
import { ReportingTable } from '../models/reporting-table.model';

const LOG_PREFIX: string = "[Reporting Tables Data Service]";
const API_PREFIX: string = "api/v1/reporting_tables";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class ReportingTablesDataService {

  // The server's base url
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { reportingTables: ReportingTable[] } = { reportingTables: [] };

  // The observables that allows subscribers to keep tabs of the current status 
  // of reportingTables records in the data store
  private _reportingTablesSubject$ = new BehaviorSubject<ReportingTable[]>([]);
  readonly reportingTables$ = this._reportingTablesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("reportingTables-data-channel");

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

          this.getAllReportingTables()
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
    this.zone.run(() => this._reportingTablesSubject$.next(event.data.newValue));
  }


  /**
   * Creates and adds an instance of a new Reporting Table record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingTable The details of the Reporting Table record to be created - with the id and version details missing
   */
  public createReportingTable(reportingTable: ReportingTable): Observable<ReportingTable> {

    this.log.trace(`${LOG_PREFIX} Entering createReportingTable()`);
    this.log.debug(`${LOG_PREFIX} Reporting Table = ${JSON.stringify(reportingTable)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<ReportingTable>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(reportingTable), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingTable) => {

          // Reporting Table record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Reporting Table record = ${JSON.stringify(data)}`);

          // Add the newly created Reporting Table record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Reporting Table record to the Local Cache`);
          this._cache.reportingTables.push(data);

          // Create an up to date copy of the Reporting Tables records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Tables records`);
          const copy = Object.assign({}, this._cache).reportingTables;

          // Broadcast the up to date copy of the Reporting Tables records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the current listener`);
          this._reportingTablesSubject$.next(copy);

          // Broadcast the up to date copy of the Reporting Tables records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Reporting Table record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Table record Creation was successful" });

        }),

        catchError((error: any) => {

          // Reporting Table record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Table record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Table record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Table record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Reporting Table record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingTableId The Unique Identifier of the Reporting Table record
   */
  getReportingTable(reportingTableId: number): Observable<ReportingTable> {

    this.log.trace(`${LOG_PREFIX} Entering getReportingTable()`);
    this.log.debug(`${LOG_PREFIX} Reporting Table Id = ${reportingTableId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${reportingTableId} to retrieve the record`);

    return this.http.get<ReportingTable>(`${this._baseUrl}/${API_PREFIX}/ids/${reportingTableId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingTable) => {

          // Reporting Table record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Reporting Table record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Reporting Table record = ${JSON.stringify(data)}`);

          // Search for the Reporting Table record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Reporting Table record in the Local Cache and returning its index`);
          let index = this._cache.reportingTables.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Reporting Table record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Reporting Table record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Reporting Table record was found in the Local Cache`);

            // Update the local Reporting Table record
            this.log.trace(`${LOG_PREFIX} Updating the local Reporting Table record`);
            this._cache.reportingTables[index] = data;

          } else {

            // The Reporting Table record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Reporting Table record was not found in the Local Cache`);

            // Add the Reporting Table record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Reporting Table record to the Local Cache`);
            this._cache.reportingTables.push(data);
          }

          // Create an up to date copy of the Reporting Tables records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Tables records`);
          const copy = Object.assign({}, this._cache).reportingTables;

          // Broadcast the up to date copy of the Reporting Tables records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the current listener`);
          this._reportingTablesSubject$.next(copy);

          // Broadcast the up to date copy of the Reporting Tables records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Reporting Table record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Table record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Reporting Table record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Table record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Table record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Table record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Reporting Tables records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllReportingTables(filters?: any): Observable<ReportingTable[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllReportingTables()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<ReportingTable[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: ReportingTable[]) => {

          // Reporting Tables records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Reporting Tables records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Reporting Tables records = ${JSON.stringify(data)}`);

          // Update the Reporting Tables records in the Local Cache to the newly pulled Reporting Tables records
          this.log.trace(`${LOG_PREFIX} Updating the Reporting Tables records in the Local Cache to the newly pulled Reporting Tables records`);
          this._cache.reportingTables = data;

          // Create an up to date copy of the Reporting Tables records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Tables records`);
          const copy = Object.assign({}, this._cache).reportingTables;

          // Broadcast the up to date copy of the Reporting Tables records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the current listener`);
          this._reportingTablesSubject$.next(copy);

          // Broadcast the up to date copy of the Reporting Tables records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Reporting Tables records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Tables records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Tables records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Reporting Tables records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Tables records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Tables records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Tables records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Tables records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Reporting Table record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param reportingTable The details of the Reporting Table record to be updated
   */
  updateReportingTable(reportingTable: ReportingTable): Observable<ReportingTable> {

    this.log.trace(`${LOG_PREFIX} Entering updateReportingTable()`);
    this.log.debug(`${LOG_PREFIX} Reporting Table = ${JSON.stringify(reportingTable)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<ReportingTable>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(reportingTable), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: ReportingTable) => {

          // Reporting Table record Update was successful
          this.log.trace(`${LOG_PREFIX} Reporting Table record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Reporting Table record = ${JSON.stringify(data)}`);

          // Search for the locally stored Reporting Table record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Reporting Table record`);
          let index = this._cache.reportingTables.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Reporting Table record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Reporting Table record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Reporting Table record`);
            this._cache.reportingTables[index] = data;

            // Create an up to date copy of the Reporting Tables records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Tables records`);
            const copy = Object.assign({}, this._cache).reportingTables;

            // Broadcast the up to date copy of the Reporting Tables records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the current listener`);
            this._reportingTablesSubject$.next(copy);

            // Broadcast the up to date copy of the Reporting Tables records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Reporting Table record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Table record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Reporting Table record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Reporting Tables records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Reporting Table record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Table record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Table record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Table record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Reporting Table record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param reportingTableId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteReportingTable(reportingTableId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteReportingTable()`);
    this.log.debug(`${LOG_PREFIX} Reporting Table Id = ${reportingTableId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${reportingTableId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${reportingTableId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Reporting Table record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Reporting Table record Deletion was successful`);

            // Search for the deleted Reporting Table record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Reporting Table record in the Local Cache`);
            let index = this._cache.reportingTables.findIndex(d => d.id == reportingTableId);
            this.log.debug(`${LOG_PREFIX} Deleted Reporting Table record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Reporting Table record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Reporting Table record from the Local Cache`);
              this._cache.reportingTables.splice(index, 1);

              // Create an up to date copy of the Reporting Tables records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Reporting Tables records`);
              const copy = Object.assign({}, this._cache).reportingTables;

              // Broadcast the up to date copy of the Reporting Tables records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the current listener`);
              this._reportingTablesSubject$.next(copy);

              // Broadcast the up to date copy of the Reporting Tables records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Reporting Tables records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Reporting Table record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Reporting Table record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Reporting Table record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Reporting Tables records Local Cache Update was unsuccessful" });
            }
          } else {

            // Reporting Table record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Reporting Table record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Reporting Table record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Table record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Reporting Table record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Reporting Table record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Reporting Table record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Reporting Table record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Reporting Table record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._reportingTablesSubject$.value;
  }
}
