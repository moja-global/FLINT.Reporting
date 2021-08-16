import { HttpClient } from "@angular/common/http";
import { Component, ChangeDetectionStrategy, OnInit } from "@angular/core";
import { DatabaseFilter } from "@common/models/database-filter.model";
import { ConfigService } from "@common/services/config.service";
import { DatabaseFilterService } from "@common/services/database-filter.service";
import { DatabasesDataService } from "@modules/databases/services/databases-data.service";
import { environment } from "environments/environment";
import FileSaver from "file-saver";
import { NGXLogger } from "ngx-logger";
import { BehaviorSubject, Subscription } from "rxjs";


const API_PREFIX: string = "api/v1/crf_tables";



@Component({
    selector: 'sb-dashboard',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './dashboard.component.html',
    styleUrls: ['dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {

    // The base url of the server
    private _baseUrl: string = environment.baseUrl;

    // Keep tabs on the current date
    // Use it to update the date that is displayed on the dashboard
    private _subject$ = new BehaviorSubject<number>(Date.now());
    readonly time$ = this._subject$.asObservable();

    // Keep tabs on whether the system has a processed database.
    // Use this to determine the UI component that is displayed
    hasProcessedDatabase: boolean = false;

    // Set the default selected graph
    graph: string = "total";

    // Set the default selected table
    table: string = "forestLand"


    // Keep tabs on the criteria by which the current displayed database has been filtered
    databaseFilter!: DatabaseFilter;

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private databasesDataService: DatabasesDataService,
        public databaseFilterService: DatabaseFilterService,
        public configService: ConfigService,
        private http: HttpClient,
        private log: NGXLogger) {

        setInterval(() => {
            this._subject$.next(Date.now());
        }, 1);

        this._subscriptions.push(
            this.databasesDataService.databases$
                .subscribe(
                    success => {

                        // The update could have been an an addition or a removal
                        // So start by assuming that there are no proceesed databases
                        this.hasProcessedDatabase = false;


                        // Loop through the databases and check for a least one processed database
                        // If found, set the processed databases flag to true
                        for (let database of success) {
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


    // See: https://stackoverflow.com/questions/49169806/download-file-in-angular4-using-file-saver
    downloadCRFTable() {
        return this.http.get(`${this._baseUrl}/${API_PREFIX}/partyId/48/databaseId/${this.databaseFilterService.filter.databaseId}/from/${this.databaseFilterService.filter.startYear}/to/${this.databaseFilterService.filter.endYear}`, { responseType: 'blob' })
            .subscribe(data => {
                const file: Blob = new Blob([data], { type: 'application/vnd.ms-excel' });
                FileSaver.saveAs(file, 'crf_table.xlsx');
            }
            );
    }



}
