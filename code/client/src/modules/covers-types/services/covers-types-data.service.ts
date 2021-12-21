import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { CoverType } from '../models/cover-type.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Covers Types Data Service]";
const API_PREFIX: string = "api/v1/cover_types";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class CoversTypesDataService {

  // The local data cache
  private _cache: { coversTypes: CoverType[] } = { coversTypes: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _coversTypesSubject$ = new BehaviorSubject<CoverType[]>([]);
  readonly coversTypes$ = this._coversTypesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("covers-types-data-channel");

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

          this.getAllCoversTypes()
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
   * Creates and adds an instance of a new Cover Type record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param coverType The details of the Cover Type record to be created - with the id and version details missing
   */
  public createCoverType(coverType: CoverType): Observable<CoverType> {

    this.log.trace(`${LOG_PREFIX} Entering createCoverType()`);
    this.log.debug(`${LOG_PREFIX} Cover Type = ${JSON.stringify(coverType)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.coversTypesBaseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<CoverType>(`${environment.coversTypesBaseUrl}/${API_PREFIX}`, JSON.stringify(coverType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: CoverType) => {

          // Cover Type record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Cover Type record = ${JSON.stringify(data)}`);

          // Add the newly created Cover Type record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Cover Type record to the Local Cache`);
          this._cache.coversTypes.push(data);

          // Create an up to date copy of the Covers Types Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Covers Types Records`);
          const copy = Object.assign({}, this._cache).coversTypes;

          // Broadcast the up to date copy of the Covers Types Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Covers Types Records to the current listener`);
          this._coversTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Covers Types Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Covers Types Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Cover Type record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Type record Creation was successful" });

        }),

        catchError((error: any) => {

          // Cover Type record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Type record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Type record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._coversTypesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Covers Types Records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllCoversTypes(filters?: any): Observable<CoverType[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllCoversTypes()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.coversTypesBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<CoverType[]>(`${environment.coversTypesBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: CoverType[]) => {

          // Covers Types Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Covers Types Records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Covers Types Records = ${JSON.stringify(data)}`);

          // Update the Covers Types Records in the Local Cache to the newly pulled Covers Types Records
          this.log.trace(`${LOG_PREFIX} Updating the Covers Types Records in the Local Cache to the newly pulled Covers Types Records`);
          this._cache.coversTypes = data;

          // Create an up to date copy of the Covers Types Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Covers Types Records`);
          const copy = Object.assign({}, this._cache).coversTypes;

          // Broadcast the up to date copy of the Covers Types Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Covers Types Records to the current listener`);
          this._coversTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Covers Types Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Covers Types Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Covers Types Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Covers Types Records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Covers Types Records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Covers Types Records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Covers Types Records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Covers Types Records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Covers Types Records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Covers Types Records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Cover Type record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param coverType The details of the Cover Type record to be updated
   */
  updateCoverType(coverType: CoverType): Observable<CoverType> {

    this.log.trace(`${LOG_PREFIX} Entering updateCoverType()`);
    this.log.debug(`${LOG_PREFIX} Cover Type = ${JSON.stringify(coverType)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.coversTypesBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<CoverType>(`${environment.coversTypesBaseUrl}/${API_PREFIX}`, JSON.stringify(coverType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: CoverType) => {

          // Cover Type record Update was successful
          this.log.trace(`${LOG_PREFIX} Cover Type record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Cover Type record = ${JSON.stringify(data)}`);

          // Search for the locally stored Cover Type record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Cover Type record`);
          let index = this._cache.coversTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Cover Type record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Cover Type record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Cover Type record`);
            this._cache.coversTypes[index] = data;

            // Create an up to date copy of the Covers Types Records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Covers Types Records`);
            const copy = Object.assign({}, this._cache).coversTypes;

            // Broadcast the up to date copy of the Covers Types Records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Covers Types Records to the current listener`);
            this._coversTypesSubject$.next(copy);

            // Broadcast the up to date copy of the Covers Types Records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Covers Types Records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Cover Type record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Type record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Cover Type record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Covers Types Records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Cover Type record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Type record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Type record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Cover Type record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param coverTypeId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteCoverType(coverTypeId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteCoverType()`);
    this.log.debug(`${LOG_PREFIX} Cover Type Id = ${coverTypeId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.coversTypesBaseUrl}/${API_PREFIX}/ids/${coverTypeId} to delete the record`);

    return this.http.delete<number>(`${environment.coversTypesBaseUrl}/${API_PREFIX}/ids/${coverTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Cover Type record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Cover Type record Deletion was successful`);

            // Search for the deleted Cover Type record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Cover Type record in the Local Cache`);
            let index = this._cache.coversTypes.findIndex(d => d.id == coverTypeId);
            this.log.debug(`${LOG_PREFIX} Deleted Cover Type record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Cover Type record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Cover Type record from the Local Cache`);
              this._cache.coversTypes.splice(index, 1);

              // Create an up to date copy of the Covers Types Records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Covers Types Records`);
              const copy = Object.assign({}, this._cache).coversTypes;

              // Broadcast the up to date copy of the Covers Types Records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Covers Types Records to the current listener`);
              this._coversTypesSubject$.next(copy);

              // Broadcast the up to date copy of the Covers Types Records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Covers Types Records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Cover Type record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Type record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Cover Type record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Covers Types Records Local Cache Update was unsuccessful" });
            }
          } else {

            // Cover Type record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Cover Type record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Cover Type record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Cover Type record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Type record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Type record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._coversTypesSubject$.value;
  }
}
