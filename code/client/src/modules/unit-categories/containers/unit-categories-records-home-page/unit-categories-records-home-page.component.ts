import { ChangeDetectionStrategy, Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UnitCategory } from '@modules/unit-categories/models';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Unit Categories Records Home Page]";

@Component({
    selector: 'sb-unit-categories-records-home-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './unit-categories-records-home-page.component.html',
    styleUrls: ['unit-categories-records-home-page.component.scss'],
})
export class UnitCategoriesRecordsHomePageComponent implements OnInit {

    //Keep tabs on the target unit category
    targetUnitCategory: UnitCategory = new UnitCategory();    

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
                this.targetUnitCategory.name = name;

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
