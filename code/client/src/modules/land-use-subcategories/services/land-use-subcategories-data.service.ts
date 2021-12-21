import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';
import { LandUseSubcategory } from '../models/land-use-subcategory.model';

const LOG_PREFIX: string = "[Land Use Subcategories Data Service]";
const API_PREFIX: string = "api/v1/land_use_subcategories";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class LandUseSubcategoriesDataService {

  // The server's base url
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { landUseSubcategories: LandUseSubcategory[] } = { landUseSubcategories: [] };

  // The observables that allows subscribers to keep tabs of the current status 
  // of landUseSubcategories records in the data store
  private _landUseSubcategoriesSubject$ = new BehaviorSubject<LandUseSubcategory[]>([]);
  readonly landUseSubcategories$ = this._landUseSubcategoriesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("landUseSubcategories-data-channel");

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

          this.getAllLandUseSubcategories()
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
    this.zone.run(() => this._landUseSubcategoriesSubject$.next(event.data.newValue));
  }


  /**
   * Creates and adds an instance of a new Land Use Subcategory record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param landUseSubcategory The details of the Land Use Subcategory record to be created - with the id and version details missing
   */
  public createLandUseSubcategory(landUseSubcategory: LandUseSubcategory): Observable<LandUseSubcategory> {

    this.log.trace(`${LOG_PREFIX} Entering createLandUseSubcategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Subcategory = ${JSON.stringify(landUseSubcategory)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<LandUseSubcategory>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(landUseSubcategory), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: LandUseSubcategory) => {

          // Land Use Subcategory record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Land Use Subcategory record = ${JSON.stringify(data)}`);

          // Add the newly created Land Use Subcategory record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Land Use Subcategory record to the Local Cache`);
          this._cache.landUseSubcategories.push(data);

          // Create an up to date copy of the Land Use Subcategories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Subcategories records`);
          const copy = Object.assign({}, this._cache).landUseSubcategories;

          // Broadcast the up to date copy of the Land Use Subcategories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the current listener`);
          this._landUseSubcategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Land Use Subcategories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Land Use Subcategory record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Subcategory record Creation was successful" });

        }),

        catchError((error: any) => {

          // Land Use Subcategory record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Subcategory record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Subcategory record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Subcategory record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Land Use Subcategory record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param landUseSubcategoryId The Unique Identifier of the Land Use Subcategory record
   */
  getLandUseSubcategory(landUseSubcategoryId: number): Observable<LandUseSubcategory> {

    this.log.trace(`${LOG_PREFIX} Entering getLandUseSubcategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Subcategory Id = ${landUseSubcategoryId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${landUseSubcategoryId} to retrieve the record`);

    return this.http.get<LandUseSubcategory>(`${this._baseUrl}/${API_PREFIX}/ids/${landUseSubcategoryId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: LandUseSubcategory) => {

          // Land Use Subcategory record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Land Use Subcategory record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Land Use Subcategory record = ${JSON.stringify(data)}`);

          // Search for the Land Use Subcategory record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Land Use Subcategory record in the Local Cache and returning its index`);
          let index = this._cache.landUseSubcategories.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Land Use Subcategory record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Land Use Subcategory record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Land Use Subcategory record was found in the Local Cache`);

            // Update the local Land Use Subcategory record
            this.log.trace(`${LOG_PREFIX} Updating the local Land Use Subcategory record`);
            this._cache.landUseSubcategories[index] = data;

          } else {

            // The Land Use Subcategory record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Land Use Subcategory record was not found in the Local Cache`);

            // Add the Land Use Subcategory record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Land Use Subcategory record to the Local Cache`);
            this._cache.landUseSubcategories.push(data);
          }

          // Create an up to date copy of the Land Use Subcategories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Subcategories records`);
          const copy = Object.assign({}, this._cache).landUseSubcategories;

          // Broadcast the up to date copy of the Land Use Subcategories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the current listener`);
          this._landUseSubcategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Land Use Subcategories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Land Use Subcategory record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Subcategory record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Land Use Subcategory record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Subcategory record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Subcategory record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Subcategory record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Land Use Subcategories records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllLandUseSubcategories(filters?: any): Observable<LandUseSubcategory[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllLandUseSubcategories()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<LandUseSubcategory[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: LandUseSubcategory[]) => {

          // Land Use Subcategories records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Land Use Subcategories records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Land Use Subcategories records = ${JSON.stringify(data)}`);

          // Update the Land Use Subcategories records in the Local Cache to the newly pulled Land Use Subcategories records
          this.log.trace(`${LOG_PREFIX} Updating the Land Use Subcategories records in the Local Cache to the newly pulled Land Use Subcategories records`);
          this._cache.landUseSubcategories = data;

          // Create an up to date copy of the Land Use Subcategories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Subcategories records`);
          const copy = Object.assign({}, this._cache).landUseSubcategories;

          // Broadcast the up to date copy of the Land Use Subcategories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the current listener`);
          this._landUseSubcategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Land Use Subcategories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Land Use Subcategories records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategories records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Subcategories records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Land Use Subcategories records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Subcategories records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Subcategories records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategories records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Subcategories records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Land Use Subcategory record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param landUseSubcategory The details of the Land Use Subcategory record to be updated
   */
  updateLandUseSubcategory(landUseSubcategory: LandUseSubcategory): Observable<LandUseSubcategory> {

    this.log.trace(`${LOG_PREFIX} Entering updateLandUseSubcategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Subcategory = ${JSON.stringify(landUseSubcategory)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<LandUseSubcategory>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(landUseSubcategory), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: LandUseSubcategory) => {

          // Land Use Subcategory record Update was successful
          this.log.trace(`${LOG_PREFIX} Land Use Subcategory record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Land Use Subcategory record = ${JSON.stringify(data)}`);

          // Search for the locally stored Land Use Subcategory record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Land Use Subcategory record`);
          let index = this._cache.landUseSubcategories.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Land Use Subcategory record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Land Use Subcategory record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Land Use Subcategory record`);
            this._cache.landUseSubcategories[index] = data;

            // Create an up to date copy of the Land Use Subcategories records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Subcategories records`);
            const copy = Object.assign({}, this._cache).landUseSubcategories;

            // Broadcast the up to date copy of the Land Use Subcategories records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the current listener`);
            this._landUseSubcategoriesSubject$.next(copy);

            // Broadcast the up to date copy of the Land Use Subcategories records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Land Use Subcategory record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Subcategory record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Land Use Subcategory record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Land Use Subcategories records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Land Use Subcategory record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Subcategory record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Subcategory record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Subcategory record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Land Use Subcategory record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param landUseSubcategoryId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteLandUseSubcategory(landUseSubcategoryId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteLandUseSubcategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Subcategory Id = ${landUseSubcategoryId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${landUseSubcategoryId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${landUseSubcategoryId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Land Use Subcategory record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Land Use Subcategory record Deletion was successful`);

            // Search for the deleted Land Use Subcategory record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Land Use Subcategory record in the Local Cache`);
            let index = this._cache.landUseSubcategories.findIndex(d => d.id == landUseSubcategoryId);
            this.log.debug(`${LOG_PREFIX} Deleted Land Use Subcategory record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Land Use Subcategory record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Land Use Subcategory record from the Local Cache`);
              this._cache.landUseSubcategories.splice(index, 1);

              // Create an up to date copy of the Land Use Subcategories records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Subcategories records`);
              const copy = Object.assign({}, this._cache).landUseSubcategories;

              // Broadcast the up to date copy of the Land Use Subcategories records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the current listener`);
              this._landUseSubcategoriesSubject$.next(copy);

              // Broadcast the up to date copy of the Land Use Subcategories records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Subcategories records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Land Use Subcategory record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Subcategory record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Land Use Subcategory record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Land Use Subcategories records Local Cache Update was unsuccessful" });
            }
          } else {

            // Land Use Subcategory record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Land Use Subcategory record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Land Use Subcategory record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Subcategory record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Land Use Subcategory record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Subcategory record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Subcategory record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Subcategory record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Subcategory record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._landUseSubcategoriesSubject$.value;
  }
}
