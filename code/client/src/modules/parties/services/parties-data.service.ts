import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';
import { Party } from '../models/party.model';
import { environment } from 'environments/environment';

const LOG_PREFIX: string = "[Parties Data Service]";
const API_PREFIX: string = "api/v1/parties";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class PartiesDataService {

  // The local data cache
  private _cache: { parties: Party[] } = { parties: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _partiesSubject$ = new BehaviorSubject<Party[]>([]);
  readonly parties$ = this._partiesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("parties-data-channel");

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

          this.getAllParties()
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
   * Creates and adds an instance of a new Party Record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param party The details of the Party Record to be created - with the id and version details missing
   */
  public createParty(party: Party): Observable<Party> {

    this.log.trace(`${LOG_PREFIX} Entering createParty()`);
    this.log.debug(`${LOG_PREFIX} Party = ${JSON.stringify(party)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.partiesBaseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<Party>(`${environment.partiesBaseUrl}/${API_PREFIX}`, JSON.stringify(party), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Party) => {

          // Party Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Party Record = ${JSON.stringify(data)}`);

          // Add the newly created Party Record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Party Record to the Local Cache`);
          this._cache.parties.push(data);

          // Create an up to date copy of the Parties records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Parties records`);
          const copy = Object.assign({}, this._cache).parties;

          // Broadcast the up to date copy of the Parties records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties records to the current listener`);
          this._partiesSubject$.next(copy);

          // Broadcast the up to date copy of the Parties records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Party Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Party Record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Party Record Creation was successful" });

        }),

        catchError((error: any) => {

          // Party Record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Party Record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Party Record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Party Record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Party Record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._partiesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Parties records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllParties(filters?: any): Observable<Party[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllParties()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.partiesBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<Party[]>(`${environment.partiesBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: Party[]) => {

          // Parties records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Parties records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Parties records = ${JSON.stringify(data)}`);

          // Update the Parties records in the Local Cache to the newly pulled Parties records
          this.log.trace(`${LOG_PREFIX} Updating the Parties records in the Local Cache to the newly pulled Parties records`);
          this._cache.parties = data;

          // Create an up to date copy of the Parties records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Parties records`);
          const copy = Object.assign({}, this._cache).parties;

          // Broadcast the up to date copy of the Parties records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties records to the current listener`);
          this._partiesSubject$.next(copy);

          // Broadcast the up to date copy of the Parties records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Parties records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Parties records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Parties records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Parties records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Parties records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Parties records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Parties records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Parties records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Party Record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param party The details of the Party Record to be updated
   */
  updateParty(party: Party): Observable<Party> {

    this.log.trace(`${LOG_PREFIX} Entering updateParty()`);
    this.log.debug(`${LOG_PREFIX} Party = ${JSON.stringify(party)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.partiesBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<Party>(`${environment.partiesBaseUrl}/${API_PREFIX}`, JSON.stringify(party), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Party) => {

          // Party Record Update was successful
          this.log.trace(`${LOG_PREFIX} Party Record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Party Record = ${JSON.stringify(data)}`);

          // Search for the locally stored Party Record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Party Record`);
          let index = this._cache.parties.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Party Record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Party Record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Party Record`);
            this._cache.parties[index] = data;

            // Create an up to date copy of the Parties records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Parties records`);
            const copy = Object.assign({}, this._cache).parties;

            // Broadcast the up to date copy of the Parties records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties records to the current listener`);
            this._partiesSubject$.next(copy);

            // Broadcast the up to date copy of the Parties records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Party Record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Party Record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Party Record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Party Record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Parties records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Party Record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Party Record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Party Record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Party Record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Party Record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Party Record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param partyTypeId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteParty(partyTypeId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteParty()`);
    this.log.debug(`${LOG_PREFIX} Party Id = ${partyTypeId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.partiesBaseUrl}/${API_PREFIX}/ids/${partyTypeId} to delete the record`);

    return this.http.delete<number>(`${environment.partiesBaseUrl}/${API_PREFIX}/ids/${partyTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Party Record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Party Record Deletion was successful`);

            // Search for the deleted Party Record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Party Record in the Local Cache`);
            let index = this._cache.parties.findIndex(d => d.id == partyTypeId);
            this.log.debug(`${LOG_PREFIX} Deleted Party Record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Party Record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Party Record from the Local Cache`);
              this._cache.parties.splice(index, 1);

              // Create an up to date copy of the Parties records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Parties records`);
              const copy = Object.assign({}, this._cache).parties;

              // Broadcast the up to date copy of the Parties records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties records to the current listener`);
              this._partiesSubject$.next(copy);

              // Broadcast the up to date copy of the Parties records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Parties records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the Party Record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Party Record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Party Record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Party Record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Parties records Local Cache Update was unsuccessful" });
            }
          } else {

            // Party Record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Party Record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Party Record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Party Record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Party Record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Party Record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Party Record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Party Record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Party Record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Party Record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._partiesSubject$.value;
  }
}
