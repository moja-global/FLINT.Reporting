import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Database } from '../models/database.model';
import { environment } from 'environments/environment';

const LOG_PREFIX: string = "[Databases Integration Service]";
const API_PREFIX: string = "api/v1/task_manager/databases";
const HEADERS = { 'Content-Type': 'application/json' };


@Injectable({
  providedIn: 'root'
})
export class DatabasesIntegrationService {

  // The base url of the server
  private _baseUrl: string = environment.baseUrl;

  constructor(
    private http: HttpClient,
    private log: NGXLogger) {

  }

  /**
   * Triggers databases integration tasks i.e processing / aggregation
   * 
   * @param databaseId The unique identifier of the Database record to be integrated
   */
  public integrateDatabase(databaseId: number | null | undefined){

    if(databaseId != undefined && databaseId != null) {

      this.log.trace(`${LOG_PREFIX} Entering integrateDatabase()`);
      this.log.debug(`${LOG_PREFIX} Database = ${JSON.stringify(databaseId)}`);
  
      // Make a HTTP POST Request to create the record
      this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX}${databaseId} to integrate database`);
  
      this.http.post(`${this._baseUrl}/${API_PREFIX}/${databaseId}`, "", { headers: new HttpHeaders(HEADERS) }).subscribe(
        response => {
          console.log('Integrating database.')
        },
        error => console.log('Could not integrate database.')
      );
            
    }
      
  }
 

}
