import { ChangeDetectionStrategy, Component, HostListener, Input, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BasePartyTypes } from '@modules/party-types/data/base-party-types';
import { PartyType } from '@modules/party-types/models';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Party Types Records Tabulation Page]";

@Component({
    selector: 'sb-party-types-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './party-types-records-tabulation-page.component.html',
    styleUrls: ['party-types-records-tabulation-page.component.scss'],
})
export class PartyTypesRecordsTabulationPageComponent implements OnInit {

    // Keep tabs on the target part type
    @Input() targetPartyType!: PartyType | undefined;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];    

    constructor( 
        private activatedRoute: ActivatedRoute,
        private log: NGXLogger) {}

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {
              this.targetPartyType = BasePartyTypes.find(p => p.id == params.get('parentPartyTypeId'));
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
