import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { EmissionType } from '../models/emission-type.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Emissions Types Data Service]";
const API_PREFIX: string = "api/v1/emission_types";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class EmissionsTypesDataService {

  // The local data cache
  private _cache: { emissionsTypes: EmissionType[] } = { emissionsTypes: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _emissionsTypesSubject$ = new BehaviorSubject<EmissionType[]>([]);
  readonly emissionsTypes$ = this._emissionsTypesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("emissions-types-data-channel");

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

          this.getAllEmissionsTypes()
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
   * Creates and adds an instance of a new Emission Type record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param emissionType The details of the Emission Type record to be created - with the id and version details missing
   */
  public createEmissionType(emissionType: EmissionType): Observable<EmissionType> {

    this.log.trace(`${LOG_PREFIX} Entering createEmissionType()`);
    this.log.debug(`${LOG_PREFIX} Emission Type = ${JSON.stringify(emissionType)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.emissionsTypesBaseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<EmissionType>(`${environment.emissionsTypesBaseUrl}/${API_PREFIX}`, JSON.stringify(emissionType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: EmissionType) => {

          // Emission Type record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Emission Type record = ${JSON.stringify(data)}`);

          // Add the newly created Emission Type record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Emission Type record to the Local Cache`);
          this._cache.emissionsTypes.push(data);

          // Create an up to date copy of the Emissions Types Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Emissions Types Records`);
          const copy = Object.assign({}, this._cache).emissionsTypes;

          // Broadcast the up to date copy of the Emissions Types Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Emissions Types Records to the current listener`);
          this._emissionsTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Emissions Types Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Emissions Types Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Emission Type record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emission Type record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Emission Type record Creation was successful" });

        }),

        catchError((error: any) => {

          // Emission Type record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Emission Type record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Emission Type record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emission Type record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Emission Type record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._emissionsTypesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Emissions Types Records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllEmissionsTypes(filters?: any): Observable<EmissionType[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllEmissionsTypes()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.emissionsTypesBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<EmissionType[]>(`${environment.emissionsTypesBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: EmissionType[]) => {

          // Emissions Types Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Emissions Types Records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Emissions Types Records = ${JSON.stringify(data)}`);

          // Update the Emissions Types Records in the Local Cache to the newly pulled Emissions Types Records
          this.log.trace(`${LOG_PREFIX} Updating the Emissions Types Records in the Local Cache to the newly pulled Emissions Types Records`);
          this._cache.emissionsTypes = data;

          // Create an up to date copy of the Emissions Types Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Emissions Types Records`);
          const copy = Object.assign({}, this._cache).emissionsTypes;

          // Broadcast the up to date copy of the Emissions Types Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Emissions Types Records to the current listener`);
          this._emissionsTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Emissions Types Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Emissions Types Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Emissions Types Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emissions Types Records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Emissions Types Records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Emissions Types Records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Emissions Types Records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Emissions Types Records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emissions Types Records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Emissions Types Records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Emission Type record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param emissionType The details of the Emission Type record to be updated
   */
  updateEmissionType(emissionType: EmissionType): Observable<EmissionType> {

    this.log.trace(`${LOG_PREFIX} Entering updateEmissionType()`);
    this.log.debug(`${LOG_PREFIX} Emission Type = ${JSON.stringify(emissionType)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.emissionsTypesBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<EmissionType>(`${environment.emissionsTypesBaseUrl}/${API_PREFIX}`, JSON.stringify(emissionType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: EmissionType) => {

          // Emission Type record Update was successful
          this.log.trace(`${LOG_PREFIX} Emission Type record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Emission Type record = ${JSON.stringify(data)}`);

          // Search for the locally stored Emission Type record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Emission Type record`);
          let index = this._cache.emissionsTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Emission Type record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Emission Type record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Emission Type record`);
            this._cache.emissionsTypes[index] = data;

            // Create an up to date copy of the Emissions Types Records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Emissions Types Records`);
            const copy = Object.assign({}, this._cache).emissionsTypes;

            // Broadcast the up to date copy of the Emissions Types Records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Emissions Types Records to the current listener`);
            this._emissionsTypesSubject$.next(copy);

            // Broadcast the up to date copy of the Emissions Types Records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Emissions Types Records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Emission Type record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emission Type record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Emission Type record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Emission Type record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Emissions Types Records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Emission Type record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Emission Type record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Emission Type record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emission Type record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Emission Type record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Emission Type record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param emissionTypeId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteEmissionType(emissionTypeId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteEmissionType()`);
    this.log.debug(`${LOG_PREFIX} Emission Type Id = ${emissionTypeId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.emissionsTypesBaseUrl}/${API_PREFIX}/ids/${emissionTypeId} to delete the record`);

    return this.http.delete<number>(`${environment.emissionsTypesBaseUrl}/${API_PREFIX}/ids/${emissionTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Emission Type record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Emission Type record Deletion was successful`);

            // Search for the deleted Emission Type record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Emission Type record in the Local Cache`);
            let index = this._cache.emissionsTypes.findIndex(d => d.id == emissionTypeId);
            this.log.debug(`${LOG_PREFIX} Deleted Emission Type record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Emission Type record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Emission Type record from the Local Cache`);
              this._cache.emissionsTypes.splice(index, 1);

              // Create an up to date copy of the Emissions Types Records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Emissions Types Records`);
              const copy = Object.assign({}, this._cache).emissionsTypes;

              // Broadcast the up to date copy of the Emissions Types Records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Emissions Types Records to the current listener`);
              this._emissionsTypesSubject$.next(copy);

              // Broadcast the up to date copy of the Emissions Types Records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Emissions Types Records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Emission Type record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emission Type record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Emission Type record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Emission Type record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Emissions Types Records Local Cache Update was unsuccessful" });
            }
          } else {

            // Emission Type record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Emission Type record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Emission Type record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emission Type record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Emission Type record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Emission Type record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Emission Type record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Emission Type record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Emission Type record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Emission Type record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._emissionsTypesSubject$.value;
  }
}
