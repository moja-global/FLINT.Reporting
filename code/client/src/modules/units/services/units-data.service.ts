import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Unit } from '../models/unit.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Units Data Service]";
const API_PREFIX: string = "api/v1/units";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class UnitsDataService {

  // The server's base url
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { units: Unit[] } = { units: [] };

  // The observables that allows subscribers to keep tabs of the current status 
  // of units records in the data store
  private _unitsSubject$ = new BehaviorSubject<Unit[]>([]);
  readonly units$ = this._unitsSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("units-data-channel");

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

          this.getAllUnits()
            .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
            .subscribe((response => {

              // Data store initialization complete
              this.log.trace(`${LOG_PREFIX} Data store initialization complete`);

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
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._unitsSubject$.next(event.data.newValue));
  }


  /**
   * Creates and adds an instance of a new Unit record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param unit The details of the Unit record to be created - with the id and version details missing
   */
  public createUnit(unit: Unit): Observable<Unit> {

    this.log.trace(`${LOG_PREFIX} Entering createUnit()`);
    this.log.debug(`${LOG_PREFIX} Unit = ${JSON.stringify(unit)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<Unit>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(unit), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Unit) => {

          // Unit record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Unit record = ${JSON.stringify(data)}`);

          // Add the newly created Unit record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Unit record to the Local Cache`);
          this._cache.units.push(data);

          // Create an up to date copy of the Units records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Units records`);
          const copy = Object.assign({}, this._cache).units;

          // Broadcast the up to date copy of the Units records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the current listener`);
          this._unitsSubject$.next(copy);

          // Broadcast the up to date copy of the Units records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Unit record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit record Creation was successful" });

        }),

        catchError((error: any) => {

          // Unit record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Unit record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param unitId The Unique Identifier of the Unit record
   */
  getUnit(unitId: number): Observable<Unit> {

    this.log.trace(`${LOG_PREFIX} Entering getUnit()`);
    this.log.debug(`${LOG_PREFIX} Unit Id = ${unitId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${unitId} to retrieve the record`);

    return this.http.get<Unit>(`${this._baseUrl}/${API_PREFIX}/ids/${unitId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Unit) => {

          // Unit record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Unit record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Unit record = ${JSON.stringify(data)}`);

          // Search for the Unit record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Unit record in the Local Cache and returning its index`);
          let index = this._cache.units.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Unit record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Unit record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Unit record was found in the Local Cache`);

            // Update the local Unit record
            this.log.trace(`${LOG_PREFIX} Updating the local Unit record`);
            this._cache.units[index] = data;

          } else {

            // The Unit record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Unit record was not found in the Local Cache`);

            // Add the Unit record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Unit record to the Local Cache`);
            this._cache.units.push(data);
          }

          // Create an up to date copy of the Units records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Units records`);
          const copy = Object.assign({}, this._cache).units;

          // Broadcast the up to date copy of the Units records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the current listener`);
          this._unitsSubject$.next(copy);

          // Broadcast the up to date copy of the Units records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Unit record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Unit record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Units records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllUnits(filters?: any): Observable<Unit[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllUnits()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<Unit[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: Unit[]) => {

          // Units records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Units records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Units records = ${JSON.stringify(data)}`);

          // Update the Units records in the Local Cache to the newly pulled Units records
          this.log.trace(`${LOG_PREFIX} Updating the Units records in the Local Cache to the newly pulled Units records`);
          this._cache.units = data;

          // Create an up to date copy of the Units records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Units records`);
          const copy = Object.assign({}, this._cache).units;

          // Broadcast the up to date copy of the Units records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the current listener`);
          this._unitsSubject$.next(copy);

          // Broadcast the up to date copy of the Units records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Units records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Units records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Units records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Units records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Units records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Units records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Units records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Units records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Unit record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param unit The details of the Unit record to be updated
   */
  updateUnit(unit: Unit): Observable<Unit> {

    this.log.trace(`${LOG_PREFIX} Entering updateUnit()`);
    this.log.debug(`${LOG_PREFIX} Unit = ${JSON.stringify(unit)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<Unit>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(unit), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Unit) => {

          // Unit record Update was successful
          this.log.trace(`${LOG_PREFIX} Unit record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Unit record = ${JSON.stringify(data)}`);

          // Search for the locally stored Unit record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Unit record`);
          let index = this._cache.units.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Unit record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Unit record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Unit record`);
            this._cache.units[index] = data;

            // Create an up to date copy of the Units records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Units records`);
            const copy = Object.assign({}, this._cache).units;

            // Broadcast the up to date copy of the Units records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the current listener`);
            this._unitsSubject$.next(copy);

            // Broadcast the up to date copy of the Units records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Unit record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Unit record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Units records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Unit record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Unit record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param unitId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteUnit(unitId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteUnit()`);
    this.log.debug(`${LOG_PREFIX} Unit Id = ${unitId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${unitId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${unitId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Unit record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Unit record Deletion was successful`);

            // Search for the deleted Unit record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Unit record in the Local Cache`);
            let index = this._cache.units.findIndex(d => d.id == unitId);
            this.log.debug(`${LOG_PREFIX} Deleted Unit record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Unit record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Unit record from the Local Cache`);
              this._cache.units.splice(index, 1);

              // Create an up to date copy of the Units records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Units records`);
              const copy = Object.assign({}, this._cache).units;

              // Broadcast the up to date copy of the Units records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the current listener`);
              this._unitsSubject$.next(copy);

              // Broadcast the up to date copy of the Units records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Units records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Unit record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Unit record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Units records Local Cache Update was unsuccessful" });
            }
          } else {

            // Unit record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Unit record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Unit record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Unit record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._unitsSubject$.value;
  }
}
