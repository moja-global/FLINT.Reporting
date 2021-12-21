import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Accountability } from '../models/accountability.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Accountabilities Data Service]";
const API_PREFIX: string = "api/v1/accountabilities";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class AccountabilitiesDataService {

  // The local data cache
  private _cache: { accountabilities: Accountability[] } = { accountabilities: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _accountabilitiesSubject$ = new BehaviorSubject<Accountability[]>([]);
  readonly accountabilities$ = this._accountabilitiesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("accountabilities-data-channel");

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

          // Initialize data
          this.log.trace(`${LOG_PREFIX} Initializing data`);

          this.getAllAccountabilities()
            .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
            .subscribe((response => {

              // Data initialization complete
              this.log.trace(`${LOG_PREFIX} Data initialization complete`);

            }));

          // Unsubscribe from connectivity status notifications
          this.log.trace(`${LOG_PREFIX} Unsubscribing from connectivity status notifications`);
          this._done$.next(true);
          this._done$.complete();

        }

      });

    //Note: "bc.onmessage" isn't invoked on sender ui
    this.bc.onmessage = this.zone.run(() => this.handleEvent);
  }

  /**
   * Creates and adds instances of  Accountabilities records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param accountabilities The details of the Accountabilities record to be created - with the ids and versions details missing
   */
  public createAccountabilities(accountabilities: Accountability[]): Observable<Accountability[]> {

    this.log.trace(`${LOG_PREFIX} Entering createAccountabilities()`);
    this.log.debug(`${LOG_PREFIX} Accountabilities = ${JSON.stringify(accountabilities)}`);

    // Make a HTTP POST Request to create the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.accountabilitiesBaseUrl}/${API_PREFIX}/all to create the records`);

    return this.http.post<Accountability[]>(`${environment.accountabilitiesBaseUrl}/${API_PREFIX}/all`, JSON.stringify(accountabilities), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Accountability[]) => {

          // Accountabilities records Creation was successful
          this.log.trace(`${LOG_PREFIX} Records Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Accountabilities records = ${JSON.stringify(data)}`);

          // Add the newly created Accountabilities records to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Accountabilities records to the Local Cache`);
          this._cache.accountabilities = [...this._cache.accountabilities, ...data];

          // Create an up to date copy of the Accountabilities Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Records`);
          const copy = Object.assign({}, this._cache).accountabilities;

          // Broadcast the up to date copy of the Accountabilities Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Records to the current listener`);
          this._accountabilitiesSubject$.next(copy);

          // Broadcast the up to date copy of the Accountabilities Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Accountability record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountability record Creation was successful" });

        }),

        catchError((error: any) => {

          // Accountabilities records Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountabilities records Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountabilities records Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountabilities records Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountabilities records Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._accountabilitiesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Accountabilities Records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllAccountabilities(filters?: any): Observable<Accountability[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllAccountabilities()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.accountabilitiesBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<Accountability[]>(`${environment.accountabilitiesBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: Accountability[]) => {

          // Accountabilities Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Accountabilities Records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Accountabilities Records = ${JSON.stringify(data)}`);

          // Update the Accountabilities Records in the Local Cache to the newly pulled Accountabilities Records
          this.log.trace(`${LOG_PREFIX} Updating the Accountabilities Records in the Local Cache to the newly pulled Accountabilities Records`);
          this._cache.accountabilities = data;

          // Create an up to date copy of the Accountabilities Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Records`);
          const copy = Object.assign({}, this._cache).accountabilities;

          // Broadcast the up to date copy of the Accountabilities Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Records to the current listener`);
          this._accountabilitiesSubject$.next(copy);

          // Broadcast the up to date copy of the Accountabilities Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Accountabilities Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountabilities Records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountabilities Records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Accountabilities Records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountabilities Records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountabilities Records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountabilities Records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountabilities Records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Accountability record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param accountability The details of the Accountability record to be updated
   */
  updateAccountability(accountability: Accountability): Observable<Accountability> {

    this.log.trace(`${LOG_PREFIX} Entering updateAccountability()`);
    this.log.debug(`${LOG_PREFIX} Accountability = ${JSON.stringify(accountability)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.accountabilitiesBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<Accountability>(`${environment.accountabilitiesBaseUrl}/${API_PREFIX}`, JSON.stringify(accountability), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Accountability) => {

          // Accountability record Update was successful
          this.log.trace(`${LOG_PREFIX} Accountability record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Accountability record = ${JSON.stringify(data)}`);

          // Search for the locally stored Accountability record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Accountability record`);
          let index = this._cache.accountabilities.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Accountability record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Accountability record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Accountability record`);
            this._cache.accountabilities[index] = data;

            // Create an up to date copy of the Accountabilities Records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Records`);
            const copy = Object.assign({}, this._cache).accountabilities;

            // Broadcast the up to date copy of the Accountabilities Records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Records to the current listener`);
            this._accountabilitiesSubject$.next(copy);

            // Broadcast the up to date copy of the Accountabilities Records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Accountability record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountability record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Accountability record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Accountabilities Records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Accountability record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountability record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountability record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountability record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Accountability record and its corresponding counterparts in the local cache and then broadcasts the changes to all subscribers
   *
   * @param accountabilityId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteAccountability(accountabilityId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteAccountability()`);
    this.log.debug(`${LOG_PREFIX} Accountability Id = ${accountabilityId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.accountabilitiesBaseUrl}/${API_PREFIX}/ids/${accountabilityId} to delete the record`);

    return this.http.delete<number>(`${environment.accountabilitiesBaseUrl}/${API_PREFIX}/ids/${accountabilityId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Send a message that states that the Accountability record Deletion was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability record Deletion was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountability record Deletion was successful" });

          // Since the accountability and all its descendants have been deleted, refresh the service to get the up to date accountabilities
          this.log.trace(`${LOG_PREFIX} Since the accountability and all its descendants have been deleted, refreshing the service to get the up to date accountabilities`);

          this.getAllAccountabilities();

        }),

        catchError((error: any) => {

          // Accountability record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountability record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountability record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountability record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._accountabilitiesSubject$.value;
  }
}
