import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { CoverType } from '../models/cover-type.model';
import { environment } from 'environments/environment';
import { MessageType } from '@common/models/message.type.model';
import { MessageService } from '@common/services';
import { catchError, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Cover Types Data Service]";
const API_PREFIX: string = "api/v1/cover_types";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class CoverTypesDataService {

  private _baseUrl: string = environment.baseUrl;
  private _cache: { coverTypes: CoverType[] } = { coverTypes: [] };
  private _subject$ = new BehaviorSubject<CoverType[]>([]);

  readonly coverTypes$ = this._subject$.asObservable();

  constructor(
    private http: HttpClient,
    private log: NGXLogger,
    private messageService: MessageService) {

  }

  /**
   * Creates and adds an instance of a new Cover Type record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param coverType The details of the Cover Type record to be created - with the id and version details missing
   */
  public createCoverType(coverType: CoverType): Observable<CoverType> {

    this.log.trace(`${LOG_PREFIX} Entering createCoverType()`);
    this.log.debug(`${LOG_PREFIX} CoverType = ${JSON.stringify(coverType)}`);

    // Make a HTTP POST Request to create the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);

    return this.http.post<CoverType>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(coverType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: CoverType) => {

          // Cover Type record Creation was successful
          this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
          this.log.debug(`${LOG_PREFIX} Created Cover Type record = ${JSON.stringify(data)}`);

          // Add the newly created Cover Type record to the Local Cache
          this.log.trace(`${LOG_PREFIX} Adding the newly created Cover Type record to the Local Cache`);
          this._cache.coverTypes.push(data);

          // Push a copy of the newly updated Cover Types records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Cover Types records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).coverTypes);

          // Send a message that states that the Cover Type record Creation was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Creation was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Type record Creation was successful" });

        }),

        catchError((error: any) => {

          // Cover Type record Creation was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Type record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Type record Creation was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Creation was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Creation was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds a single Cover Type record to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param coverTypeId The Unique Identifier of the Cover Type record
   */
  getCoverType(coverTypeId: number): Observable<CoverType> {

    this.log.trace(`${LOG_PREFIX} Entering getCoverType()`);
    this.log.debug(`${LOG_PREFIX} CoverType Id = ${coverTypeId}`);

    // Make a HTTP GET Request to retrieve the record
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${coverTypeId} to retrieve the record`);

    return this.http.get<CoverType>(`${this._baseUrl}/${API_PREFIX}/ids/${coverTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: CoverType) => {

          // Cover Type record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Cover Type record Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Cover Type record = ${JSON.stringify(data)}`);

          // Search for the Cover Type record in the Local Cache and return its index
          this.log.trace(`${LOG_PREFIX} Searching for the Cover Type record in the Local Cache and returning its index`);
          let index = this._cache.coverTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Cover Type record Index = ${index}`);

          // If the record was found (index != -1), update it, else, add it to the Local Storage
          if (index != -1) {

            // The Cover Type record was found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Cover Type record was found in the Local Cache`);

            // Update the local Cover Type record
            this.log.trace(`${LOG_PREFIX} Updating the local Cover Type record`);
            this._cache.coverTypes[index] = data;

          } else {

            // The Cover Type record was not found in the Local Cache
            this.log.trace(`${LOG_PREFIX} The Cover Type record was not found in the Local Cache`);

            // Add the Cover Type record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the Cover Type record to the Local Cache`);
            this._cache.coverTypes.push(data);
          }

          // Push a copy of the newly updated Cover Types records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Cover Types records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).coverTypes);

          // Send a message that states that the Cover Type record Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Type record Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Cover Type record Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Type record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Type record Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Retrieves and adds all or a subset of all Cover Types records to the local cache and then broadcasts the changes to all subscribers
   * 
   * @param filters Optional query parameters used in filtering the retrieved records
   */
  getAllCoverTypes(filters?: any): Observable<CoverType[]> {

    this.log.trace(`${LOG_PREFIX} Entering getAllCoverTypes()`);
    this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);

    // Make a HTTP GET Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);

    return this.http.get<CoverType[]>(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new HttpHeaders(HEADERS), params: filters == null ? {} : filters })
      .pipe(

        tap((data: CoverType[]) => {

          // Cover Types records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Cover Types records Retrieval was successful`);
          this.log.debug(`${LOG_PREFIX} Retrieved Cover Types records = ${JSON.stringify(data)}`);

          // Update the Cover Types records in the Local Cache to the newly pulled Cover Types records
          this.log.trace(`${LOG_PREFIX} Updating the Cover Types records in the Local Cache to the newly pulled Cover Types records`);
          this._cache.coverTypes = data;

          // Push a copy of the newly updated Cover Types records to all Subscribers
          this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Cover Types records to all Subscribers`);
          this._subject$.next(Object.assign({}, this._cache).coverTypes);

          // Send a message that states that the Cover Types records Retrieval was successful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Types records Retrieval was successful`);
          this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Types records Retrieval was successful" });

        }),

        catchError((error: any) => {

          // Cover Types records Retrieval was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Types records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Types records Retrieval was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Types records Retrieval was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Types records Retrieval was unsuccessful" });

          return throwError(error);
        }));
  }


  /**
   * Updates a single Cover Type record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   * 
   * @param coverType The details of the Cover Type record to be updated
   */
  updateCoverType(coverType: CoverType): Observable<CoverType> {

    this.log.trace(`${LOG_PREFIX} Entering updateCoverType()`);
    this.log.debug(`${LOG_PREFIX} CoverType = ${JSON.stringify(coverType)}`);

    // Make a HTTP POST Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);

    return this.http.put<CoverType>(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(coverType), { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((data: CoverType) => {

          // Cover Type record Update was successful
          this.log.trace(`${LOG_PREFIX} Cover Type record Update was successful`);
          this.log.debug(`${LOG_PREFIX} Updated Cover Type record = ${JSON.stringify(data)}`);

          // Search for the locally stored Cover Type record
          this.log.trace(`${LOG_PREFIX} Searching for the locally stored Cover Type record`);
          let index = this._cache.coverTypes.findIndex(d => d.id === data.id);
          this.log.debug(`${LOG_PREFIX} Updated Cover Type record Index = ${index}`);

          // If the record was found (index != -1), update it in the Local Cache
          if (index != -1) {

            // Update the local Cover Type record
            this.log.trace(`${LOG_PREFIX} Updating the locally stored Cover Type record`);
            this._cache.coverTypes[index] = data;

            // Push a copy of the newly updated Cover Types records to all Subscribers
            this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Cover Types records to all Subscribers`);
            this._subject$.next(Object.assign({}, this._cache).coverTypes);

            // Send a message that states that the Cover Type record Update was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Update was successful`);
            this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Type record Update was successful" });

          } else {

            // Local Cache Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Cover Type record is missing in the Local Cache`);

            // Send a message that states that the Local Cache Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "Cover Types records Local Cache Update was unsuccessful" });
          }

        }),

        catchError((error: any) => {

          // Cover Type record Update was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Type record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Type record Update was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Update was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Update was unsuccessful" });

          return throwError(error);

        }));
  }


  /**
   * Deletes a single Cover Type record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
   *
   * @param coverTypeId The Unique Identifier of the record
   * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
   */
  deleteCoverType(coverTypeId: number): Observable<number> {

    this.log.trace(`${LOG_PREFIX} Entering deleteCoverType()`);
    this.log.debug(`${LOG_PREFIX} CoverType Id = ${coverTypeId}`);

    // Make a HTTP DELETE Request to retrieve the records
    this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${coverTypeId} to delete the record`);

    return this.http.delete<number>(`${this._baseUrl}/${API_PREFIX}/ids/${coverTypeId}`, { headers: new HttpHeaders(HEADERS) })
      .pipe(

        tap((count: number) => {

          // Mark the deletion successful if and only if exactly 1 record was deleted
          if (count == 1) {

            // Cover Type record Deletion was successful
            this.log.trace(`${LOG_PREFIX} Cover Type record Deletion was successful`);

            // Search for the deleted Cover Type record in the Local Cache
            this.log.trace(`${LOG_PREFIX} Searching for the deleted Cover Type record in the Local Cache`);
            let index = this._cache.coverTypes.findIndex(d => d.id == coverTypeId);
            this.log.debug(`${LOG_PREFIX} Deleted Cover Type record Index = ${index}`);

            // If the record was found (index != -1), remove it from the Local Cache
            if (index != -1) {

              // Remove the deleted Cover Type record from the Local Cache
              this.log.trace(`${LOG_PREFIX} Removing the deleted Cover Type record from the Local Cache`);
              this._cache.coverTypes.splice(index, 1);

              // Push a copy of the newly updated Cover Types records to all Subscribers
              this.log.trace(`${LOG_PREFIX} Pushing a copy of the newly updated Cover Types records to all Subscribers`);
              this._subject$.next(Object.assign({}, this._cache).coverTypes);

              // Send a message that states that the Cover Type record Deletion was successful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Deletion was successful`);
              this.messageService.sendMessage({ "type": MessageType.Success, "message": "The Cover Type record Deletion was successful" });

            } else {

              // Local Cache Update was unsuccessful
              this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Cover Type record is missing in the Local Cache`);

              // Send a message that states that the Local Cache Update was unsuccessful
              this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
              this.messageService.sendMessage({ "type": MessageType.Error, "message": "Cover Types records Local Cache Update was unsuccessful" });
            }
          } else {

            // Cover Type record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Cover Type record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);

            // Send a message that states that the Cover Type record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Deletion was unsuccessful" });

          }


        }),

        catchError((error: any) => {

          // Cover Type record Deletion was unsuccessful
          this.log.error(`${LOG_PREFIX} Cover Type record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);

          // Send a message that states that the Cover Type record Deletion was unsuccessful
          this.log.trace(`${LOG_PREFIX} Sending a message that states that the Cover Type record Deletion was unsuccessful`);
          this.messageService.sendMessage({ "type": MessageType.Error, "message": "The Cover Type record Deletion was unsuccessful" });

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
