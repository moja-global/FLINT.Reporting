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
import { ParentPartiesRecordsTabulationService, PartiesRecordsTabulationService, SubsidiaryPartiesRecordsTabulationService } from '../../services';

const LOG_PREFIX: string = "[Parties Records Selection Component]";

export interface PartySelectionPayload {
    index: number | null | undefined,
    partyId: number | null | undefined
}

@Component({
    selector: 'sb-parties-records-selection',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './parties-records-selection.component.html',
    styleUrls: ['parties-records-selection.component.scss'],
})
export class PartiesRecordsSelectionComponent implements OnInit, OnDestroy, AfterViewInit {

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

    // Instantiate and avail a basis flag to the parent component.
    // This will allow the parent component to specify whether the selection should be based on parent or subsidiary parties or on default settings.
    @Input() basis: string = "default"; // parents, subsidiaries, default

    // Instantiate and avail an index flag to the parent component.
    // This will allow the parent component to specify the index of the records to be listed for selection
    @Input() index: number = -1;    

    // Instantiate and avail a partyTypeId flag to the parent component.
    // This will allow the parent component to specify the type of the previously selected Parties.
    @Input() partyTypeId: number = -1;

    // Instantiate and avail a previously selected Parties arrays to the parent component.
    // This will allow the parent component to specify the previously selected Parties.
    // This might then be used to disable the previously selected Parties if the next configuration is set to true
    @Input() previouslySelectedPartiesIds: number[] = [];

    // Instantiate and avail a 'skip previously selected Parties' flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be skipped. 
    @Input() skipPreviouslySelectedPartiesIds: boolean = true;

    // Propogates Radio Buttons Selection Events to the Parent Component
    @Output() select: EventEmitter<PartySelectionPayload> = new EventEmitter<PartySelectionPayload>();

    // Propogates Checkboxes Check Events to the Parent Component
    @Output() check: EventEmitter<PartySelectionPayload> = new EventEmitter<PartySelectionPayload>();

    // Propogates Checkboxes Uncheck Events to the Parent Component
    @Output() uncheck: EventEmitter<PartySelectionPayload> = new EventEmitter<PartySelectionPayload>();

    // Keep tabs on the column that the records are currently sorted by.
    sortedColumn!: string;

    // Keep tabs on the direction that the records are currently sorted by: ascending or descending.
    sortedDirection!: string;

