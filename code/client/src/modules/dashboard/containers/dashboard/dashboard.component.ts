import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, ChangeDetectionStrategy, OnInit, ChangeDetectorRef } from "@angular/core";
import { DatabaseFilter } from "@common/models/database-filter.model";
import { DatabasesDataService } from "@modules/databases/services/databases-data.service";
import { QuantityObservationsRecordsFilterService } from "@modules/quantity-observations/services/quantity-observations-records-filter.service";
import { environment } from "environments/environment";
import FileSaver from "file-saver";
import { NGXLogger } from "ngx-logger";
import { BehaviorSubject, Observable, Subscription } from "rxjs";
import { map } from "rxjs/internal/operators/map";


const LOG_PREFIX: string = "[Dashboard Component]";
const API_PREFIX: string = "api/v1/crf_tables";
const HEADERS = { 'Content-Type': 'application/json' };

const httpOptions = {
    headers: new HttpHeaders({
    'Content-Type': 'application/json'
    }),
    observe: 'response' as 'body',
    responseType: 'blob' as 'blob'
};


@Component({
    selector: 'sb-dashboard',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './dashboard.component.html',
    styleUrls: ['dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {  

      // The base url of the server
  private _baseUrl: string = environment.baseUrl; 

    private _subject$ = new BehaviorSubject<number>(Date.now());
    readonly time$ = this._subject$.asObservable();

    hasProcessedDatabase: boolean = false;

    databaseFilter!: DatabaseFilter;

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private databasesDataService: DatabasesDataService,
        private quantityObservationsRecordsFilterService: QuantityObservationsRecordsFilterService,
        private cd: ChangeDetectorRef,
        private http: HttpClient,
        private log: NGXLogger) { 

        setInterval(() => {
            this._subject$.next(Date.now());
        }, 1);

        this._subscriptions.push(
            this.databasesDataService.databases$
                .subscribe(
                    databases => {

                        // The update could have been an an addition or a removal
                        // So start by assuming that there are no proceesed databases
                        this.hasProcessedDatabase = false;


                        // Loop through the databases and check for a least one processed database
                        // If found, set the processed databases flag to true
                        for (let database of databases) {
                            if (database.processed) {
                                this.hasProcessedDatabase = true;
                                break;
                            }
                        }
                    },
                    error => {

                        // Assume the worst and set processed databases to false
                        this.log.error('Could not load databases');
                        this.hasProcessedDatabase = false;
                    }
                ));
    }

    ngOnInit() { }

    ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    onDatabaseFilterChange(databaseFilter: DatabaseFilter) {
        this.databaseFilter = databaseFilter;
        this.quantityObservationsRecordsFilterService.filterQuantityObservations(databaseFilter);
        this.cd.detectChanges();
    }


    // See: https://stackoverflow.com/questions/49169806/download-file-in-angular4-using-file-saver
    downloadCRFTable(){
        return this.http.get(`${this._baseUrl}/${API_PREFIX}/partyId/48/databaseId/${this.databaseFilter.databaseId}/from/${this.databaseFilter.startYear}/to/${this.databaseFilter.endYear}`,{responseType: 'blob'})
        .subscribe( data => {
            const file: Blob = new Blob([data], { type: 'application/vnd.ms-excel' });
            FileSaver.saveAs(file, 'crf_table.xlsx');
          }
        ); 
    }    



}
