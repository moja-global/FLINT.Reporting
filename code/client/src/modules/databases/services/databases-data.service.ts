import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Database } from '../models/database.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';
import { DatabasesIntegrationService } from './databases-integration.service';

const LOG_PREFIX: string = "[Databases Data Service]";
const API_PREFIX: string = "api/v1/databases";
const HEADERS = { 'Content-Type': 'application/json' }; 


@Injectable({
  providedIn: 'root'
})
export class DatabasesDataService {

  // The base url of the server
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { databases: Database[] } = { databases: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of databases records in the data store
  private _databasesSubject$: BehaviorSubject<Array<Database>> = new BehaviorSubject<Array<Database>>([]);
  readonly databases$: Observable<Array<Database>> = this._databasesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("databases-data-channel");

  constructor(
    private http: HttpClient,
    private connectivityStatusService: ConnectivityStatusService,
    private databasesIntegrationService: DatabasesIntegrationService,
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

          this.getAllDatabases()
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
    this.zone.run(() => this._databasesSubject$.next(event.data.newValue));
  }
    

  /**
   * Creates and adds an instance of a new Database record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param database The details of the Database record to be created - with the id and version details missing
   */
  public createDatabase(database: Database): Observable<Database> {

    this.log.trace(`${LOG_PREFIX} Entering createDatabase()`);
    this.log.debug(`${LOG_PREFIX} Database = ${JSON.stringify(database)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<Database>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(database), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Database) => {

          // Database record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Database record = ${JSON.stringify(data)}`);

          // Start the database integration task in the background
          this.databasesIntegrationService.integrateDatabase(data.id);

          // Add the newly created Database record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Database record to the Local Cache`);
          this._cache.databases.push(data);

          // Create an up to date copy of the Databases records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Databases records`);
          const copy = Object.assign({}, this._cache).databases;

          // Broadcast the up to date copy of the Databases records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the current listener`);
          this._databasesSubject$.next(copy);

          // Broadcast the up to date copy of the Databases records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Database record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Database record Creation was successful" });

        }),

        catchError((error: any) => {

          // Database record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Database record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Database record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Database record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Database record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param databaseId The Unique Identifier of the Database record
   */
  getDatabase(databaseId: number): Observable<Database> {

    this.log.trace(`${LOG_PREFIX} Entering getDatabase()`);
    this.log.debug(`${LOG_PREFIX} Database Id = ${databaseId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${databaseId} to retrieve the record`);

    return this.http.get<Database>(`${this._baseUrl}/${API_PREFIX}/ids/${databaseId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Database) => {

          // Database record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Database record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Database record = ${JSON.stringify(data)}`);

          // Search for the Database record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Database record in the Local Cache and returning its index`);
          let index = this._cache.databases.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Database record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Database record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Database record was found in the Local Cache`);

            // Update the local Database record
            this.log.trace(`${LOG_PREFIX} Updating the local Database record`);
            this._cache.databases[index] = data;

          } else {

            // The Database record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Database record was not found in the Local Cache`);

            // Add the Database record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Database record to the Local Cache`);
            this._cache.databases.push(data);
          }

          // Create an up to date copy of the Databases records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Databases records`);
          const copy = Object.assign({}, this._cache).databases;

          // Broadcast the up to date copy of the Databases records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the current listener`);
          this._databasesSubject$.next(copy);

          // Broadcast the up to date copy of the Databases records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Database record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Database record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Database record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Database record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Database record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Database record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Databases records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllDatabases(filters?: any): Observable<Database[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllDatabases()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<Database[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: Database[]) => {

          // Databases records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Databases records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Databases records = ${JSON.stringify(data)}`);

          // Update the Databases records in the Local Cache to the newly pulled Databases records
          this.log.trace(`${LOG_PREFIX} Updating the Databases records in the Local Cache to the newly pulled Databases records`);
          this._cache.databases = data;

          // Create an up to date copy of the Databases records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Databases records`);
          const copy = Object.assign({}, this._cache).databases;

          // Broadcast the up to date copy of the Databases records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the current listener`);
          this._databasesSubject$.next(copy);

          // Broadcast the up to date copy of the Databases records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Databases records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Databases records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Databases records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Databases records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Databases records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Databases records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Databases records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Databases records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Database record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param database The details of the Database record to be updated
   */
  updateDatabase(database: Database): Observable<Database> {

    this.log.trace(`${LOG_PREFIX} Entering updateDatabase()`);
    this.log.debug(`${LOG_PREFIX} Database = ${JSON.stringify(database)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<Database>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(database), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Database) => {

          // Database record Update was successful
          this.log.trace(`${LOG_PREFIX} Database record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Database record = ${JSON.stringify(data)}`);

          // Search for the locally stored Database record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Database record`);
          let index = this._cache.databases.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Database record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Database record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Database record`);
            this._cache.databases[index] = data;

            // Create an up to date copy of the Databases records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Databases records`);
            const copy = Object.assign({}, this._cache).databases;

            // Broadcast the up to date copy of the Databases records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the current listener`);
            this._databasesSubject$.next(copy);

            // Broadcast the up to date copy of the Databases records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Database record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Database record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Database record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Databases records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Database record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Database record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Database record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Database record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Database record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param databaseId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteDatabase(databaseId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteDatabase()`);
    this.log.debug(`${LOG_PREFIX} Database Id = ${databaseId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${databaseId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${databaseId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Database record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Database record Deletion was successful`);

            // Search for the deleted Database record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Database record in the Local Cache`);
            let index = this._cache.databases.findIndex(d => d.id == databaseId);
            this.log.debug(`${LOG_PREFIX} Deleted Database record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Database record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Database record from the Local Cache`);
              this._cache.databases.splice(index, 1);

              // Create an up to date copy of the Databases records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Databases records`);
              const copy = Object.assign({}, this._cache).databases;

              // Broadcast the up to date copy of the Databases records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the current listener`);
              this._databasesSubject$.next(copy);

              // Broadcast the up to date copy of the Databases records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Databases records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Database record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Database record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Database record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Databases records Local Cache Update was unsuccessful" });
            }
          } else {

            // Database record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Database record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Database record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Database record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Database record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Database record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Database record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Database record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Database record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._databasesSubject$.value;
  }
}
