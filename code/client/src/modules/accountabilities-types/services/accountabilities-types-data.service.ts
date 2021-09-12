import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { AccountabilityType } from '../models/accountability-type.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Accountabilities Types Data Service]";
const API_PREFIX: string = "api/v1/accountability_types";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class AccountabilitiesTypesDataService {

  // The local data cache
  private _cache: { accountabilitiesTypes: AccountabilityType[] } = { accountabilitiesTypes: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _accountabilitiesTypesSubject$ = new BehaviorSubject<AccountabilityType[]>([]);
  readonly accountabilitiesTypes$ = this._accountabilitiesTypesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("accountabilities-types-data-channel");

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

          this.getAllAccountabilitiesTypes()
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
   * Creates and adds an instance of a new AccountabilityType record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param accountabilityType The details of the AccountabilityType record to be created - with the id and version details missing
   */
  public createAccountabilityType(accountabilityType: AccountabilityType): Observable<AccountabilityType> {

    this.log.trace(`${LOG_PREFIX} Entering createAccountabilityType()`);
    this.log.debug(`${LOG_PREFIX} AccountabilityType = ${JSON.stringify(accountabilityType)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.accountabilitiesTypesBaseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<AccountabilityType>(`${environment.accountabilitiesTypesBaseUrl}/${API_PREFIX}`, JSON.stringify(accountabilityType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: AccountabilityType) => {

          // AccountabilityType record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created AccountabilityType record = ${JSON.stringify(data)}`);

          // Add the newly created AccountabilityType record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created AccountabilityType record to the Local Cache`);
          this._cache.accountabilitiesTypes.push(data);

          // Create an up to date copy of the Accountabilities Types Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Types Records`);
          const copy = Object.assign({}, this._cache).accountabilitiesTypes;

          // Broadcast the up to date copy of the Accountabilities Types Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Types Records to the current listener`);
          this._accountabilitiesTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Accountabilities Types Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Types Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the AccountabilityType record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the AccountabilityType record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The AccountabilityType record Creation was successful" });

        }),

        catchError((error: any) => {

          // AccountabilityType record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} AccountabilityType record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the AccountabilityType record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the AccountabilityType record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The AccountabilityType record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._accountabilitiesTypesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Accountabilities Types Records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllAccountabilitiesTypes(filters?: any): Observable<AccountabilityType[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllAccountabilitiesTypes()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.accountabilitiesTypesBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<AccountabilityType[]>(`${environment.accountabilitiesTypesBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: AccountabilityType[]) => {

          // Accountabilities Types Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Accountabilities Types Records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Accountabilities Types Records = ${JSON.stringify(data)}`);

          // Update the Accountabilities Types Records in the Local Cache to the newly pulled Accountabilities Types Records
          this.log.trace(`${LOG_PREFIX} Updating the Accountabilities Types Records in the Local Cache to the newly pulled Accountabilities Types Records`);
          this._cache.accountabilitiesTypes = data;

          // Create an up to date copy of the Accountabilities Types Records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Types Records`);
          const copy = Object.assign({}, this._cache).accountabilitiesTypes;

          // Broadcast the up to date copy of the Accountabilities Types Records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Types Records to the current listener`);
          this._accountabilitiesTypesSubject$.next(copy);

          // Broadcast the up to date copy of the Accountabilities Types Records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Types Records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Accountabilities Types Records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountabilities Types Records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountabilities Types Records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Accountabilities Types Records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountabilities Types Records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountabilities Types Records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountabilities Types Records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountabilities Types Records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single AccountabilityType record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param accountabilityType The details of the AccountabilityType record to be updated
   */
  updateAccountabilityType(accountabilityType: AccountabilityType): Observable<AccountabilityType> {

    this.log.trace(`${LOG_PREFIX} Entering updateAccountabilityType()`);
    this.log.debug(`${LOG_PREFIX} AccountabilityType = ${JSON.stringify(accountabilityType)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.accountabilitiesTypesBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<AccountabilityType>(`${environment.accountabilitiesTypesBaseUrl}/${API_PREFIX}`, JSON.stringify(accountabilityType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: AccountabilityType) => {

          // AccountabilityType record Update was successful
          this.log.trace(`${LOG_PREFIX} AccountabilityType record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated AccountabilityType record = ${JSON.stringify(data)}`);

          // Search for the locally stored AccountabilityType record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored AccountabilityType record`);
          let index = this._cache.accountabilitiesTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated AccountabilityType record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local AccountabilityType record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored AccountabilityType record`);
            this._cache.accountabilitiesTypes[index] = data;

            // Create an up to date copy of the Accountabilities Types Records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Types Records`);
            const copy = Object.assign({}, this._cache).accountabilitiesTypes;

            // Broadcast the up to date copy of the Accountabilities Types Records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Types Records to the current listener`);
            this._accountabilitiesTypesSubject$.next(copy);

            // Broadcast the up to date copy of the Accountabilities Types Records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Types Records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the AccountabilityType record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the AccountabilityType record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The AccountabilityType record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: AccountabilityType record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Accountabilities Types Records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // AccountabilityType record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} AccountabilityType record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the AccountabilityType record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the AccountabilityType record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The AccountabilityType record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single AccountabilityType record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param accountabilityTypeId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteAccountabilityType(accountabilityTypeId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteAccountabilityType()`);
    this.log.debug(`${LOG_PREFIX} AccountabilityType Id = ${accountabilityTypeId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.accountabilitiesTypesBaseUrl}/${API_PREFIX}/ids/${accountabilityTypeId} to delete the record`);

    return this.http.delete<number>(`${environment.accountabilitiesTypesBaseUrl}/${API_PREFIX}/ids/${accountabilityTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // AccountabilityType record Deletion was successful
            this.log.trace(`${LOG_PREFIX} AccountabilityType record Deletion was successful`);

            // Search for the deleted AccountabilityType record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted AccountabilityType record in the Local Cache`);
            let index = this._cache.accountabilitiesTypes.findIndex(d => d.id == accountabilityTypeId);
            this.log.debug(`${LOG_PREFIX} Deleted AccountabilityType record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted AccountabilityType record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted AccountabilityType record from the Local Cache`);
              this._cache.accountabilitiesTypes.splice(index, 1);

              // Create an up to date copy of the Accountabilities Types Records
              this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Types Records`);
              const copy = Object.assign({}, this._cache).accountabilitiesTypes;

              // Broadcast the up to date copy of the Accountabilities Types Records to the current listener
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Types Records to the current listener`);
              this._accountabilitiesTypesSubject$.next(copy);

              // Broadcast the up to date copy of the Accountabilities Types Records to the other listeners
              this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Types Records to the other listeners`);
              this.bc.postMessage({ newValue: copy });

              // Send a message that states that the AccountabilityType record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the AccountabilityType record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The AccountabilityType record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: AccountabilityType record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Accountabilities Types Records Local Cache Update was unsuccessful" });
            }
          } else {

            // AccountabilityType record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} AccountabilityType record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the AccountabilityType record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the AccountabilityType record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The AccountabilityType record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // AccountabilityType record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} AccountabilityType record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the AccountabilityType record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the AccountabilityType record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The AccountabilityType record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._accountabilitiesTypesSubject$.value;
  }
}
