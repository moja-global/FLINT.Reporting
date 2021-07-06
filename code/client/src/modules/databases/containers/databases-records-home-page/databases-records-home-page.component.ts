import { ChangeDetectionStrategy, Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Database } from '@modules/databases/models';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Databases Records Home Page]";

@Component({
    selector: 'sb-databases-records-home-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './databases-records-home-page.component.html',
    styleUrls: ['databases-records-home-page.component.scss'],
})
export class DatabasesRecordsHomePageComponent implements OnInit {

    //Keep tabs on the target database
    targetDatabase: Database = new Database();    

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];    

    constructor(
        private activatedRoute: ActivatedRoute,
        private log: NGXLogger) {}

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(
            this.activatedRoute.queryParamMap.subscribe(params => {

                // Get and update the name from the query parameters
                const name: string | null = params.get('name');
                this.targetDatabase.name = name;

            })
        );         

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach(s => s.unsubscribe());        
    }
      
}
