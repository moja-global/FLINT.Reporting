import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Pool } from '../models/pool.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { MessageService } from '@common/services';
import { catchError, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Pools Data Service]";
const API_PREFIX: string = "api/v1/pools";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class PoolsDataService {

  private _baseUrl: string = environment.baseUrl;
  private _cache: { pools: Pool[] } = { pools: [] };
  private _subject$ = new BehaviorSubject<Pool[]>([]);

  readonly pools$ = this._subject$.asObservable();

  constructor(
    private http: HttpClient,
    private log: NGXLogger,
    private messageService: MessageService) {

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
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<Pool>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(pool), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Pool) => {

          // Pool record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Pool record = ${JSON.stringify(data)}`);

          // Add the newly created Pool record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Pool record to the Local Cache`);
          this._cache.pools.push(data);

          // Push a copy of the newly updated Pools records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Pools records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).pools);

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
   * Retrieves and adds a single Pool record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param poolId The Unique Identifier of the Pool record
   */
  getPool(poolId: number): Observable<Pool> {

    this.log.trace(`${LOG_PREFIX} Entering getPool()`);
    this.log.debug(`${LOG_PREFIX} Pool Id = ${poolId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${poolId} to retrieve the record`);

    return this.http.get<Pool>(`${this._baseUrl}/${API_PREFIX}/ids/${poolId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Pool) => {

          // Pool record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Pool record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Pool record = ${JSON.stringify(data)}`);

          // Search for the Pool record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Pool record in the Local Cache and returning its index`);
          let index = this._cache.pools.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Pool record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Pool record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Pool record was found in the Local Cache`);

            // Update the local Pool record
            this.log.trace(`${LOG_PREFIX} Updating the local Pool record`);
            this._cache.pools[index] = data;

          } else {

            // The Pool record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Pool record was not found in the Local Cache`);

            // Add the Pool record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Pool record to the Local Cache`);
            this._cache.pools.push(data);
          }

          // Push a copy of the newly updated Pools records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Pools records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).pools);

          // Send a message that states that the Pool record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Pool record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Pool record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Pool record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Pool record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Pool record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Pools records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllPools(filters?: any): Observable<Pool[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllPools()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<Pool[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: Pool[]) => {

          // Pools records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Pools records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Pools records = ${JSON.stringify(data)}`);

          // Update the Pools records in the Local Cache to the newly pulled Pools records
          this.log.trace(`${LOG_PREFIX} Updating the Pools records in the Local Cache to the newly pulled Pools records`);
          this._cache.pools = data;

          // Push a copy of the newly updated Pools records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Pools records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).pools);

          // Send a message that states that the Pools records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pools records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Pools records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Pools records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Pools records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Pools records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pools records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Pools records Retrieval was unsuccessful" });

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
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<Pool>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(pool), { headers: new HttpHeaders(HEADERS) })
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

            // Push a copy of the newly updated Pools records to all Subscribers
            this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Pools records to all Subscribers`);
            this._subject$.next(Object.assign({}, this._cache).pools);

            // Send a message that states that the Pool record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Pool record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Pool record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Pools records Local Cache Update was unsuccessful" });
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

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${poolId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${poolId}`, { headers: new HttpHeaders(HEADERS) })
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

              // Push a copy of the newly updated Pools records to all Subscribers
              this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Pools records to all Subscribers`);
              this._subject$.next(Object.assign({}, this._cache).pools);

              // Send a message that states that the Pool record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Pool record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Pool record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Pool record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Pools records Local Cache Update was unsuccessful" });
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
    return this._subject$.value;
  }
}
