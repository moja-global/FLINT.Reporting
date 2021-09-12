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
import { LandUsesCategoriesRecordsTabulationService } from '../../services';
import { first } from 'rxjs/operators';
import { ReportingFrameworksDataService } from '@modules/reporting-frameworks/services/reporting-frameworks-data.service';

const LOG_PREFIX: string = "[Land Uses Categories Records Selection Component]";

export interface LandUseCategorySelectionPayload {
    reportingFrameworkId: number | null,
    landUseCategoryId: number
}

@Component({
    selector: 'sb-land-uses-categories-records-selection',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './land-uses-categories-records-selection.component.html',
    styleUrls: ['land-uses-categories-records-selection.component.scss'],
})
export class LandUsesCategoriesRecordsSelectionComponent implements OnInit, OnDestroy, AfterViewInit {

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

    // Instantiate and avail a reportingFrameworkId field to the parent component.
    // This will allow the parent component to initialize the previously selected domain
    @Input() reportingFrameworkId: number | null = null;

    // Instantiate and avail a selectedDomainActivitiesIds arrays to the parent component.
    // This will allow the parent component to initialize previously selected domain activities ids
    // This array will contain a single item during single-selection mode and multiple items during multi-select mode
    @Input() selectedDomainActivitiesIds: number[] = [];

    // Propogates Radio Buttons Selection Events to the Parent Component
    @Output() select: EventEmitter<LandUseCategorySelectionPayload> = new EventEmitter<LandUseCategorySelectionPayload>();

    // Propogates Checkboxes Check Events to the Parent Component
    @Output() check: EventEmitter<LandUseCategorySelectionPayload> = new EventEmitter<LandUseCategorySelectionPayload>();

    // Propogates Checkboxes Uncheck Events to the Parent Component
    @Output() uncheck: EventEmitter<LandUseCategorySelectionPayload> = new EventEmitter<LandUseCategorySelectionPayload>();

    // Propogates Reset Events to the Parent Component when all selections get cleared
    @Output() reset: EventEmitter<void> = new EventEmitter<void>();

    // Keep tabs on the column that the records are currently sorted by.
    sortedColumn!: string;

    // Keep tabs on the direction that the records are currently sorted by: ascending or descending.
    sortedDirection!: string;



    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public landUsesCategoriesTableService: LandUsesCategoriesRecordsTabulationService,
        public reportingFrameworksDataService: ReportingFrameworksDataService,
        private cd: ChangeDetectorRef,
        private log: NGXLogger) {

    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this.initReportingFrameworks(
            () => this.init());


    }

    initReportingFrameworks(callback: () => void) {

        // Check if there's a need for domains initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for domains initialization`);
        if (this.reportingFrameworksDataService.records.length != 0) {

            // The domains have already been initialized
            this.log.trace(`${LOG_PREFIX} The domains have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for domains initialization
            this.log.trace(`${LOG_PREFIX} There's a need for domains initialization`);

            // Initializing the domains
            this.log.trace(`${LOG_PREFIX} Initializing the domains`);
            this.reportingFrameworksDataService
                .getAllReportingFrameworks()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Reporting Frameworks initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Reporting Frameworks initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }

    }

    init() {


        // Update the landUseCategory parent id in the tabulation service tool
        this.log.trace(`${LOG_PREFIX} Updating the parent id in the tabulation service tool`);
        this.landUsesCategoriesTableService.reportingFrameworkId = this.reportingFrameworkId;
        this.cd.detectChanges();

    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.landUsesCategoriesTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.landUsesCategoriesTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.landUsesCategoriesTableService.loading$.subscribe(
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
        this.landUsesCategoriesTableService.searchTerm = event;
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
        this.landUsesCategoriesTableService.sortColumn = column;
        this.landUsesCategoriesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.landUsesCategoriesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.landUsesCategoriesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates domain id change events to the table service
     * @param event The newly desired landUseCategory parent id
     */
    onReportingFrameworkIdChange(event: any) {

        this.log.trace(`${LOG_PREFIX} Changing Reporting Framework Id to ${event.target.value}`);

        // Clear any previously selected land use category
        if (this.selectedDomainActivitiesIds.length > 0) {
            this.selectedDomainActivitiesIds = [];
            this.reset.emit();
        }

        // Clear the current search term
        this.landUsesCategoriesTableService.searchTerm = "";

        // Update the domain id
        this.landUsesCategoriesTableService.reportingFrameworkId = event.target.value;

        this.cd.detectChanges();
    }



    /** 
    * Propagates LandUseCategory Selection Event to the Parent Component
    * @param id The id of the Selected LandUseCategory
    */
    onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected LandUseCategory Id: ${id}`);

        // Update the Selected LandUseCategory Ids array
        this.selectedDomainActivitiesIds = [id];

        // Push the Selected LandUseCategory Id to the Parent Component
        this.select.emit({ reportingFrameworkId: this.reportingFrameworkId, landUseCategoryId: id });
    }


    /** 
    * Propagates LandUsesCategories Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked LandUseCategory
    */
    onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked LandUseCategory Id: ${id}`);

        // Insert the newly Checked LandUseCategory Id into the Selected LandUseCategory Ids array - if its nonexistent
        if (this.selectedDomainActivitiesIds.indexOf(id) == -1) {
            this.selectedDomainActivitiesIds.push(id);
        }

        // Notify the Parent Component
        this.check.emit({ reportingFrameworkId: this.reportingFrameworkId, landUseCategoryId: id });
    }


    /** 
    * Propagates LandUsesCategories Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked LandUseCategory
    */
    onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked LandUseCategory Id: ${id}`);

        // Removes the newly Unchecked LandUseCategory Id from the Selected LandUseCategory Ids array - if in existence
        let index: number = this.selectedDomainActivitiesIds.indexOf(id);
        if (index != -1) {
            this.selectedDomainActivitiesIds.splice(index, 1);
        }

        // Notify the Parent Component
        this.uncheck.emit({ reportingFrameworkId: this.reportingFrameworkId, landUseCategoryId: id });
    }


    isSelected(landUseCategoryId: number) {
        return this.selectedDomainActivitiesIds.some(id => id == landUseCategoryId);
    }

    isChecked(landUseCategoryId: number) {
        return this.selectedDomainActivitiesIds.some(id => id == landUseCategoryId);
    }

    truncate(value: string, args: any[]): string {
        const limit = args.length > 0 ? parseInt(args[0], 10) : 20;
        const trail = args.length > 1 ? args[1] : '...';
        return value.length > limit ? value.substring(0, limit) + trail : value;
    }
}
