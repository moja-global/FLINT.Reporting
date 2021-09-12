import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountabilityRule } from '@modules/accountabilities-rules/models';
import { AccountabilitiesRulesDataService } from '@modules/accountabilities-rules/services';
import { AccountabilityType } from '@modules/accountabilities-types/models';
import { AccountabilitiesTypesDataService } from '@modules/accountabilities-types/services/accountabilities-types-data.service';
import { Breadcrumb } from '@modules/navigation/models/navigation.model';
import { NavigationService } from '@modules/navigation/services/navigation.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';

const LOG_PREFIX: string = "[Accountabilities Rules Records Home Page]";

@Component({
    selector: 'sb-accountabilities-rules-records-home-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-rules-records-home-page.component.html',
    styleUrls: ['accountabilities-rules-records-home-page.component.scss'],
})
export class AccountabilitiesRulesRecordsHomePageComponent implements OnInit {

    //Keep tabs on the target accountability type
    targetAccountabilityType: AccountabilityType | undefined;       

    //Keep tabs on the target accountability rule
    targetAccountabilityRule: AccountabilityRule | undefined;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private activatedRoute: ActivatedRoute,
        private navigationService: NavigationService,
        private accountabilitiesTypesDataService: AccountabilitiesTypesDataService,
        private accountabilitiesRulesDataService: AccountabilitiesRulesDataService,
        private log: NGXLogger) { }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

            this.initAccountabilitiesTypes(
                () => this.initAccountabilitiesRules(
                    () => this.init()));

    }


    initAccountabilitiesTypes(callback: () => void) {

        // Check if there's a need for accountability types initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for accountability types initialization`);
        if (this.accountabilitiesTypesDataService.records.length != 0) {

            // The accountability types have already been initialized
            this.log.trace(`${LOG_PREFIX} The accountability types have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for accountability types initialization
            this.log.trace(`${LOG_PREFIX} There's a need for accountability types initialization`);

            // Initializing the accountability types
            this.log.trace(`${LOG_PREFIX} Initializing the accountability types`);
            this.accountabilitiesTypesDataService
                .getAllAccountabilitiesTypes()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Context Types initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Context Types initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
    }

    initAccountabilitiesRules(callback: () => void) {

        // Check if there's a need for accountabilities rules initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for accountabilities rules initialization`);
        if (this.accountabilitiesRulesDataService.records.length != 0) {

            // The accountabilities rules have already been initialized
            this.log.trace(`${LOG_PREFIX} The accountabilities rules have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for accountabilities rules initialization
            this.log.trace(`${LOG_PREFIX} There's a need for accountabilities rules initialization`);

            // Initializing the accountabilitiesRules
            this.log.trace(`${LOG_PREFIX} Initializing the accountabilities rules`);
            this.accountabilitiesRulesDataService
                .getAllAccountabilitiesRules()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Accountabilities Rules initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Contexts Issues initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }

    }     

    init() {

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {

                // Get the accountability type id from the path parameters
                this.log.trace(`${LOG_PREFIX} Getting the accountability type id from the path parameters`);
                const temp: string | null = this.activatedRoute.snapshot.paramMap.get("accountabilityTypeId")
                const id: number | null = (temp == null ? null : parseInt(temp));
                this.log.debug(`${LOG_PREFIX} Accountability Type Id = ${id}`);

                // Use the accountability type id to get the accountability type record
                this.log.trace(`${LOG_PREFIX} Using the accountability type id to get the accountability type record`);
                if (id) {
                    this.targetAccountabilityType = this.accountabilitiesTypesDataService.records.find(c => c.id == id);
                    this.log.debug(`${LOG_PREFIX} Target Accountability Type = ${JSON.stringify(this.targetAccountabilityType)}`);
                }
                
                // Initialize a new breadcrumb navigation
                this.log.trace(`${LOG_PREFIX} Initializing a new breadcrumb navigation`);
                let breadcrumbs: Breadcrumb[] = [];                

                // Configure the breadcrumb navigation
                this.log.trace(`${LOG_PREFIX} Configuring the breadcrumb navigation`);

                breadcrumbs.push({
                    text: "Hierarchies Types",
                    link: "/accountabilities_types"
                });


                if (this.targetAccountabilityType) {

                    breadcrumbs.push({
                        text: "" + this.targetAccountabilityType.name
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
