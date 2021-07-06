import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable, of, Subject, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { QuantityObservation } from '../models/quantity-observation.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { catchError, filter, first, map, takeUntil, tap } from 'rxjs/operators';
import { DatabaseFilter } from '@common/models';

const LOG_PREFIX: string = "[Quantity Observations Data Service]";
const API_PREFIX: string = "api/v1/quantity_observations";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class QuantityObservationsDataService {

  // The base url of the server
  private _baseUrl: string = environment.baseUrl;

  // The local data cache
  private _cache: { quantityObservations: QuantityObservation[] } = { quantityObservations: [] };

  // The observables that allow subscribers to keep tabs of the current status 
  // of unit categories records in the data store
  private _quantityObservationsSubject$ = new BehaviorSubject<QuantityObservation[]>([]);
  readonly quantityObservations$ = this._quantityObservationsSubject$.asObservable();

  // The api that we'll use to communicate data store changes when components that 
  // subscribe to this service are outside the current ngzone
  private bc: BroadcastChannel = new BroadcastChannel("quantity-observations-data-channel");

  constructor(
    private http: HttpClient,
    private messageService: MessageService,
    private zone: NgZone,
    private log: NGXLogger) {

    //Note: "bc.onmessage" isn't invoked on sender ui
    this.bc.onmessage = this.zone.run(() => this.handleEvent);
  }

  /**
   * Publish information to current (listening) ui
   * @param event 
   */
  private handleEvent = (event: MessageEvent) => {
    this.zone.run(() => this._quantityObservationsSubject$.next(event.data.newValue));
  }


  /**
   * Retrieves and adds all or a subset of all Quantity Observations records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllQuantityObservations(filters?: any): Observable<QuantityObservation[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllQuantityObservations()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<QuantityObservation[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: QuantityObservation[]) => {

          // Quantity Observations records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Quantity Observations records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Quantity Observations records = ${JSON.stringify(data)}`);

          // Update the Quantity Observations records in the Local Cache to the newly pulled Quantity Observations records
          this.log.trace(`${LOG_PREFIX} Updating the Quantity Observations records in the Local Cache to the newly pulled Quantity Observations records`);
          this._cache.quantityObservations = data;

          // Create an up to date copy of the Quantity Observations records
          this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Quantity Observations records`);
          const copy = Object.assign({}, this._cache).quantityObservations;

          // Broadcast the up to date copy of the Quantity Observations records to the current listener
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Quantity Observations records to the current listener`);
          this._quantityObservationsSubject$.next(copy);

          // Broadcast the up to date copy of the Quantity Observations records to the other listeners
          this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Quantity Observations records to the other listeners`);
          this.bc.postMessage({ newValue: copy });

          // Send a message that states that the Quantity Observations records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Quantity Observations records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Quantity Observations records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Quantity Observations records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Quantity Observations records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Quantity Observations records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Quantity Observations records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Quantity Observations records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._quantityObservationsSubject$.value;
  }
}
