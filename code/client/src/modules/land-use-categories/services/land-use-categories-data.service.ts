import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';
import { LandUseCategory } from '../models/land-use-category.model';

const LOG_PREFIX: string = "[Land Use Categories Data Service]";
const API_PREFIX: string = "api/v1/land_use_categories";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class LandUseCategoriesDataService {

  // The server's base url
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { landUseCategories: LandUseCategory[] } = { landUseCategories: [] };

  // The observables that allows subscribers to keep tabs of the current status 
  // of landUseCategories records in the data store
  private _landUseCategoriesSubject$ = new BehaviorSubject<LandUseCategory[]>([]);
  readonly landUseCategories$ = this._landUseCategoriesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("landUseCategories-data-channel");

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

          this.getAllLandUseCategories()
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
    this.zone.run(() => this._landUseCategoriesSubject$.next(event.data.newValue));
  }


  /**
   * Creates and adds an instance of a new Land Use Category record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param landUseCategory The details of the Land Use Category record to be created - with the id and version details missing
   */
  public createLandUseCategory(landUseCategory: LandUseCategory): Observable<LandUseCategory> {

    this.log.trace(`${LOG_PREFIX} Entering createLandUseCategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Category = ${JSON.stringify(landUseCategory)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<LandUseCategory>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(landUseCategory), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: LandUseCategory) => {

          // Land Use Category record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Land Use Category record = ${JSON.stringify(data)}`);

          // Add the newly created Land Use Category record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Land Use Category record to the Local Cache`);
          this._cache.landUseCategories.push(data);

          // Create an up to date copy of the Land Use Categories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Categories records`);
          const copy = Object.assign({}, this._cache).landUseCategories;

          // Broadcast the up to date copy of the Land Use Categories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the current listener`);
          this._landUseCategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Land Use Categories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Land Use Category record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Category record Creation was successful" });

        }),

        catchError((error: any) => {

          // Land Use Category record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Category record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Category record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Land Use Category record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param landUseCategoryId The Unique Identifier of the Land Use Category record
   */
  getLandUseCategory(landUseCategoryId: number): Observable<LandUseCategory> {

    this.log.trace(`${LOG_PREFIX} Entering getLandUseCategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Category Id = ${landUseCategoryId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${landUseCategoryId} to retrieve the record`);

    return this.http.get<LandUseCategory>(`${this._baseUrl}/${API_PREFIX}/ids/${landUseCategoryId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: LandUseCategory) => {

          // Land Use Category record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Land Use Category record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Land Use Category record = ${JSON.stringify(data)}`);

          // Search for the Land Use Category record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Land Use Category record in the Local Cache and returning its index`);
          let index = this._cache.landUseCategories.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Land Use Category record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Land Use Category record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Land Use Category record was found in the Local Cache`);

            // Update the local Land Use Category record
            this.log.trace(`${LOG_PREFIX} Updating the local Land Use Category record`);
            this._cache.landUseCategories[index] = data;

          } else {

            // The Land Use Category record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Land Use Category record was not found in the Local Cache`);

            // Add the Land Use Category record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Land Use Category record to the Local Cache`);
            this._cache.landUseCategories.push(data);
          }

          // Create an up to date copy of the Land Use Categories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Categories records`);
          const copy = Object.assign({}, this._cache).landUseCategories;

          // Broadcast the up to date copy of the Land Use Categories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the current listener`);
          this._landUseCategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Land Use Categories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Land Use Category record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Category record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Land Use Category record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Category record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Category record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Land Use Categories records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllLandUseCategories(filters?: any): Observable<LandUseCategory[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllLandUseCategories()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<LandUseCategory[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: LandUseCategory[]) => {

          // Land Use Categories records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Land Use Categories records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Land Use Categories records = ${JSON.stringify(data)}`);

          // Update the Land Use Categories records in the Local Cache to the newly pulled Land Use Categories records
          this.log.trace(`${LOG_PREFIX} Updating the Land Use Categories records in the Local Cache to the newly pulled Land Use Categories records`);
          this._cache.landUseCategories = data;

          // Create an up to date copy of the Land Use Categories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Categories records`);
          const copy = Object.assign({}, this._cache).landUseCategories;

          // Broadcast the up to date copy of the Land Use Categories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the current listener`);
          this._landUseCategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Land Use Categories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Land Use Categories records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Categories records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Categories records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Land Use Categories records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Categories records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Categories records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Categories records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Categories records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Land Use Category record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param landUseCategory The details of the Land Use Category record to be updated
   */
  updateLandUseCategory(landUseCategory: LandUseCategory): Observable<LandUseCategory> {

    this.log.trace(`${LOG_PREFIX} Entering updateLandUseCategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Category = ${JSON.stringify(landUseCategory)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<LandUseCategory>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(landUseCategory), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: LandUseCategory) => {

          // Land Use Category record Update was successful
          this.log.trace(`${LOG_PREFIX} Land Use Category record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Land Use Category record = ${JSON.stringify(data)}`);

          // Search for the locally stored Land Use Category record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Land Use Category record`);
          let index = this._cache.landUseCategories.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Land Use Category record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Land Use Category record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Land Use Category record`);
            this._cache.landUseCategories[index] = data;

            // Create an up to date copy of the Land Use Categories records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Categories records`);
            const copy = Object.assign({}, this._cache).landUseCategories;

            // Broadcast the up to date copy of the Land Use Categories records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the current listener`);
            this._landUseCategoriesSubject$.next(copy);

            // Broadcast the up to date copy of the Land Use Categories records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Land Use Category record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Category record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Land Use Category record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Land Use Categories records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Land Use Category record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Category record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Category record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Land Use Category record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param landUseCategoryId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteLandUseCategory(landUseCategoryId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteLandUseCategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Category Id = ${landUseCategoryId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${landUseCategoryId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${landUseCategoryId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Land Use Category record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Land Use Category record Deletion was successful`);

            // Search for the deleted Land Use Category record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Land Use Category record in the Local Cache`);
            let index = this._cache.landUseCategories.findIndex(d => d.id == landUseCategoryId);
            this.log.debug(`${LOG_PREFIX} Deleted Land Use Category record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Land Use Category record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Land Use Category record from the Local Cache`);
              this._cache.landUseCategories.splice(index, 1);

              // Create an up to date copy of the Land Use Categories records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Land Use Categories records`);
              const copy = Object.assign({}, this._cache).landUseCategories;

              // Broadcast the up to date copy of the Land Use Categories records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the current listener`);
              this._landUseCategoriesSubject$.next(copy);

              // Broadcast the up to date copy of the Land Use Categories records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Land Use Categories records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Land Use Category record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Category record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Land Use Category record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Land Use Categories records Local Cache Update was unsuccessful" });
            }
          } else {

            // Land Use Category record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Land Use Category record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Land Use Category record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Land Use Category record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Category record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Category record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._landUseCategoriesSubject$.value;
  }
}
