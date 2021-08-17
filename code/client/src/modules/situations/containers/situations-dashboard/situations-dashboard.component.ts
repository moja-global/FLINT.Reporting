import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs/internal/Subscription';

const LOG_PREFIX: string = "[Situations Records Tabulation Page]";

@Component({
    selector: 'sb-situations-dashboard',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './situations-dashboard.component.html',
    styleUrls: ['situations-dashboard.component.scss'],
})
export class SituationsDashboardComponent implements OnInit {

    page: string = "data";


    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];    

    constructor(
        public connectivityStatusService: ConnectivityStatusService, 
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
