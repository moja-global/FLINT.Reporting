import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';
import { LandUseCategory } from '../models/land-use-category.model';
import { environment } from 'environments/environment';

const LOG_PREFIX: string = "[Land Uses Categories Data Service]";
const API_PREFIX: string = "api/v1/land_use_categories";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class LandUsesCategoriesDataService {

  // The local data cache
  private _cache: { landUsesCategories: LandUseCategory[] } = { landUsesCategories: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _landUsesCategoriesSubject$ = new BehaviorSubject<LandUseCategory[]>([]);
  readonly landUsesCategories$ = this._landUsesCategoriesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("landUsesCategories-data-channel");

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

          this.getAllLandUsesCategories()
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
   * Creates and adds an instance of a new Land Use Category Record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param landUseCategory The details of the Land Use Category Record to be created - with the id and version details missing
   */
  public createLandUseCategory(landUseCategory: LandUseCategory): Observable<LandUseCategory> {

    this.log.trace(`${LOG_PREFIX} Entering createLandUseCategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Category = ${JSON.stringify(landUseCategory)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.landUsesCategoriesBaseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<LandUseCategory>(`${environment.landUsesCategoriesBaseUrl}/${API_PREFIX}`, JSON.stringify(landUseCategory), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: LandUseCategory) => {

          // Land Use Category Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Land Use Category Record = ${JSON.stringify(data)}`);

          // Add the newly created Land Use Category Record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Land Use Category Record to the Local Cache`);
          this._cache.landUsesCategories.push(data);

          // Create an up to date copy of the LandUsesCategories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the LandUsesCategories records`);
          const copy = Object.assign({}, this._cache).landUsesCategories;

          // Broadcast the up to date copy of the LandUsesCategories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the LandUsesCategories records to the current listener`);
          this._landUsesCategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the LandUsesCategories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the LandUsesCategories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Land Use Category Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category Record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Category Record Creation was successful" });

        }),

        catchError((error: any) => {

          // Land Use Category Record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Category Record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Category Record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category Record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category Record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._landUsesCategoriesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all LandUsesCategories records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllLandUsesCategories(filters?: any): Observable<LandUseCategory[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllLandUsesCategories()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.landUsesCategoriesBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<LandUseCategory[]>(`${environment.landUsesCategoriesBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: LandUseCategory[]) => {

          // LandUsesCategories records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} LandUsesCategories records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved LandUsesCategories records = ${JSON.stringify(data)}`);

          // Update the LandUsesCategories records in the Local Cache to the newly pulled LandUsesCategories records
          this.log.trace(`${LOG_PREFIX} Updating the LandUsesCategories records in the Local Cache to the newly pulled LandUsesCategories records`);
          this._cache.landUsesCategories = data;

          // Create an up to date copy of the LandUsesCategories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the LandUsesCategories records`);
          const copy = Object.assign({}, this._cache).landUsesCategories;

          // Broadcast the up to date copy of the LandUsesCategories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the LandUsesCategories records to the current listener`);
          this._landUsesCategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the LandUsesCategories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the LandUsesCategories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the LandUsesCategories records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the LandUsesCategories records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The LandUsesCategories records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // LandUsesCategories records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} LandUsesCategories records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the LandUsesCategories records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the LandUsesCategories records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The LandUsesCategories records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Land Use Category Record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param landUseCategory The details of the Land Use Category Record to be updated
   */
  updateLandUseCategory(landUseCategory: LandUseCategory): Observable<LandUseCategory> {

    this.log.trace(`${LOG_PREFIX} Entering updateLandUseCategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Category = ${JSON.stringify(landUseCategory)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.landUsesCategoriesBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<LandUseCategory>(`${environment.landUsesCategoriesBaseUrl}/${API_PREFIX}`, JSON.stringify(landUseCategory), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: LandUseCategory) => {

          // Land Use Category Record Update was successful
          this.log.trace(`${LOG_PREFIX} Land Use Category Record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Land Use Category Record = ${JSON.stringify(data)}`);

          // Search for the locally stored Land Use Category Record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Land Use Category Record`);
          let index = this._cache.landUsesCategories.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Land Use Category Record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Land Use Category Record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Land Use Category Record`);
            this._cache.landUsesCategories[index] = data;

            // Create an up to date copy of the LandUsesCategories records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the LandUsesCategories records`);
            const copy = Object.assign({}, this._cache).landUsesCategories;

            // Broadcast the up to date copy of the LandUsesCategories records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the LandUsesCategories records to the current listener`);
            this._landUsesCategoriesSubject$.next(copy);

            // Broadcast the up to date copy of the LandUsesCategories records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the LandUsesCategories records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Land Use Category Record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category Record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Category Record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Land Use Category Record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "LandUsesCategories records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Land Use Category Record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Category Record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Category Record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category Record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category Record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Land Use Category Record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param landUseCategoryId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteLandUseCategory(landUseCategoryId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteLandUseCategory()`);
    this.log.debug(`${LOG_PREFIX} Land Use Category Id = ${landUseCategoryId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.landUsesCategoriesBaseUrl}/${API_PREFIX}/ids/${landUseCategoryId} to delete the record`);

    return this.http.delete<number>(`${environment.landUsesCategoriesBaseUrl}/${API_PREFIX}/ids/${landUseCategoryId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Land Use Category Record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Land Use Category Record Deletion was successful`);

            // Search for the deleted Land Use Category Record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Land Use Category Record in the Local Cache`);
            let index = this._cache.landUsesCategories.findIndex(d => d.id == landUseCategoryId);
            this.log.debug(`${LOG_PREFIX} Deleted Land Use Category Record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Land Use Category Record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Land Use Category Record from the Local Cache`);
              this._cache.landUsesCategories.splice(index, 1);

              // Create an up to date copy of the LandUsesCategories records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the LandUsesCategories records`);
              const copy = Object.assign({}, this._cache).landUsesCategories;

              // Broadcast the up to date copy of the LandUsesCategories records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the LandUsesCategories records to the current listener`);
              this._landUsesCategoriesSubject$.next(copy);

              // Broadcast the up to date copy of the LandUsesCategories records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the LandUsesCategories records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Land Use Category Record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category Record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Land Use Category Record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Land Use Category Record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "LandUsesCategories records Local Cache Update was unsuccessful" });
            }
          } else {

            // Land Use Category Record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Land Use Category Record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Land Use Category Record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category Record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category Record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Land Use Category Record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Land Use Category Record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Land Use Category Record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Land Use Category Record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Land Use Category Record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._landUsesCategoriesSubject$.value;
  }
}
