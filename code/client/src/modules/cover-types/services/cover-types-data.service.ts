import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { CoverType } from '../models/cover-type.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Cover Types Data Service]";
const API_PREFIX: string = "api/v1/cover_types";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class CoverTypesDataService {

  // The base url of the server
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { coverTypes: CoverType[] } = { coverTypes: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of cover types records in the data store
  private _coverTypesSubject$ = new BehaviorSubject<CoverType[]>([]);
  readonly coverTypes$ = this._coverTypesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("cover-types-data-channel");

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

          this.getAllCoverTypes()
            .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
            .subscribe((response => {

              // Data initialization complete
              this.log.trace(`${LOG_PREFIX} Data initialization complete`);

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
    this.zone.run(() => this._coverTypesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds a single Cover Type record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param coverTypeId The Unique Identifier of the Cover Type record
   */
  getCoverType(coverTypeId: number): Observable<CoverType> {

    this.log.trace(`${LOG_PREFIX} Entering getCoverType()`);
    this.log.debug(`${LOG_PREFIX} CoverType Id = ${coverTypeId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${coverTypeId} to retrieve the record`);

    return this.http.get<CoverType>(`${this._baseUrl}/${API_PREFIX}/ids/${coverTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: CoverType) => {

          // Cover Type record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Cover Type record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Cover Type record = ${JSON.stringify(data)}`);

          // Search for the Cover Type record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Cover Type record in the Local Cache and returning its index`);
          let index = this._cache.coverTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Cover Type record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Cover Type record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Cover Type record was found in the Local Cache`);

            // Update the local Cover Type record
            this.log.trace(`${LOG_PREFIX} Updating the local Cover Type record`);
            this._cache.coverTypes[index] = data;

          } else {

            // The Cover Type record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Cover Type record was not found in the Local Cache`);

            // Add the Cover Type record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Cover Type record to the Local Cache`);
            this._cache.coverTypes.push(data);
          }

          // Create an up to date copy of the Cover Types records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Cover Types records`);
          const copy = Object.assign({}, this._cache).coverTypes;

          // Broadcast the up to date copy of the Cover Types records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Cover Types records to the current listener`);
          this._coverTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Cover Types records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Cover Types records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Cover Type record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Type record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Cover Type record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Type record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Type record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Cover Types records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllCoverTypes(filters?: any): Observable<CoverType[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllCoverTypes()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<CoverType[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: CoverType[]) => {

          // Cover Types records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Cover Types records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Cover Types records = ${JSON.stringify(data)}`);

          // Update the Cover Types records in the Local Cache to the newly pulled Cover Types records
          this.log.trace(`${LOG_PREFIX} Updating the Cover Types records in the Local Cache to the newly pulled Cover Types records`);
          this._cache.coverTypes = data;

          // Create an up to date copy of the Cover Types records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Cover Types records`);
          const copy = Object.assign({}, this._cache).coverTypes;

          // Broadcast the up to date copy of the Cover Types records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Cover Types records to the current listener`);
          this._coverTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Cover Types records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Cover Types records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Cover Types records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Types records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Types records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Cover Types records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Types records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Types records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Types records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Types records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }



  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._coverTypesSubject$.value;
  }
}
