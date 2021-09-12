import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    EventEmitter,
    HostListener,
    Input,
    OnDestroy,
    OnInit,
    Output,
    ViewChild,
} from '@angular/core';
import { LoadingAnimationComponent, PaginationComponent } from '@common/components';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { SortEvent } from '@common/directives/sortable.directive';
import { AccountabilitiesDataService, AccountabilitiesRecordsTabulationService } from '../../services';

const LOG_PREFIX: string = "[Accountabilities Records Selection Component]";

@Component({
    selector: 'sb-accountabilities-records-selection',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-records-selection.component.html',
    styleUrls: ['accountabilities-records-selection.component.scss'],
})
export class AccountabilitiesRecordsSelectionComponent implements OnInit, OnDestroy, AfterViewInit {

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

    // Instantiate and avail a selection mode flag to the parent component.
    // This will allow the parent component to specify whether single-selection or multi-selection mode is required.
    // Single and Multi Selection Mode will then be executed through Radio Buttons and Checkboxes respectively.
    @Input() selectionMode: string = "single";

    // Instantiate and avail a previously selected Accountability Ids arrays to the parent component.
    // This will allow the parent component to initialize previously selected Accountability Ids e.g. in the case of an update.
    // This array will contain a single item during single-selection mode and multiple items during multi-select mode
    @Input() previouslySelectedAccountabilitiesIds: number[] = [];    

    // Instantiate and avail a skip id field to the parent component.
    // This will allow the parent component to specify the accountability that should be skipped when listing the selection options
    @Input() skipId: number = -1;       

    // Instantiate and avail a selected accountabilities treatment flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be disabled. 
    @Input() disableSelectedAccountabilityIdsTreatment:boolean = false    

    // Propogates Radio Buttons Selection Events to the Parent Component
    @Output() select: EventEmitter<number> = new EventEmitter<number>();

    // Propogates Checkboxes Check Events to the Parent Component
    @Output() check: EventEmitter<number> = new EventEmitter<number>();

    // Propogates Checkboxes Uncheck Events to the Parent Component
    @Output() uncheck: EventEmitter<number> = new EventEmitter<number>();

    // Instantiate a selected Accountability Ids.
    selectedAccountabilityIds: number[] = [];    

    // Keep tabs on the column that the records are currently sorted by.
    sortedColumn!: string;

    // Keep tabs on the direction that the records are currently sorted by: ascending or descending.
    sortedDirection!: string;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public accountabilitiesDataService: AccountabilitiesDataService,
        public accountabilitiesTableService: AccountabilitiesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private log: NGXLogger) {

    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
        this.log.debug(`${LOG_PREFIX} Pre-selected Accountabilities = ${JSON.stringify(this.selectedAccountabilityIds)}`);

    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.accountabilitiesTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.accountabilitiesTableService.accountabilityRuleAccountabilitiesTotal$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.accountabilitiesTableService.loading$.subscribe(
                (loading) => {
                    this.animation.loading = loading;
                    this.cd.detectChanges();
                }));

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear any previous search term
        this.accountabilitiesTableService.searchTerm = "";

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
        this.accountabilitiesTableService.searchTerm = event;
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
        this.accountabilitiesTableService.sortColumn = column;
        this.accountabilitiesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.accountabilitiesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.accountabilitiesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /** 
    * Propagates Accountability Selection Event to the Parent Component
    * @param id The id of the Selected Accountability
    */
    onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected Accountability Id: ${id}`);

        // Update the Selected Accountability Ids array
        this.selectedAccountabilityIds = [id];

        // Push the Selected Accountability Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates Accountabilities Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked Accountability
    */
    onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked Accountability Id: ${id}`);

        // Insert the newly Checked Accountability Id into the Selected Accountability Ids array - if its nonexistent
        if (this.selectedAccountabilityIds.indexOf(id) == -1) {
            this.selectedAccountabilityIds.push(id);
        }

        // Notify the Parent Component
        this.check.emit(id);
    }


    /** 
    * Propagates Accountabilities Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked Accountability
    */
    onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked Accountability Id: ${id}`);

        // Removes the newly Unchecked Accountability Id from the Selected Accountability Ids array - if in existence
        let index: number = this.selectedAccountabilityIds.indexOf(id);
        if (index != -1) {
            this.selectedAccountabilityIds.splice(index, 1);
        }

        // Notify the Parent Component
        this.uncheck.emit(id);
    }


    isSelected(accountabilityId: number) {
        if(this.selectedAccountabilityIds.some(id => id == accountabilityId)){
            return true;
        } else {
            return this.previouslySelectedAccountabilitiesIds.some(id => id == accountabilityId);
        }
    }

    isChecked(accountabilityId: number) {
        if(this.selectedAccountabilityIds.some(id => id == accountabilityId)){
            return true;
        } else {
            return this.previouslySelectedAccountabilitiesIds.some(id => id == accountabilityId);
        }        
    }

    isPreviouslySelectedOrChecked(accountabilityId: number) {
            return this.previouslySelectedAccountabilitiesIds.some(id => id == accountabilityId);
        
    } 

    truncate(value: string, args: any[]): string {
        const limit = args.length > 0 ? parseInt(args[0], 10) : 20;
        const trail = args.length > 1 ? args[1] : '...';
        return value.length > limit ? value.substring(0, limit) + trail : value;
    }
}
