import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    HostListener,
    Input,
    OnDestroy,
    OnInit,
    ViewChild,
} from '@angular/core';
import { LoadingAnimationComponent, PaginationComponent } from '@common/components';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { SortEvent } from '@common/directives/sortable.directive';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountabilitiesRecordsTabulationService } from '../../services';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountabilitiesRecordsCreationModalComponent, AccountabilitiesRecordsDeletionModalComponent } from '@modules/accountabilities/containers';
import { PartiesTypesDataService } from '@modules/parties-types/services';
import { first } from 'rxjs/operators';
import { AccountabilitiesRulesDataService } from '@modules/accountabilities-rules/services/accountabilities-rules-data.service';
import { PartyType } from '@modules/parties-types/models';
import { AccountabilityRule } from '@modules/accountabilities-rules/models/accountability-rule.model';
import { Accountability } from '@modules/accountabilities/models/accountability.model';

const LOG_PREFIX: string = "[Accountabilities Records Tabulation Component]";

@Component({
    selector: 'sb-accountabilities-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-records-tabulation.component.html',
    styleUrls: ['accountabilities-records-tabulation.component.scss'],
})
export class AccountabilitiesRecordsTabulationComponent implements OnInit, OnDestroy, AfterViewInit {

    // Inject a reference to the loading animation component. 
    // This will provide a way of informing it of the status of 
    // the processing events happening in the background.
    @ViewChild(LoadingAnimationComponent) animation!: LoadingAnimationComponent;

    // Inject a reference to the pagination component.
    // This will provide a way for setting the initial page / page size settings, 
    // and thereafter provide a way for updating the changes in record totals following record filtering.
    @ViewChild(PaginationComponent) pagination!: PaginationComponent;

    // Instantiate and avail a page size variable to the parent component.
    // This will allow the parent component to set the desired page size i.e. maximum number of records per page.
    // This could differ depending on where the table is displayed:
    // For example, a few of the table records can be displayed on the dashboard and 
    // a full set of the table records can be displayed on the table's home page.
    @Input() pageSize: number = 4;

    // Keep tabs on the column that the records are currently sorted by.
    sortedColumn!: string;

    // Keep tabs on the direction that the records are currently sorted by: ascending or descending.
    sortedDirection!: string;

    //Keep tabs on the accountability rule id
    accountabilityRuleId: number | null | undefined;

    //Keep tabs on the parent party type
    parentPartyType: PartyType | null | undefined;

    //Keep tabs on the subsidiary party type
    subsidiaryPartyType: PartyType | null | undefined;

