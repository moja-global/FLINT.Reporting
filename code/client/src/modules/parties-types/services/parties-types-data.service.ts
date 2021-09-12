import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { PartyType } from '../models/party-type.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Parties Types Data Service]";
const API_PREFIX: string = "api/v1/party_types";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class PartiesTypesDataService {

  // The local data cache
  private _cache: { partiesTypes: PartyType[] } = { partiesTypes: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _partiesTypesSubject$ = new BehaviorSubject<PartyType[]>([]);
  readonly partiesTypes$ = this._partiesTypesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("parties-types-data-channel");

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

          this.getAllPartiesTypes()
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
   * Creates and adds an instance of a new PartyType record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param partyType The details of the PartyType record to be created - with the id and version details missing
   */
  public createPartyType(partyType: PartyType): Observable<PartyType> {

    this.log.trace(`${LOG_PREFIX} Entering createPartyType()`);
    this.log.debug(`${LOG_PREFIX} PartyType = ${JSON.stringify(partyType)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.partiesTypesBaseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<PartyType>(`${environment.partiesTypesBaseUrl}/${API_PREFIX}`, JSON.stringify(partyType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: PartyType) => {

          // PartyType record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created PartyType record = ${JSON.stringify(data)}`);

          // Add the newly created PartyType record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created PartyType record to the Local Cache`);
          this._cache.partiesTypes.push(data);

          // Create an up to date copy of the Parties Types Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Parties Types Records`);
          const copy = Object.assign({}, this._cache).partiesTypes;

          // Broadcast the up to date copy of the Parties Types Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties Types Records to the current listener`);
          this._partiesTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Parties Types Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties Types Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the PartyType record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the PartyType record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The PartyType record Creation was successful" });

        }),

        catchError((error: any) => {

          // PartyType record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} PartyType record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the PartyType record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the PartyType record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The PartyType record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._partiesTypesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Parties Types Records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllPartiesTypes(filters?: any): Observable<PartyType[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllPartiesTypes()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.partiesTypesBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<PartyType[]>(`${environment.partiesTypesBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: PartyType[]) => {

          // Parties Types Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Parties Types Records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Parties Types Records = ${JSON.stringify(data)}`);

          // Update the Parties Types Records in the Local Cache to the newly pulled Parties Types Records
          this.log.trace(`${LOG_PREFIX} Updating the Parties Types Records in the Local Cache to the newly pulled Parties Types Records`);
          this._cache.partiesTypes = data;

          // Create an up to date copy of the Parties Types Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Parties Types Records`);
          const copy = Object.assign({}, this._cache).partiesTypes;

          // Broadcast the up to date copy of the Parties Types Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties Types Records to the current listener`);
          this._partiesTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Parties Types Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties Types Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Parties Types Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Parties Types Records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Parties Types Records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Parties Types Records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Parties Types Records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Parties Types Records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Parties Types Records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Parties Types Records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single PartyType record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param partyType The details of the PartyType record to be updated
   */
  updatePartyType(partyType: PartyType): Observable<PartyType> {

    this.log.trace(`${LOG_PREFIX} Entering updatePartyType()`);
    this.log.debug(`${LOG_PREFIX} PartyType = ${JSON.stringify(partyType)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.partiesTypesBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<PartyType>(`${environment.partiesTypesBaseUrl}/${API_PREFIX}`, JSON.stringify(partyType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: PartyType) => {

          // PartyType record Update was successful
          this.log.trace(`${LOG_PREFIX} PartyType record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated PartyType record = ${JSON.stringify(data)}`);

          // Search for the locally stored PartyType record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored PartyType record`);
          let index = this._cache.partiesTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated PartyType record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local PartyType record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored PartyType record`);
            this._cache.partiesTypes[index] = data;

            // Create an up to date copy of the Parties Types Records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Parties Types Records`);
            const copy = Object.assign({}, this._cache).partiesTypes;

            // Broadcast the up to date copy of the Parties Types Records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties Types Records to the current listener`);
            this._partiesTypesSubject$.next(copy);

            // Broadcast the up to date copy of the Parties Types Records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties Types Records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the PartyType record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the PartyType record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The PartyType record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: PartyType record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Parties Types Records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // PartyType record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} PartyType record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the PartyType record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the PartyType record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The PartyType record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single PartyType record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param partyTypeId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deletePartyType(partyTypeId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deletePartyType()`);
    this.log.debug(`${LOG_PREFIX} PartyType Id = ${partyTypeId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.partiesTypesBaseUrl}/${API_PREFIX}/ids/${partyTypeId} to delete the record`);

    return this.http.delete<number>(`${environment.partiesTypesBaseUrl}/${API_PREFIX}/ids/${partyTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // PartyType record Deletion was successful
            this.log.trace(`${LOG_PREFIX} PartyType record Deletion was successful`);

            // Search for the deleted PartyType record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted PartyType record in the Local Cache`);
            let index = this._cache.partiesTypes.findIndex(d => d.id == partyTypeId);
            this.log.debug(`${LOG_PREFIX} Deleted PartyType record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted PartyType record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted PartyType record from the Local Cache`);
              this._cache.partiesTypes.splice(index, 1);

              // Create an up to date copy of the Parties Types Records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Parties Types Records`);
              const copy = Object.assign({}, this._cache).partiesTypes;

              // Broadcast the up to date copy of the Parties Types Records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties Types Records to the current listener`);
              this._partiesTypesSubject$.next(copy);

              // Broadcast the up to date copy of the Parties Types Records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties Types Records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the PartyType record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the PartyType record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The PartyType record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: PartyType record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Parties Types Records Local Cache Update was unsuccessful" });
            }
          } else {

            // PartyType record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} PartyType record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the PartyType record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the PartyType record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The PartyType record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // PartyType record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} PartyType record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the PartyType record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the PartyType record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The PartyType record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._partiesTypesSubject$.value;
  }
}
