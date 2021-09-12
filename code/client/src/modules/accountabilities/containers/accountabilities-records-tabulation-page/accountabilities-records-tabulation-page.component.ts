import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConnectivityStatusService } from '@common/services';
import { AccountabilityRule } from '@modules/accountabilities-rules/models/accountability-rule.model';
import { AccountabilitiesRulesDataService } from '@modules/accountabilities-rules/services/accountabilities-rules-data.service';
import { AccountabilitiesRecordsTabulationService } from '@modules/accountabilities/services/accountabilities-records-tabulation.service';
import { PartyType } from '@modules/parties-types/models/party-type.model';
import { PartiesTypesDataService } from '@modules/parties-types/services/parties-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { first } from 'rxjs/internal/operators/first';
import { Subscription } from 'rxjs/internal/Subscription';

const LOG_PREFIX: string = "[Accountabilities Records Tabulation Page]";

@Component({
    selector: 'sb-accountabilities-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-records-tabulation-page.component.html',
    styleUrls: ['accountabilities-records-tabulation-page.component.scss'],
})
export class AccountabilitiesRecordsTabulationPageComponent implements OnInit {

    //Keep tabs on the target accountability rule
    targetAccountabilityRule: AccountabilityRule | undefined;


    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];


    constructor(
        public connectivityStatusService: ConnectivityStatusService,
        public accountabilitiesRecordsTabulationService: AccountabilitiesRecordsTabulationService,
        private partiesTypesDataService: PartiesTypesDataService,
        private accountabilitiesRulesDataService: AccountabilitiesRulesDataService,
        private activatedRoute: ActivatedRoute,
        private log: NGXLogger) { }


    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this.initPartiesTypes(
            () => this.initAccountabilitiesRules(
                () => this.init()
            )
        );

    }


    initPartiesTypes(callback: () => void) {

        // Check if there's a need for Parties Types initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for Parties Types initialization`);
        if (this.partiesTypesDataService.records.length != 0) {

            // The Parties Types have already been initialized
            this.log.trace(`${LOG_PREFIX} The Parties Types have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for Parties Types initialization
            this.log.trace(`${LOG_PREFIX} There's a need for Parties Types initialization`);

            // Initializing the Parties Types
            this.log.trace(`${LOG_PREFIX} Initializing the Parties Types`);
            this.partiesTypesDataService
                .getAllPartiesTypes()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Parties Types initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Parties Types initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
    }


    initAccountabilitiesRules(callback: () => void) {

        // Check if there's a need for accountability rules initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for accountability rules initialization`);
        if (this.accountabilitiesRulesDataService.records.length != 0) {

            // The accountability rules have already been initialized
            this.log.trace(`${LOG_PREFIX} The accountability rules have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for accountability rules initialization
            this.log.trace(`${LOG_PREFIX} There's a need for accountability rules initialization`);

            // Initializing the accountability rules
            this.log.trace(`${LOG_PREFIX} Initializing the accountability rules`);
            this.accountabilitiesRulesDataService
                .getAllAccountabilitiesRules()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Accountability Rules initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Accountability Rules initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
    }

    init() {

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {

                // Get the accountability rule id from the path parameters
                this.log.trace(`${LOG_PREFIX} Getting the accountability rule id from the path parameters`);
                const temp: string | null = this.activatedRoute.snapshot.paramMap.get("accountabilityRuleId")
                const id: number | null = (temp == null ? null : parseInt(temp));
                this.log.debug(`${LOG_PREFIX} Accountability Rule Id = ${id}`);

                // Use the accountability rule id to get the accountability record
                this.log.trace(`${LOG_PREFIX} Using the accountability rule id to get the accountability record`);
                if (id) {

                    this.targetAccountabilityRule = this.accountabilitiesRulesDataService.records.find(c => c.id == id);

                    if (this.targetAccountabilityRule) {

                        // Append subsidiary party name
                        let partyType: PartyType | undefined = this.partiesTypesDataService.records.find((p: PartyType) => p.id == this.targetAccountabilityRule?.subsidiaryPartyTypeId);

                        // Set the name in the accountability type
                        if (partyType) {
                            this.targetAccountabilityRule.subsidiaryPartyTypeName = partyType.name;
                        } else {
                            this.targetAccountabilityRule.subsidiaryPartyTypeName = "";
                        }
                    }

                    this.log.debug(`${LOG_PREFIX} Target Accountability Rule = ${JSON.stringify(this.targetAccountabilityRule)}`);
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
