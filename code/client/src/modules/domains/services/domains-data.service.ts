import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Domain } from '../models/domain.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { MessageService } from '@common/services';
import { catchError, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Domains Data Service]";
const API_PREFIX: string = "api/v1/domains";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class DomainsDataService {

  private _baseUrl: string = environment.baseUrl;
  private _cache: { domains: Domain[] } = { domains: [] };
  private _subject$ = new BehaviorSubject<Domain[]>([]);

  readonly domains$ = this._subject$.asObservable();

  constructor(
    private http: HttpClient,
    private log: NGXLogger,
    private messageService: MessageService) {

  }

  /**
   * Creates and adds an instance of a new Domain Record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param domain The details of the Domain Record to be created - with the id and version details missing
   */
  public createDomain(domain: Domain): Observable<Domain> {

    this.log.trace(`${LOG_PREFIX} Entering createDomain()`);
    this.log.debug(`${LOG_PREFIX} Domain = ${JSON.stringify(domain)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<Domain>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(domain), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Domain) => {

          // Domain Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Domain Record = ${JSON.stringify(data)}`);

          // Add the newly created Domain Record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Domain Record to the Local Cache`);
          this._cache.domains.push(data);

          // Push a copy of the newly updated domains records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated domains records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).domains);

          // Send a message that states that the Domain Record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Domain Record Creation was successful" });

        }),

        catchError((error: any) => {

          // Domain Record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Domain Record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Domain Record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Domain Record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Domain Record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param domainId The Unique Identifier of the Domain Record
   */
  getDomain(domainId: number): Observable<Domain> {

    this.log.trace(`${LOG_PREFIX} Entering getDomain()`);
    this.log.debug(`${LOG_PREFIX} Domain Id = ${domainId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${domainId} to retrieve the record`);

    return this.http.get<Domain>(`${this._baseUrl}/${API_PREFIX}/ids/${domainId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Domain) => {

          // Domain Record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Domain Record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Domain Record = ${JSON.stringify(data)}`);

          // Search for the Domain Record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Domain Record in the Local Cache and returning its index`);
          let index = this._cache.domains.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Domain Record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Domain Record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Domain Record was found in the Local Cache`);

            // Update the local Domain Record
            this.log.trace(`${LOG_PREFIX} Updating the local Domain Record`);
            this._cache.domains[index] = data;

          } else {

            // The Domain Record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Domain Record was not found in the Local Cache`);

            // Add the Domain Record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Domain Record to the Local Cache`);
            this._cache.domains.push(data);
          }

          // Push a copy of the newly updated domains records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated domains records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).domains);

          // Send a message that states that the Domain Record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Domain Record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Domain Record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Domain Record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Domain Record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Domain Record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all domains records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllDomains(filters?: any): Observable<Domain[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllDomains()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<Domain[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: Domain[]) => {

          // domains records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} domains records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved domains records = ${JSON.stringify(data)}`);

          // Update the domains records in the Local Cache to the newly pulled domains records
          this.log.trace(`${LOG_PREFIX} Updating the domains records in the Local Cache to the newly pulled domains records`);
          this._cache.domains = data;

          // Push a copy of the newly updated domains records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated domains records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).domains);

          // Send a message that states that the domains records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the domains records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The domains records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // domains records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} domains records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the domains records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the domains records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The domains records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Domain Record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param domain The details of the Domain Record to be updated
   */
  updateDomain(domain: Domain): Observable<Domain> {

    this.log.trace(`${LOG_PREFIX} Entering updateDomain()`);
    this.log.debug(`${LOG_PREFIX} Domain = ${JSON.stringify(domain)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<Domain>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(domain), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: Domain) => {

          // Domain Record Update was successful
          this.log.trace(`${LOG_PREFIX} Domain Record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Domain Record = ${JSON.stringify(data)}`);

          // Search for the locally stored Domain Record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Domain Record`);
          let index = this._cache.domains.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Domain Record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Domain Record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Domain Record`);
            this._cache.domains[index] = data;

            // Push a copy of the newly updated domains records to all Subscribers
            this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated domains records to all Subscribers`);
            this._subject$.next(Object.assign({}, this._cache).domains);

            // Send a message that states that the Domain Record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Domain Record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Domain Record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "domains records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Domain Record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Domain Record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Domain Record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Domain Record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Domain Record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param domainId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteDomain(domainId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteDomain()`);
    this.log.debug(`${LOG_PREFIX} Domain Id = ${domainId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${domainId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${domainId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Domain Record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Domain Record Deletion was successful`);

            // Search for the deleted Domain Record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Domain Record in the Local Cache`);
            let index = this._cache.domains.findIndex(d => d.id == domainId);
            this.log.debug(`${LOG_PREFIX} Deleted Domain Record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Domain Record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Domain Record from the Local Cache`);
              this._cache.domains.splice(index, 1);

              // Push a copy of the newly updated domains records to all Subscribers
              this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated domains records to all Subscribers`);
              this._subject$.next(Object.assign({}, this._cache).domains);

              // Send a message that states that the Domain Record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Domain Record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Domain Record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "domains records Local Cache Update was unsuccessful" });
            }
          } else {

            // Domain Record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Domain Record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Domain Record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Domain Record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Domain Record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Domain Record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Domain Record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Domain Record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Domain Record Deletion was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
   */
  public get records() {
    return this._subject$.value;
  }
}
