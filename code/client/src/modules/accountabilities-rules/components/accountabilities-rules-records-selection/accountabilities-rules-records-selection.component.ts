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
import { AccountabilitiesRulesRecordsTabulationService } from '../../services';

const LOG_PREFIX: string = "[Accountabilities Rules Records Selection Component]";

@Component({
    selector: 'sb-accountabilities-rules-records-selection',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-rules-records-selection.component.html',
    styleUrls: ['accountabilities-rules-records-selection.component.scss'],
})
export class AccountabilitiesRulesRecordsSelectionComponent implements OnInit, OnDestroy, AfterViewInit {

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

    // Instantiate and avail a previously selected Accountabilities Types Ids arrays to the parent component.
    // This will allow the parent component to specify the previously selected Accountabilities Types Ids.
    // This might then be used to disable the previously selected Accountabilities Types Ids if the next configuration is set to true
    @Input() previouslySelectedAccountabilitiesRulesIds: number[] = [];       

    // Instantiate and avail a 'skip previously selected Accountabilities Types Ids' flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be skipped. 
    @Input() skipPreviouslySelectedAccountabilitiesRulesIds:boolean = true;
    
    
    // Instantiate and avail a 'level' field the parent component.
    // This will allow the parent component to specify the administrative level that the current selection maps to. 
    @Input() level:number | undefined;     

    // Propogates Radio Buttons Selection Events to the Parent Component
    @Output() select: EventEmitter<number> = new EventEmitter<number>();

    // Propogates Checkboxes Check Events to the Parent Component
    @Output() check: EventEmitter<number> = new EventEmitter<number>();

    // Propogates Checkboxes Uncheck Events to the Parent Component
    @Output() uncheck: EventEmitter<number> = new EventEmitter<number>();

    // Instantiate a selected AccountabilityType Ids.
    selectedAccountabilityTypeIds: number[] = [];    

    // Keep tabs on the column that the records are currently sorted by.
    sortedColumn!: string;

    // Keep tabs on the direction that the records are currently sorted by: ascending or descending.
    sortedDirection!: string;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public accountabilitiesRulesTableService: AccountabilitiesRulesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private log: NGXLogger) {

    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
        this.log.debug(`${LOG_PREFIX} Pre-selected Accountabilities Rules = ${JSON.stringify(this.selectedAccountabilityTypeIds)}`);

    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.accountabilitiesRulesTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.accountabilitiesRulesTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.accountabilitiesRulesTableService.loading$.subscribe(
                (loading) => {
                    this.animation.loading = loading;
                    this.cd.detectChanges();
                }));

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear any previous search term
        this.accountabilitiesRulesTableService.searchTerm = "";

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
        this.accountabilitiesRulesTableService.searchTerm = event;
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
        this.accountabilitiesRulesTableService.sortColumn = column;
        this.accountabilitiesRulesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.accountabilitiesRulesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.accountabilitiesRulesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /** 
    * Propagates AccountabilityType Selection Event to the Parent Component
    * @param id The id of the Selected AccountabilityType
    */
    onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected AccountabilityType Id: ${id}`);

        // Update the Selected AccountabilityType Ids array
        this.selectedAccountabilityTypeIds = [id];

        // Push the Selected AccountabilityType Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates AccountabilitiesRules Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked AccountabilityType
    */
    onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked AccountabilityType Id: ${id}`);

        // Insert the newly Checked AccountabilityType Id into the Selected AccountabilityType Ids array - if its nonexistent
        if (this.selectedAccountabilityTypeIds.indexOf(id) == -1) {
            this.selectedAccountabilityTypeIds.push(id);
        }

        // Notify the Parent Component
        this.check.emit(id);
    }


    /** 
    * Propagates AccountabilitiesRules Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked AccountabilityType
    */
    onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked AccountabilityType Id: ${id}`);

        // Removes the newly Unchecked AccountabilityType Id from the Selected AccountabilityType Ids array - if in existence
        let index: number = this.selectedAccountabilityTypeIds.indexOf(id);
        if (index != -1) {
            this.selectedAccountabilityTypeIds.splice(index, 1);
        }

        // Notify the Parent Component
        this.uncheck.emit(id);
    }


    isSelected(accountabilityTypeId: number) {
        if(this.selectedAccountabilityTypeIds.some(id => id == accountabilityTypeId)){
            return true;
        } else {
            return this.previouslySelectedAccountabilitiesRulesIds.some(id => id == accountabilityTypeId);
        }
    }

    isChecked(accountabilityTypeId: number) {
        if(this.selectedAccountabilityTypeIds.some(id => id == accountabilityTypeId)){
            return true;
        } else {
            return this.previouslySelectedAccountabilitiesRulesIds.some(id => id == accountabilityTypeId);
        }        
    }

    isPreviouslySelectedOrChecked(accountabilityTypeId: number) {
            return this.previouslySelectedAccountabilitiesRulesIds.some(id => id == accountabilityTypeId);
        
    } 

    truncate(value: string, args: any[]): string {
        const limit = args.length > 0 ? parseInt(args[0], 10) : 20;
        const trail = args.length > 1 ? args[1] : '...';
        return value.length > limit ? value.substring(0, limit) + trail : value;
    }
}
