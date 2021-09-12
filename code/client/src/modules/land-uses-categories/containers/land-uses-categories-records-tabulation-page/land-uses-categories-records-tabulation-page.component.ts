import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConnectivityStatusService } from '@common/services';
import { LandUsesCategoriesRecordsTabulationService } from '@modules/land-uses-categories/services/land-uses-categories-records-tabulation.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs/internal/Subscription';

const LOG_PREFIX: string = "[Land Uses Categories Records Tabulation Page]";

@Component({
    selector: 'sb-land-uses-categories-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './land-uses-categories-records-tabulation-page.component.html',
    styleUrls: ['land-uses-categories-records-tabulation-page.component.scss'],
})
export class LandUsesCategoriesRecordsTabulationPageComponent implements OnInit {

    // Keep tabs on the page that should be displayed
    page: string = "";

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];  


    constructor(
        public connectivityStatusService: ConnectivityStatusService, 
        public landUsesCategoriesRecordsTabulationService: LandUsesCategoriesRecordsTabulationService,
        private activatedRoute: ActivatedRoute,
        private log: NGXLogger) { }

        ngOnInit() {

            this.log.trace(`${LOG_PREFIX} Initializing Component`);
     
    
            this._subscriptions.push(
                this.activatedRoute.paramMap.subscribe(params => {
    
                    // Get and update the id from the path parameter
                    const temp: string | null = params.get('reportingFrameworkId');
                    const id  = (temp == null? null : parseInt(temp));

                    if(id == 1){
                        this.page = "bau";
                    } else if(id == 2){
                        this.page = "emergency";
                    } else {
                        this.page = "";
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
