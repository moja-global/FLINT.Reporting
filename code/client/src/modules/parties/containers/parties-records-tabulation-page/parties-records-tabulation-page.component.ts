import { ChangeDetectionStrategy, Component, HostListener, Input, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Party } from '@modules/parties/models';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Parties Records Tabulation Page]";

@Component({
    selector: 'sb-parties-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './parties-records-tabulation-page.component.html',
    styleUrls: ['parties-records-tabulation-page.component.scss'],
})
export class PartiesRecordsTabulationPageComponent implements OnInit {

    // Keep tabs on the target part type
    @Input() targetPartyType: Party = new Party();

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
                this.targetPartyType.name = name;

            })
        );         

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {

                // Get and update the id from the path parameter
                const id: string | null = params.get('partyTypeId');
                this.targetPartyType.id = (id == null? null : parseInt(id));

            })
        );

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach((s) => s.unsubscribe());        
    }
      
}
