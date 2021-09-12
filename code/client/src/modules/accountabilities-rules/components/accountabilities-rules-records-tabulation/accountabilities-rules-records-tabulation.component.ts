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
import { AccountabilitiesRulesRecordsTabulationService } from '../../services';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountabilitiesRulesRecordsCreationModalComponent, AccountabilitiesRulesRecordsDeletionModalComponent } from '@modules/accountabilities-rules/containers';
import { AccountabilityRule } from '@modules/accountabilities-rules/models/accountability-rule.model';

const LOG_PREFIX: string = "[Accountabilities Rules Records Tabulation Component]";

@Component({
    selector: 'sb-accountabilities-rules-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-rules-records-tabulation.component.html',
    styleUrls: ['accountabilities-rules-records-tabulation.component.scss'],
})
export class AccountabilitiesRulesRecordsTabulationComponent implements OnInit, OnDestroy, AfterViewInit {

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

    //Keep tabs on the accountability type category id
    accountabilityTypeId: number | null | undefined;

    // Keep tabs on the previously selected party types ids
    previouslySelectedPartiesTypesIds: number[] = [];

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public accountabilitiesRulesRecordsTabulationService: AccountabilitiesRulesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private modalService: NgbModal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private log: NGXLogger) {


    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {

                // Get and update the id from the path parameter
                const temp: string | null = params.get('accountabilityTypeId');
                this.accountabilityTypeId = (temp == null ? null : parseInt(temp));
                this.onAccountabilityTypeChange(this.accountabilityTypeId);


            })
        );


        this._subscriptions.push(
            this.accountabilitiesRulesRecordsTabulationService.unpaginatedAccountabilitiesRules$.subscribe(unpaginatedAccountabilitiesRules => {

                // Clear the previous selected party types list
                this.previouslySelectedPartiesTypesIds = [];

                // Set up the updated selected party types list
                unpaginatedAccountabilitiesRules.forEach((c: AccountabilityRule) => {
                    if(c.subsidiaryPartyTypeId){
                        this.previouslySelectedPartiesTypesIds.push(c.subsidiaryPartyTypeId);
                    }
                }) 


            })
        );        
    }



    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.accountabilitiesRulesRecordsTabulationService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.accountabilitiesRulesRecordsTabulationService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                    this.cd.detectChanges();
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.accountabilitiesRulesRecordsTabulationService.loading$.subscribe(
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
        this.accountabilitiesRulesRecordsTabulationService.searchTerm = event;
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
        this.accountabilitiesRulesRecordsTabulationService.sortColumn = column;
        this.accountabilitiesRulesRecordsTabulationService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.accountabilitiesRulesRecordsTabulationService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.accountabilitiesRulesRecordsTabulationService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates accountability type id change events to the table service
     * @param event The newly desired context issue id
     */
    onAccountabilityTypeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Accountability Rule Id to ${event}`);
        this.accountabilitiesRulesRecordsTabulationService.accountabilityTypeId = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates Accountabilities Rules Records Addition Requests to the responsible component
     */
    onAddAccountabilityRule() {
        this.log.trace(`${LOG_PREFIX} Adding a new Accountability Rule Record`);
        this.log.debug(`${LOG_PREFIX} Accountability Rule = ${JSON.stringify(this.accountabilityTypeId)}`);

        const modalRef = this.modalService.open(AccountabilitiesRulesRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.accountabilityTypeId = this.accountabilityTypeId;
        modalRef.componentInstance.previouslySelectedPartiesTypesIds = this.previouslySelectedPartiesTypesIds;
    }

    /**
     * Propagates Accountabilities Rules Records Deletion Requests to the responsible component
     */
    onDeleteAccountabilityRule(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Accountability Rule Record`);
        this.log.debug(`${LOG_PREFIX} Accountability Rule Record Id = ${id}`);
        const modalRef = this.modalService.open(AccountabilitiesRulesRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }


    /**
     * Accountabilities Home Window
     */
    onOpenAccountabilities(accountabilityRule: any) {
        this.log.trace(`${LOG_PREFIX} Opening Accountabilities Home Window`);
        this.log.debug(`${LOG_PREFIX} Accountability Rule = ${JSON.stringify(accountabilityRule)}`);

        this.router.navigate(['/accountabilities', accountabilityRule.id]);

    }  

}
