import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Emissions Types Records Home Page]";

@Component({
    selector: 'sb-emissions-types-records-home-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './emissions-types-records-home-page.component.html',
    styleUrls: ['emissions-types-records-home-page.component.scss'],
})
export class EmissionsTypesRecordsHomePageComponent implements OnInit {

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];    
    
    constructor(
        private log: NGXLogger) {}

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);      

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach(s => s.unsubscribe());        
    }

  
}
