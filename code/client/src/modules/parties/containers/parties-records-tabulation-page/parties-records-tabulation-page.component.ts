import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PartyType } from '@modules/parties-types/models/party-type.model';
import { PartiesTypesDataService } from '@modules/parties-types/services/parties-types-data.service';
import { PartiesRecordsTabulationService } from '@modules/parties/services/parties-records-tabulation.service';
import { NGXLogger } from 'ngx-logger';
import { first } from 'rxjs/internal/operators/first';
import { Subscription } from 'rxjs/internal/Subscription';

const LOG_PREFIX: string = "[Parties Records Tabulation Page]";

@Component({
    selector: 'sb-parties-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './parties-records-tabulation-page.component.html',
    styleUrls: ['parties-records-tabulation-page.component.scss'],
})
export class PartiesRecordsTabulationPageComponent implements OnInit {

    //Keep tabs on the target party type
    targetPartyType: PartyType | undefined;    

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];  


    constructor(
        private partiesTypesDataService: PartiesTypesDataService,
        public partiesRecordsTabulationService: PartiesRecordsTabulationService,
        private activatedRoute: ActivatedRoute,
        private log: NGXLogger) { }

        ngOnInit() {

            this.log.trace(`${LOG_PREFIX} Initializing Component`);
     
    
            this.initPartiesTypes(
                () => this.init()
            );
    
    
        }


        initPartiesTypes(callback: () => void) {

            // Check if there's a need for party types initialization
            this.log.trace(`${LOG_PREFIX} Checking if there's a need for party types initialization`);
            if (this.partiesTypesDataService.records.length != 0) {
    
                // The party types have already been initialized
                this.log.trace(`${LOG_PREFIX} The party types have already been initialized`);
    
                // Transfer control to the callback() method
                callback();
    
            } else {
    
                // There's a need for party types initialization
                this.log.trace(`${LOG_PREFIX} There's a need for party types initialization`);
    
                // Initializing the party types
                this.log.trace(`${LOG_PREFIX} Initializing the party types`);
                this.partiesTypesDataService
                    .getAllPartiesTypes()
                    .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                    .subscribe((response => {
    
                        // Party Types initialization successfully completed
                        this.log.trace(`${LOG_PREFIX} Party Types initialization successfully completed`);
    
                        // Transfer control to the callback() method
                        callback();
    
                    }));
    
            }
        }    
    
        init() {
            this._subscriptions.push(
                this.activatedRoute.paramMap.subscribe(params => {
    
                    // Get the party type id from the path parameters
                    this.log.trace(`${LOG_PREFIX} Getting the party type id from the path parameters`);
                    const temp: string | null = this.activatedRoute.snapshot.paramMap.get("partyTypeId")
                    const id: number | null = (temp == null ? null : parseInt(temp));
                    this.log.debug(`${LOG_PREFIX} Party Type Id = ${id}`);
    
                    // Use the party type id to get the party record
                    this.log.trace(`${LOG_PREFIX} Using the party type id to get the party record`);
                    if (id) {
                        this.targetPartyType = this.partiesTypesDataService.records.find(c => c.id == id);
                        this.log.debug(`${LOG_PREFIX} Target Party Type = ${JSON.stringify(this.targetPartyType)}`);
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