    // Keep tabs on the selected / desected parties
    selectedPartiesIds: number[] = [];    

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public partiesTableService: PartiesRecordsTabulationService,
        public parentPartiesTableService: ParentPartiesRecordsTabulationService,
        public subsidiaryPartiesTableService: SubsidiaryPartiesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private log: NGXLogger) {

    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        switch (this.basis) {

            case "parents":

                // Update the party type id in the tabulation service tool
                this.log.trace(`${LOG_PREFIX} Updating the party type id in the tabulation service tool`);
                this.parentPartiesTableService.partyTypeId = this.partyTypeId;

                // Update the previously selected parties detail in the tabulation service tool
                this.log.trace(`${LOG_PREFIX} Updating the previously selected parties detail in the tabulation service tool`);
                if (this.skipPreviouslySelectedPartiesIds) {
                    this.parentPartiesTableService.excludedParties = this.previouslySelectedPartiesIds;
                } else {
                    this.parentPartiesTableService.excludedParties = [];
                }
                break;

            case "subsidiaries":

                // Update the party type id in the tabulation service tool
                this.log.trace(`${LOG_PREFIX} Updating the party type id in the tabulation service tool`);
                this.subsidiaryPartiesTableService.partyTypeId = this.partyTypeId;

                // Update the previously selected parties detail in the tabulation service tool
                this.log.trace(`${LOG_PREFIX} Updating the previously selected parties detail in the tabulation service tool`);
                if (this.skipPreviouslySelectedPartiesIds) {
                    this.subsidiaryPartiesTableService.excludedParties = this.previouslySelectedPartiesIds;
                } else {
                    this.subsidiaryPartiesTableService.excludedParties = [];
                }
                break;

            default:

                // Update the party type id in the tabulation service tool
                this.log.trace(`${LOG_PREFIX} Updating the party type id in the tabulation service tool`);
                this.partiesTableService.partyTypeId = this.partyTypeId;

                // Update the previously selected parties detail in the tabulation service tool
                this.log.trace(`${LOG_PREFIX} Updating the previously selected parties detail in the tabulation service tool`);
                if (this.skipPreviouslySelectedPartiesIds) {
                    this.partiesTableService.excludedParties = this.previouslySelectedPartiesIds;
                } else {
                    this.partiesTableService.excludedParties = [];
                }

        }




        this.cd.detectChanges();
    }


    ngAfterViewInit() {


        switch (this.basis) {

            case "parents":

                // Set the initial page and page size values on the pagination component.
                this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
                this.pagination.initialize(this.parentPartiesTableService.page, this.pageSize);

                // Subscribe to the total value changes and propagate them to the pagination component.
                // These values typically change in response to the user filtering the records.
                this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
                this._subscriptions.push(
                    this.parentPartiesTableService.total$.subscribe(
                        (total) => {
                            this.pagination.total = total;
                        }));

                // Subscribe to loading events and propagate them to the loading component.
                // Loading events occur when the user searches, sorts or moves from one record page to another.
                this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
                this._subscriptions.push(
                    this.parentPartiesTableService.loading$.subscribe(
                        (loading) => {
                            this.animation.loading = loading;
                            this.cd.detectChanges();
                        }));
                break;

            case "subsidiaries":

                // Set the initial page and page size values on the pagination component.
                this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
                this.pagination.initialize(this.subsidiaryPartiesTableService.page, this.pageSize);

                // Subscribe to the total value changes and propagate them to the pagination component.
                // These values typically change in response to the user filtering the records.
                this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
                this._subscriptions.push(
                    this.subsidiaryPartiesTableService.total$.subscribe(
                        (total) => {
                            this.pagination.total = total;
                        }));

                // Subscribe to loading events and propagate them to the loading component.
                // Loading events occur when the user searches, sorts or moves from one record page to another.
                this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
                this._subscriptions.push(
                    this.subsidiaryPartiesTableService.loading$.subscribe(
                        (loading) => {
                            this.animation.loading = loading;
                            this.cd.detectChanges();
                        }));
                break;

            default:

                // Set the initial page and page size values on the pagination component.
                this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
                this.pagination.initialize(this.partiesTableService.page, this.pageSize);

                // Subscribe to the total value changes and propagate them to the pagination component.
                // These values typically change in response to the user filtering the records.
                this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
                this._subscriptions.push(
                    this.partiesTableService.total$.subscribe(
                        (total) => {
                            this.pagination.total = total;
                        }));

                // Subscribe to loading events and propagate them to the loading component.
                // Loading events occur when the user searches, sorts or moves from one record page to another.
                this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
                this._subscriptions.push(
                    this.partiesTableService.loading$.subscribe(
                        (loading) => {
                            this.animation.loading = loading;
                            this.cd.detectChanges();
                        }));

        }





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
        this.partiesTableService.searchTerm = event;
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
        this.partiesTableService.sortColumn = column;
        this.partiesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.partiesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.partiesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates party parent id change events to the table service
     * @param event The newly desired party parent id
     */
    onParentIdChange(event: any) {

        this.log.trace(`${LOG_PREFIX} Changing Party Category Id to ${event.target.value}`);

        // Update the local parent id value
        this.partyTypeId = event.target.value;

        // Clear any previously selected party
        if (this.selectedPartiesIds.length > 0) {
            this.onUncheck(this.selectedPartiesIds[0]);
            this.selectedPartiesIds = [];
        }

        this.partiesTableService.partyTypeId = this.partyTypeId;

        this.cd.detectChanges();
    }



    /** 
    * Propagates Party Selection Event to the Parent Component
    * @param id The id of the Selected Party
    */
    onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected Party Id: ${id}`);

        // Update the Selected Parties Ids array
        this.selectedPartiesIds = [id];

        // Push the Selected Party Id to the Parent Component
        this.select.emit({index: 0, partyId: id});
    }


    /** 
    * Propagates Parties Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked Party
    */
     onCheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Checked Party Id: ${id}`);

        // Get the index of the checked party
        // If has already been inserted into the Selected Parties Ids array the index will be -1
        let index: number = this.selectedPartiesIds.indexOf(id);

        // If the party hasn't been inserted into the parties array, insert it and update the index value.
        // Please note that the index value should be a single step up from the last index value.
        // This will avoid reusing the same index values when middle-placed values are deleted from the array
        if (index == -1) {
            index = this.selectedPartiesIds.length > 0 ? ((this.selectedPartiesIds[this.selectedPartiesIds.length - 1]) + 1): 1;
            this.selectedPartiesIds.push(id);
        }

        // Notify the Parent Component
        this.check.emit({ index: index, partyId: id });
    }


    /** 
    * Propagates Parties Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked Party
    */
    onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked Party Id: ${id}`);

        // Get the index of the unchecked party in the Selected Parties Ids array
        // If its non-existent, the index will be -1
        let index: number = this.selectedPartiesIds.indexOf(id);        

        // Remove the party from the array
        if (index != -1) {
            this.selectedPartiesIds.splice(index, 1);
        }

        // Notify the Parent Component
        this.check.emit({ index: index, partyId: id });

    }


    isSelected(partyTypeId: number) {
        return this.selectedPartiesIds.some(id => id == partyTypeId);
    }

    isChecked(partyTypeId: number) {
        return this.selectedPartiesIds.some(id => id == partyTypeId);
    }

    truncate(value: string, args: any[]): string {
        const limit = args.length > 0 ? parseInt(args[0], 10) : 20;
        const trail = args.length > 1 ? args[1] : '...';
        return value.length > limit ? value.substring(0, limit) + trail : value;
    }
}
