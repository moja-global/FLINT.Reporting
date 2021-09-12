import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { PartyType } from '@modules/parties-types/models/party-type.model';
import { PartiesTypesDataService } from '@modules/parties-types/services/parties-types-data.service';
import { Party } from '@modules/parties/models';
import { PartiesDataService } from '@modules/parties/services';
import { Breadcrumb } from '@modules/navigation/models/navigation.model';
import { NavigationService } from '@modules/navigation/services/navigation.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';

const LOG_PREFIX: string = "[Parties Records Home Page]";

@Component({
    selector: 'sb-parties-records-home-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './parties-records-home-page.component.html',
    styleUrls: ['parties-records-home-page.component.scss'],
})
export class PartiesRecordsHomePageComponent implements OnInit {

    //Keep tabs on the target party type
    targetPartyType: PartyType | undefined;

    //Keep tabs on the target party
    targetParty: Party | undefined;    

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private activatedRoute: ActivatedRoute,
        private navigationService: NavigationService,
        private partiesTypesDataService: PartiesTypesDataService,
        private partiesDataService: PartiesDataService,
        private log: NGXLogger) { }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this.initPartiesTypes(
            () => this.init());

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

                // Initialize a new breadcrumb navigation
                this.log.trace(`${LOG_PREFIX} Initializing a new breadcrumb navigation`);
                let breadcrumbs: Breadcrumb[] = [];                

                // Configure the breadcrumb navigation
                this.log.trace(`${LOG_PREFIX} Configuring the breadcrumb navigation`);

                breadcrumbs.push({
                    text: "Administrative Units Types",
                    link: "/parties_types"
                });


                if (this.targetPartyType) {

                    breadcrumbs.push({
                        text: "" + this.targetPartyType.name
                    });

                }

                // Push the updated breadcrumb navigation to the navigation service
                this.log.trace(`${LOG_PREFIX} Pushing the updated breadcrumb navigation to the navigation service`);
                this.navigationService.dynamicBreadcrumbs = breadcrumbs;
                this.log.debug(`${LOG_PREFIX} Breadcrumbs = ${JSON.stringify(breadcrumbs)}`);
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
