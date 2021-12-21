import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { UnitCategory } from '../models/unit-category.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Unit Categories Data Service]";
const API_PREFIX: string = "api/v1/unit_categories";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class UnitCategoriesDataService {

  // The base url of the server
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { unitCategories: UnitCategory[] } = { unitCategories: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _unitCategoriesSubject$: BehaviorSubject<Array<UnitCategory>> = new BehaviorSubject<Array<UnitCategory>>([]);
  readonly unitCategories$: Observable<Array<UnitCategory>> = this._unitCategoriesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("unit-categories-data-channel");

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

          this.getAllUnitCategories()
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
   * Publish information to current (listening) ui
   * @param event 
   */
   private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._unitCategoriesSubject$.next(event.data.newValue));
  }
    

  /**
   * Creates and adds an instance of a new Unit Category record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param unitCategory The details of the Unit Category record to be created - with the id and version details missing
   */
  public createUnitCategory(unitCategory: UnitCategory): Observable<UnitCategory> {

    this.log.trace(`${LOG_PREFIX} Entering createUnitCategory()`);
    this.log.debug(`${LOG_PREFIX} UnitCategory = ${JSON.stringify(unitCategory)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<UnitCategory>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(unitCategory), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: UnitCategory) => {

          // Unit Category record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Unit Category record = ${JSON.stringify(data)}`);

          // Add the newly created Unit Category record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Unit Category record to the Local Cache`);
          this._cache.unitCategories.push(data);

          // Create an up to date copy of the Unit Categories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
          const copy = Object.assign({}, this._cache).unitCategories;

          // Broadcast the up to date copy of the Unit Categories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
          this._unitCategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Unit Categories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Unit Category record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit Category record Creation was successful" });

        }),

        catchError((error: any) => {

          // Unit Category record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit Category record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit Category record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit Category record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Unit Category record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param unitCategoryId The Unique Identifier of the Unit Category record
   */
  getUnitCategory(unitCategoryId: number): Observable<UnitCategory> {

    this.log.trace(`${LOG_PREFIX} Entering getUnitCategory()`);
    this.log.debug(`${LOG_PREFIX} UnitCategory Id = ${unitCategoryId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${unitCategoryId} to retrieve the record`);

    return this.http.get<UnitCategory>(`${this._baseUrl}/${API_PREFIX}/ids/${unitCategoryId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: UnitCategory) => {

          // Unit Category record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Unit Category record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Unit Category record = ${JSON.stringify(data)}`);

          // Search for the Unit Category record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Unit Category record in the Local Cache and returning its index`);
          let index = this._cache.unitCategories.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Unit Category record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Unit Category record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Unit Category record was found in the Local Cache`);

            // Update the local Unit Category record
            this.log.trace(`${LOG_PREFIX} Updating the local Unit Category record`);
            this._cache.unitCategories[index] = data;

          } else {

            // The Unit Category record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Unit Category record was not found in the Local Cache`);

            // Add the Unit Category record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Unit Category record to the Local Cache`);
            this._cache.unitCategories.push(data);
          }

          // Create an up to date copy of the Unit Categories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
          const copy = Object.assign({}, this._cache).unitCategories;

          // Broadcast the up to date copy of the Unit Categories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
          this._unitCategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Unit Categories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Unit Category record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit Category record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Unit Category record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit Category record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit Category record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit Category record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Unit Categories records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllUnitCategories(filters?: any): Observable<UnitCategory[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllUnitCategories()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<UnitCategory[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: UnitCategory[]) => {

          // Unit Categories records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Unit Categories records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Unit Categories records = ${JSON.stringify(data)}`);

          // Update the Unit Categories records in the Local Cache to the newly pulled Unit Categories records
          this.log.trace(`${LOG_PREFIX} Updating the Unit Categories records in the Local Cache to the newly pulled Unit Categories records`);
          this._cache.unitCategories = data;

          // Create an up to date copy of the Unit Categories records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
          const copy = Object.assign({}, this._cache).unitCategories;

          // Broadcast the up to date copy of the Unit Categories records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
          this._unitCategoriesSubject$.next(copy);

          // Broadcast the up to date copy of the Unit Categories records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Unit Categories records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Categories records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit Categories records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Unit Categories records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit Categories records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit Categories records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Categories records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit Categories records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Unit Category record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param unitCategory The details of the Unit Category record to be updated
   */
  updateUnitCategory(unitCategory: UnitCategory): Observable<UnitCategory> {

    this.log.trace(`${LOG_PREFIX} Entering updateUnitCategory()`);
    this.log.debug(`${LOG_PREFIX} UnitCategory = ${JSON.stringify(unitCategory)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<UnitCategory>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(unitCategory), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: UnitCategory) => {

          // Unit Category record Update was successful
          this.log.trace(`${LOG_PREFIX} Unit Category record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Unit Category record = ${JSON.stringify(data)}`);

          // Search for the locally stored Unit Category record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Unit Category record`);
          let index = this._cache.unitCategories.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Unit Category record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Unit Category record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Unit Category record`);
            this._cache.unitCategories[index] = data;

            // Create an up to date copy of the Unit Categories records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
            const copy = Object.assign({}, this._cache).unitCategories;

            // Broadcast the up to date copy of the Unit Categories records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
            this._unitCategoriesSubject$.next(copy);

            // Broadcast the up to date copy of the Unit Categories records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Unit Category record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit Category record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Unit Category record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Unit Categories records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Unit Category record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit Category record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit Category record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit Category record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Unit Category record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param unitCategoryId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteUnitCategory(unitCategoryId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteUnitCategory()`);
    this.log.debug(`${LOG_PREFIX} UnitCategory Id = ${unitCategoryId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${unitCategoryId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${unitCategoryId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Unit Category record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Unit Category record Deletion was successful`);

            // Search for the deleted Unit Category record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Unit Category record in the Local Cache`);
            let index = this._cache.unitCategories.findIndex(d => d.id == unitCategoryId);
            this.log.debug(`${LOG_PREFIX} Deleted Unit Category record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Unit Category record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Unit Category record from the Local Cache`);
              this._cache.unitCategories.splice(index, 1);

              // Create an up to date copy of the Unit Categories records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
              const copy = Object.assign({}, this._cache).unitCategories;

              // Broadcast the up to date copy of the Unit Categories records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
              this._unitCategoriesSubject$.next(copy);

              // Broadcast the up to date copy of the Unit Categories records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Unit Category record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Unit Category record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Unit Category record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Unit Categories records Local Cache Update was unsuccessful" });
            }
          } else {

            // Unit Category record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Unit Category record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Unit Category record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit Category record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Unit Category record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Unit Category record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Unit Category record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Unit Category record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._unitCategoriesSubject$.value;
  }
}
