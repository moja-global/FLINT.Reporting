import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, of, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, first, takeUntil, tap } from 'rxjs/operators';
import { AccountabilityRule } from '../models/accountability-rule.model';
import { environment } from 'environments/environment';

const LOG_PREFIX: string = "[Accountabilities Rules Data Service]";
const API_PREFIX: string = "api/v1/accountability_rules";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class AccountabilitiesRulesDataService {

  // The local data cache
  private _cache: { accountabilitiesRules: AccountabilityRule[] } = { accountabilitiesRules: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _accountabilitiesRulesSubject$ = new BehaviorSubject<AccountabilityRule[]>([]);
  readonly accountabilitiesRules$ = this._accountabilitiesRulesSubject$.asObservable();

  // The observable that we will use to opt out of initialization subscriptions 
  // once we are done with them
  private _done$ = new Subject<boolean>();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("accountabilities-rules-data-channel");

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

          this.getAllAccountabilitiesRules()
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
   * Creates and adds an instance of a new Accountability Rule Record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param accountabilityRule The details of the Accountability Rule Record to be created - with the id and version details missing
   */
  public createAccountabilityRule(accountabilityRule: AccountabilityRule): Observable<AccountabilityRule> {

    this.log.trace(`${LOG_PREFIX} Entering createAccountabilityRule()`);
    this.log.debug(`${LOG_PREFIX} Accountability Rule = ${JSON.stringify(accountabilityRule)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<AccountabilityRule>(`${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX}`, JSON.stringify(accountabilityRule), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: AccountabilityRule) => {

          // Accountability Rule Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Accountability Rule Record = ${JSON.stringify(data)}`);

          // Add the newly created Accountability Rule Record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Accountability Rule Record to the Local Cache`);
          this._cache.accountabilitiesRules.push(data);

          // Create an up to date copy of the Accountabilities Rules records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Rules records`);
          const copy = Object.assign({}, this._cache).accountabilitiesRules;

          // Broadcast the up to date copy of the Accountabilities Rules records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Rules records to the current listener`);
          this._accountabilitiesRulesSubject$.next(copy);

          // Broadcast the up to date copy of the Accountabilities Rules records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Rules records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Accountability Rule Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability Rule Record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountability Rule Record Creation was successful" });

        }),

        catchError((error: any) => {

          // Accountability Rule Record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountability Rule Record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountability Rule Record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability Rule Record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountability Rule Record Creation was unsuccessful" });

          return throwError(error);
        }));
  }



  /**
   * Creates and adds new instances of Accountability Rules Records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param accountabilityRules The details of the Accountabilities Rules Records to be created - with the id and version details missing
   */
  public createAccountabilityRules(accountabilityRules: AccountabilityRule[]): Observable<AccountabilityRule[]> {

    this.log.trace(`${LOG_PREFIX} Entering createAccountabilityRules()`);
    this.log.debug(`${LOG_PREFIX} Accountability Rules = ${JSON.stringify(accountabilityRules)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX}/all to create the records`);

    return this.http.post<AccountabilityRule[]>(`${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX}/all`, JSON.stringify(accountabilityRules), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: AccountabilityRule[]) => {

          // Accountability Rules Records Creation was successful
          this.log.trace(`${LOG_PREFIX} Records Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Accountability Rules Records = ${JSON.stringify(data)}`);

          // Add the newly created Accountability Rules Records to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Accountabilities Rules Record to the Local Cache`);
          data.forEach((c: AccountabilityRule) => {
            this._cache.accountabilitiesRules.push(c);
          });

          // Create an up to date copy of the Accountabilities Rules records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Rules records`);
          const copy = Object.assign({}, this._cache).accountabilitiesRules;

          // Broadcast the up to date copy of the Accountabilities Rules records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Rules records to the current listener`);
          this._accountabilitiesRulesSubject$.next(copy);

          // Broadcast the up to date copy of the Accountabilities Rules records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Rules records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Accountability Rule Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability Rule Record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountability Rule Record Creation was successful" });

        }),

        catchError((error: any) => {

          // Accountability Rules Records Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountability Rules Records Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountability Rules Records Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability Rules Records Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountability Rules Records Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._accountabilitiesRulesSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Accountabilities Rules records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllAccountabilitiesRules(filters?: any): Observable<AccountabilityRule[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllAccountabilitiesRules()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<AccountabilityRule[]>(`${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: AccountabilityRule[]) => {

          // Accountabilities Rules records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Accountabilities Rules records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Accountabilities Rules records = ${JSON.stringify(data)}`);

          // Update the Accountabilities Rules records in the Local Cache to the newly pulled Accountabilities Rules records
          this.log.trace(`${LOG_PREFIX} Updating the Accountabilities Rules records in the Local Cache to the newly pulled Accountabilities Rules records`);
          this._cache.accountabilitiesRules = data;

          // Create an up to date copy of the Accountabilities Rules records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Rules records`);
          const copy = Object.assign({}, this._cache).accountabilitiesRules;

          // Broadcast the up to date copy of the Accountabilities Rules records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Rules records to the current listener`);
          this._accountabilitiesRulesSubject$.next(copy);

          // Broadcast the up to date copy of the Accountabilities Rules records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Rules records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Accountabilities Rules records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountabilities Rules records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountabilities Rules records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Accountabilities Rules records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountabilities Rules records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountabilities Rules records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountabilities Rules records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountabilities Rules records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Accountability Rule Record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param accountabilityRule The details of the Accountability Rule Record to be updated
   */
  updateAccountabilityRule(accountabilityRule: AccountabilityRule): Observable<AccountabilityRule> {

    this.log.trace(`${LOG_PREFIX} Entering updateAccountabilityRule()`);
    this.log.debug(`${LOG_PREFIX} Accountability Rule = ${JSON.stringify(accountabilityRule)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<AccountabilityRule>(`${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX}`, JSON.stringify(accountabilityRule), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: AccountabilityRule) => {

          // Accountability Rule Record Update was successful
          this.log.trace(`${LOG_PREFIX} Accountability Rule Record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Accountability Rule Record = ${JSON.stringify(data)}`);

          // Search for the locally stored Accountability Rule Record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Accountability Rule Record`);
          let index = this._cache.accountabilitiesRules.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Accountability Rule Record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Accountability Rule Record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Accountability Rule Record`);
            this._cache.accountabilitiesRules[index] = data;

            // Create an up to date copy of the Accountabilities Rules records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Accountabilities Rules records`);
            const copy = Object.assign({}, this._cache).accountabilitiesRules;

            // Broadcast the up to date copy of the Accountabilities Rules records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Rules records to the current listener`);
            this._accountabilitiesRulesSubject$.next(copy);

            // Broadcast the up to date copy of the Accountabilities Rules records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Accountabilities Rules records to the other listeners`);
            this.bc.postMessage({ newValue: copy });

            // Send a message that states that the Accountability Rule Record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability Rule Record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountability Rule Record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Accountability Rule Record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Accountabilities Rules records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Accountability Rule Record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountability Rule Record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountability Rule Record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability Rule Record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountability Rule Record Update was unsuccessful" });

          return throwError(error);

        }));
  }

  deleteAccountabilityRule(accountabilityRuleId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteAccountabilityRule()`);
    this.log.debug(`${LOG_PREFIX} Accountability Rule Id = ${accountabilityRuleId}`);

    // Make a HTTP DELETE Request to delete the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX}/ids/${accountabilityRuleId} to delete the record`);

    return this.http.delete<number>(`${environment.accountabilitiesRulesBaseUrl}/${API_PREFIX}/ids/${accountabilityRuleId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Send a message that states that the Accountability Rule Record Deletion was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability Rule Record Deletion was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Accountability Rule Record Deletion was successful" });

          // Since the Accountability Rule and all its descendants have been deleted, refresh the service to get the up to date Accountabilities Rules
          this.log.trace(`${LOG_PREFIX} Since the Accountability Rule and all its descendants have been deleted, refreshing the service to get the up to date Accountabilities Rules`);

          this.getAllAccountabilitiesRules();

        }),

        catchError((error: any) => {

          // Accountability Rule record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Accountability Rule Record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Accountability Rule Record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Accountability Rule Record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Accountability Rule Record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }  

  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._accountabilitiesRulesSubject$.value;
  }
}
