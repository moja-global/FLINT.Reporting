import { ChangeDetectionStrategy, Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs/internal/Subscription';

const LOG_PREFIX: string = "[Actors Records Tabulation Page]";

@Component({
    selector: 'sb-actors-dashboard',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './actors-dashboard.component.html',
    styleUrls: ['actors-dashboard.component.scss'],
})
export class ActorsDashboardComponent implements OnInit {

    page: string = "data";
    formId: number = -1;


    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];    

    constructor(
        public connectivityStatusService: ConnectivityStatusService, 
        private activatedRoute: ActivatedRoute,
        private log: NGXLogger) {}


    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);       

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {

                // Get the id of the opened data form
                this.log.trace(`${LOG_PREFIX} Getting the id of the opened data form`);
                const id: string | null = params.get('formId');
                this.log.debug(`${LOG_PREFIX} Data Form Id = ${id}`);

                if(id != null) {
                    this.formId = parseInt(id);
                }

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
