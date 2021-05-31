import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { FluxType } from '../models/flux-type.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Flux Types Data Service]";
const API_PREFIX: string = "api/v1/flux_types";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class FluxTypesDataService {

  // The base url of the server
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { fluxTypes: FluxType[] } = { fluxTypes: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of flux types records in the data store
  private _fluxTypesSubject$ = new BehaviorSubject<FluxType[]>([]);
  readonly fluxTypes$ = this._fluxTypesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("flux-types-data-channel");

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

          this.getAllFluxTypes()
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
    this.zone.run(() => this._fluxTypesSubject$.next(event.data.newValue));
  }


  /**
   * Creates and adds an instance of a new Flux Type record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param fluxType The details of the Flux Type record to be created - with the id and version details missing
   */
  public createFluxType(fluxType: FluxType): Observable<FluxType> {

    this.log.trace(`${LOG_PREFIX} Entering createFluxType()`);
    this.log.debug(`${LOG_PREFIX} FluxType = ${JSON.stringify(fluxType)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<FluxType>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(fluxType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: FluxType) => {

          // Flux Type record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Flux Type record = ${JSON.stringify(data)}`);

          // Add the newly created Flux Type record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Flux Type record to the Local Cache`);
          this._cache.fluxTypes.push(data);

          // Create an up to date copy of the Flux Types records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Flux Types records`);
          const copy = Object.assign({}, this._cache).fluxTypes;

          // Broadcast the up to date copy of the Flux Types records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the current listener`);
          this._fluxTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Flux Types records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Flux Type record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Flux Type record Creation was successful" });

        }),

        catchError((error: any) => {

          // Flux Type record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Flux Type record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Flux Type record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Flux Type record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Flux Type record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param fluxTypeId The Unique Identifier of the Flux Type record
   */
  getFluxType(fluxTypeId: number): Observable<FluxType> {

    this.log.trace(`${LOG_PREFIX} Entering getFluxType()`);
    this.log.debug(`${LOG_PREFIX} FluxType Id = ${fluxTypeId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${fluxTypeId} to retrieve the record`);

    return this.http.get<FluxType>(`${this._baseUrl}/${API_PREFIX}/ids/${fluxTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: FluxType) => {

          // Flux Type record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Flux Type record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Flux Type record = ${JSON.stringify(data)}`);

          // Search for the Flux Type record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Flux Type record in the Local Cache and returning its index`);
          let index = this._cache.fluxTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Flux Type record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Flux Type record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Flux Type record was found in the Local Cache`);

            // Update the local Flux Type record
            this.log.trace(`${LOG_PREFIX} Updating the local Flux Type record`);
            this._cache.fluxTypes[index] = data;

          } else {

            // The Flux Type record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Flux Type record was not found in the Local Cache`);

            // Add the Flux Type record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Flux Type record to the Local Cache`);
            this._cache.fluxTypes.push(data);
          }

          // Create an up to date copy of the Flux Types records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Flux Types records`);
          const copy = Object.assign({}, this._cache).fluxTypes;

          // Broadcast the up to date copy of the Flux Types records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the current listener`);
          this._fluxTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Flux Types records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Flux Type record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Flux Type record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Flux Type record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Flux Type record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Flux Type record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Flux Type record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Flux Types records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllFluxTypes(filters?: any): Observable<FluxType[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllFluxTypes()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<FluxType[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: FluxType[]) => {

          // Flux Types records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Flux Types records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Flux Types records = ${JSON.stringify(data)}`);

          // Update the Flux Types records in the Local Cache to the newly pulled Flux Types records
          this.log.trace(`${LOG_PREFIX} Updating the Flux Types records in the Local Cache to the newly pulled Flux Types records`);
          this._cache.fluxTypes = data;

          // Create an up to date copy of the Flux Types records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Flux Types records`);
          const copy = Object.assign({}, this._cache).fluxTypes;

          // Broadcast the up to date copy of the Flux Types records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the current listener`);
          this._fluxTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Flux Types records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Flux Types records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Types records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Flux Types records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Flux Types records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Flux Types records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Flux Types records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Types records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Flux Types records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Flux Type record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param fluxType The details of the Flux Type record to be updated
   */
  updateFluxType(fluxType: FluxType): Observable<FluxType> {

    this.log.trace(`${LOG_PREFIX} Entering updateFluxType()`);
    this.log.debug(`${LOG_PREFIX} FluxType = ${JSON.stringify(fluxType)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<FluxType>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(fluxType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: FluxType) => {

          // Flux Type record Update was successful
          this.log.trace(`${LOG_PREFIX} Flux Type record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Flux Type record = ${JSON.stringify(data)}`);

          // Search for the locally stored Flux Type record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Flux Type record`);
          let index = this._cache.fluxTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Flux Type record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Flux Type record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Flux Type record`);
            this._cache.fluxTypes[index] = data;

            // Create an up to date copy of the Flux Types records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Flux Types records`);
            const copy = Object.assign({}, this._cache).fluxTypes;

            // Broadcast the up to date copy of the Flux Types records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the current listener`);
            this._fluxTypesSubject$.next(copy);

            // Broadcast the up to date copy of the Flux Types records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Flux Type record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Flux Type record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Flux Type record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Flux Types records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Flux Type record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Flux Type record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Flux Type record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Flux Type record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Flux Type record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param fluxTypeId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteFluxType(fluxTypeId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteFluxType()`);
    this.log.debug(`${LOG_PREFIX} FluxType Id = ${fluxTypeId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${fluxTypeId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${fluxTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Flux Type record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Flux Type record Deletion was successful`);

            // Search for the deleted Flux Type record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Flux Type record in the Local Cache`);
            let index = this._cache.fluxTypes.findIndex(d => d.id == fluxTypeId);
            this.log.debug(`${LOG_PREFIX} Deleted Flux Type record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Flux Type record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Flux Type record from the Local Cache`);
              this._cache.fluxTypes.splice(index, 1);

              // Create an up to date copy of the Flux Types records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Flux Types records`);
              const copy = Object.assign({}, this._cache).fluxTypes;

              // Broadcast the up to date copy of the Flux Types records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the current listener`);
              this._fluxTypesSubject$.next(copy);

              // Broadcast the up to date copy of the Flux Types records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Flux Types records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Flux Type record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Flux Type record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Flux Type record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Flux Types records Local Cache Update was unsuccessful" });
            }
          } else {

            // Flux Type record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Flux Type record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Flux Type record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Flux Type record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Flux Type record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Flux Type record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Flux Type record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Flux Type record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Flux Type record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._fluxTypesSubject$.value;
  }
}