    // Keep tabs on the previously selected party types ids
    previouslySelectedPartiesIds: number[] = [];    

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public accountabilitiesRecordsTabulationService: AccountabilitiesRecordsTabulationService,
        private accountabilitiesRulesDataService: AccountabilitiesRulesDataService,
        private partiesTypesDataService: PartiesTypesDataService,
        private cd: ChangeDetectorRef,
        private modalService: NgbModal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private log: NGXLogger) {


    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this.initPartiesTypes(
            () => this.initAccountabilitiesRules(
                () => this.init()));
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

        // Check if there's a need for Accountabilities Rules initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for Accountabilities Rules initialization`);
        if (this.accountabilitiesRulesDataService.records.length != 0) {

            // The Accountabilities Rules have already been initialized
            this.log.trace(`${LOG_PREFIX} The Accountabilities Rules have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for Accountabilities Rules initialization
            this.log.trace(`${LOG_PREFIX} There's a need for Accountabilities Rules initialization`);

            // Initializing the Accountabilities Rules
            this.log.trace(`${LOG_PREFIX} Initializing the Accountabilities Rules`);
            this.accountabilitiesRulesDataService
                .getAllAccountabilitiesRules()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Accountabilities Rules initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Accountabilities Rules initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
    }

    init() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {

                // Get the accountability rule id from the path parameter
                this.log.trace(`${LOG_PREFIX} Get the accountability rule id from the path parameter`);
                const temp: string | null = params.get('accountabilityRuleId');
                this.accountabilityRuleId = (temp == null ? null : parseInt(temp));
                this.log.trace(`${LOG_PREFIX} Accountability Rule id = ${this.accountabilityRuleId}`);

                // Update the table service's accountability rule value
                this.log.trace(`${LOG_PREFIX} Updating the table service's accountability rule value`);
                this.onAccountabilityRuleChange(this.accountabilityRuleId);

                if (this.accountabilityRuleId) {

                    // Get the Accountability Rule Record corresponding to the Accountability Rule id
                    this.log.trace(`${LOG_PREFIX} Getting the Accountability Rule Record corresponding to the Accountability Rule id`);
                    const accountabilityRule: AccountabilityRule | undefined = this.accountabilitiesRulesDataService.records.find((a: AccountabilityRule) => a.id == this.accountabilityRuleId);
                    this.log.trace(`${LOG_PREFIX} Accountability Rule = ${JSON.stringify(accountabilityRule)}`);

                    if (accountabilityRule) {

                        if (accountabilityRule.parentPartyTypeId) {

                            // Get the Parent Party Type Record
                            this.log.trace(`${LOG_PREFIX} Getting the Parent Party Type Record`);
                            this.parentPartyType = this.partiesTypesDataService.records.find((p: PartyType) => p.id == accountabilityRule.parentPartyTypeId);
                            this.log.trace(`${LOG_PREFIX} Parent Party Type = ${JSON.stringify(this.parentPartyType)}`);

                        }

                        if (accountabilityRule.subsidiaryPartyTypeId) {

                            // Get the Subsidiary Party Type Record
                            this.log.trace(`${LOG_PREFIX} Getting the Subsidiary Party Type Record`);
                            this.subsidiaryPartyType = this.partiesTypesDataService.records.find((p: PartyType) => p.id == accountabilityRule.subsidiaryPartyTypeId);
                            this.log.trace(`${LOG_PREFIX} Subsidiary Party Type = ${JSON.stringify(this.subsidiaryPartyType)}`);
                            
                        }                        
                    }
                }



            })
        );

        this._subscriptions.push(
            this.accountabilitiesRecordsTabulationService.accountabilityRuleAccountabilities$.subscribe(accountabilityRuleAccountabilities => {

                // Clear the previous selected parties list
                this.previouslySelectedPartiesIds = [];

                // Set up the updated selected parties list
                accountabilityRuleAccountabilities.forEach((c: Accountability) => {
                    if(c.subsidiaryPartyId){
                        this.previouslySelectedPartiesIds.push(c.subsidiaryPartyId);
                    }
                }) 


            })
        );        
    }



    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.accountabilitiesRecordsTabulationService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.accountabilitiesRecordsTabulationService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                    this.cd.detectChanges();
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.accountabilitiesRecordsTabulationService.loading$.subscribe(
                (loading) => {
                    this.animation.loading = loading;
                    this.cd.detectChanges();
                }));

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach(s => s.unsubscribe());
    }

    /**
     * Propagates search events to the table service
     * @param event The term to search by
     */
    onSearch(event: any) {
        this.log.trace(`${LOG_PREFIX} Searching for ${event}`);
        this.accountabilitiesRecordsTabulationService.searchTerm = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates sort events to the table service
     * @param param0 The column / direction to sort by
     */
    onSort({ column, direction }: SortEvent) {
        this.log.trace(`${LOG_PREFIX} Sorting ${column} in ${direction} order`);
        this.sortedColumn = column;
        this.sortedDirection = direction;
        this.accountabilitiesRecordsTabulationService.sortColumn = column;
        this.accountabilitiesRecordsTabulationService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.accountabilitiesRecordsTabulationService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.accountabilitiesRecordsTabulationService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates Accountability Rule id change events to the table service
     * @param event The newly desired Accountability Rule id
     */
    onAccountabilityRuleChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Accountability Rule Id to ${event}`);
        this.accountabilitiesRecordsTabulationService.accountabilityRuleId = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates Accountabilities records Addition Requests to the responsible component
     */
    onAddAccountability() {
        this.log.trace(`${LOG_PREFIX} Adding a new Accountability Record`);
        this.log.debug(`${LOG_PREFIX} Accountability Rule Id = ${this.accountabilityRuleId}`);
        const modalRef = this.modalService.open(AccountabilitiesRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.accountabilityRuleId = this.accountabilityRuleId;
        modalRef.componentInstance.previouslySelectedPartiesIds = this.previouslySelectedPartiesIds;
    }

    /**
     * Propagates Accountabilities records Deletion Requests to the responsible component
     */
    onDeleteAccountability(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Accountability Record`);
        this.log.debug(`${LOG_PREFIX} Accountability Record Id = ${id}`);
        this.log.debug(`${LOG_PREFIX} Accountability = ${JSON.stringify(this.accountabilityRuleId)}`);
        const modalRef = this.modalService.open(AccountabilitiesRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }

}
