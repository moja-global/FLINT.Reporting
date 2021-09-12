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
import { PartiesTypesRecordsTabulationService } from '../../services';

const LOG_PREFIX: string = "[Parties Types Records Selection Component]";

@Component({
    selector: 'sb-parties-types-records-selection',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './parties-types-records-selection.component.html',
    styleUrls: ['parties-types-records-selection.component.scss'],
})
export class PartiesTypesRecordsSelectionComponent implements OnInit, OnDestroy, AfterViewInit {

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

    // Instantiate and avail a previously selected Party Types Ids arrays to the parent component.
    // This will allow the parent component to specify the previously selected Party Types Ids.
    // This might then be used to disable the previously selected Party Types Ids if the next configuration is set to true
    @Input() previouslySelectedPartiesTypesIds: number[] = [];       

    // Instantiate and avail a 'skip previously selected Party Types Ids' flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be skipped. 
    @Input() skipPreviouslySelectedPartiesTypesIds:boolean = true;    

    // Propogates Radio Buttons Selection Events to the Parent Component
    @Output() select: EventEmitter<number> = new EventEmitter<number>();

    // Propogates Checkboxes Check Events to the Parent Component
    @Output() check: EventEmitter<number> = new EventEmitter<number>();

    // Propogates Checkboxes Uncheck Events to the Parent Component
    @Output() uncheck: EventEmitter<number> = new EventEmitter<number>();

    // Instantiate a selected PartyType Ids.
    selectedPartyTypeIds: number[] = [];    

    // Keep tabs on the column that the records are currently sorted by.
    sortedColumn!: string;

    // Keep tabs on the direction that the records are currently sorted by: ascending or descending.
    sortedDirection!: string;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public partiesTypesTableService: PartiesTypesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private log: NGXLogger) {

    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
        this.log.debug(`${LOG_PREFIX} Pre-selected Parties Types = ${JSON.stringify(this.selectedPartyTypeIds)}`);

    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.partiesTypesTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.partiesTypesTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.partiesTypesTableService.loading$.subscribe(
                (loading) => {
                    this.animation.loading = loading;
                    this.cd.detectChanges();
                }));

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear any previous search term
        this.partiesTypesTableService.searchTerm = "";

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
        this.partiesTypesTableService.searchTerm = event;
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
        this.partiesTypesTableService.sortColumn = column;
        this.partiesTypesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.partiesTypesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.partiesTypesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /** 
    * Propagates PartyType Selection Event to the Parent Component
    * @param id The id of the Selected PartyType
    */
    onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected PartyType Id: ${id}`);

        // Update the Selected PartyType Ids array
        this.selectedPartyTypeIds = [id];

        // Push the Selected PartyType Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates PartiesTypes Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked PartyType
    */
    onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked PartyType Id: ${id}`);

        // Insert the newly Checked PartyType Id into the Selected PartyType Ids array - if its nonexistent
        if (this.selectedPartyTypeIds.indexOf(id) == -1) {
            this.selectedPartyTypeIds.push(id);
        }

        // Notify the Parent Component
        this.check.emit(id);
    }


    /** 
    * Propagates PartiesTypes Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked PartyType
    */
    onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked PartyType Id: ${id}`);

        // Removes the newly Unchecked PartyType Id from the Selected PartyType Ids array - if in existence
        let index: number = this.selectedPartyTypeIds.indexOf(id);
        if (index != -1) {
            this.selectedPartyTypeIds.splice(index, 1);
        }

        // Notify the Parent Component
        this.uncheck.emit(id);
    }


    isSelected(partyTypeId: number) {
        if(this.selectedPartyTypeIds.some(id => id == partyTypeId)){
            return true;
        } else {
            return this.previouslySelectedPartiesTypesIds.some(id => id == partyTypeId);
        }
    }

    isChecked(partyTypeId: number) {
        if(this.selectedPartyTypeIds.some(id => id == partyTypeId)){
            return true;
        } else {
            return this.previouslySelectedPartiesTypesIds.some(id => id == partyTypeId);
        }        
    }

    isPreviouslySelectedOrChecked(partyTypeId: number) {
            return this.previouslySelectedPartiesTypesIds.some(id => id == partyTypeId);
        
    } 

    truncate(value: string, args: any[]): string {
        const limit = args.length > 0 ? parseInt(args[0], 10) : 20;
        const trail = args.length > 1 ? args[1] : '...';
        return value.length > limit ? value.substring(0, limit) + trail : value;
    }
}
