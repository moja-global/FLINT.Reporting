import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Pool } from '../models/pool.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Pools Data Service]";
const API_PREFIX: string = "api/v1/pools";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class PoolsDataService {

  // The local data cache
  private _cache: { pools: Pool[] } = { pools: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _poolsSubject$ = new BehaviorSubject<Pool[]>([]);
  readonly pools$ = this._poolsSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("pools-data-channel");

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

          this.getAllPools()
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
   * Creates and adds an instance of a new Pool record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param pool The details of the Pool record to be created - with the id and version details missing
   */
  public createPool(pool: Pool): Observable<Pool> {

    this.log.trace(`${LOG_PREFIX} Entering createPool()`);
    this.log.debug(`${LOG_PREFIX} Pool = ${JSON.stringify(pool)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.poolsBaseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<Pool>(`${environment.poolsBaseUrl}/${API_PREFIX}`, JSON.stringify(pool), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Pool) => {

          // Pool record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Pool record = ${JSON.stringify(data)}`);

          // Add the newly created Pool record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Pool record to the Local Cache`);
          this._cache.pools.push(data);

          // Create an up to date copy of the Pools Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Pools Records`);
          const copy = Object.assign({}, this._cache).pools;

          // Broadcast the up to date copy of the Pools Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Pools Records to the current listener`);
          this._poolsSubject$.next(copy);

          // Broadcast the up to date copy of the Pools Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Pools Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Pool record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Pool record Creation was successful" });

        }),

        catchError((error: any) => {

          // Pool record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Pool record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Pool record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Pool record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._poolsSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Pools Records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllPools(filters?: any): Observable<Pool[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllPools()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.poolsBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<Pool[]>(`${environment.poolsBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: Pool[]) => {

          // Pools Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Pools Records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Pools Records = ${JSON.stringify(data)}`);

          // Update the Pools Records in the Local Cache to the newly pulled Pools Records
          this.log.trace(`${LOG_PREFIX} Updating the Pools Records in the Local Cache to the newly pulled Pools Records`);
          this._cache.pools = data;

          // Create an up to date copy of the Pools Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Pools Records`);
          const copy = Object.assign({}, this._cache).pools;

          // Broadcast the up to date copy of the Pools Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Pools Records to the current listener`);
          this._poolsSubject$.next(copy);

          // Broadcast the up to date copy of the Pools Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Pools Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Pools Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pools Records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Pools Records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Pools Records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Pools Records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Pools Records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pools Records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Pools Records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Pool record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param pool The details of the Pool record to be updated
   */
  updatePool(pool: Pool): Observable<Pool> {

    this.log.trace(`${LOG_PREFIX} Entering updatePool()`);
    this.log.debug(`${LOG_PREFIX} Pool = ${JSON.stringify(pool)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.poolsBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<Pool>(`${environment.poolsBaseUrl}/${API_PREFIX}`, JSON.stringify(pool), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Pool) => {

          // Pool record Update was successful
          this.log.trace(`${LOG_PREFIX} Pool record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Pool record = ${JSON.stringify(data)}`);

          // Search for the locally stored Pool record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Pool record`);
          let index = this._cache.pools.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Pool record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Pool record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Pool record`);
            this._cache.pools[index] = data;

            // Create an up to date copy of the Pools Records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Pools Records`);
            const copy = Object.assign({}, this._cache).pools;

            // Broadcast the up to date copy of the Pools Records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Pools Records to the current listener`);
            this._poolsSubject$.next(copy);

            // Broadcast the up to date copy of the Pools Records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Pools Records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Pool record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Pool record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Pool record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Pools Records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Pool record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Pool record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Pool record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Pool record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Pool record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param poolId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deletePool(poolId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deletePool()`);
    this.log.debug(`${LOG_PREFIX} Pool Id = ${poolId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.poolsBaseUrl}/${API_PREFIX}/ids/${poolId} to delete the record`);

    return this.http.delete<number>(`${environment.poolsBaseUrl}/${API_PREFIX}/ids/${poolId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Pool record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Pool record Deletion was successful`);

            // Search for the deleted Pool record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Pool record in the Local Cache`);
            let index = this._cache.pools.findIndex(d => d.id == poolId);
            this.log.debug(`${LOG_PREFIX} Deleted Pool record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Pool record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Pool record from the Local Cache`);
              this._cache.pools.splice(index, 1);

              // Create an up to date copy of the Pools Records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Pools Records`);
              const copy = Object.assign({}, this._cache).pools;

              // Broadcast the up to date copy of the Pools Records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Pools Records to the current listener`);
              this._poolsSubject$.next(copy);

              // Broadcast the up to date copy of the Pools Records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Pools Records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Pool record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Pool record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Pool record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Pools Records Local Cache Update was unsuccessful" });
            }
          } else {

            // Pool record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Pool record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Pool record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Pool record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Pool record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Pool record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Pool record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Pool record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._poolsSubject$.value;
  }
}
