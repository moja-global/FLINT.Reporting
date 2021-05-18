import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    Input,
    ViewChild,
} from '@angular/core';
import { LoadingAnimationComponent, PaginationComponent } from '@common/components';
import { SortEvent } from '@common/directives/sortable.directive';
import { CountryService } from '@modules/tables/services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';


const LOG_PREFIX: string = "[Countries Table]";

@Component({
    selector: 'sb-ng-bootstrap-table',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './ng-bootstrap-table.component.html',
    styleUrls: ['ng-bootstrap-table.component.scss'],
})
export class NgBootstrapTableComponent implements AfterViewInit {

    // Inject a reference to the loading animation component. 
    // This will provide a way of informing it of the status of 
    // the processing events happening in the background.
    @ViewChild(LoadingAnimationComponent) animation!: LoadingAnimationComponent;

    // Inject a reference to the pagination component.
    // This will provide a way for setting the initial page / page size settings, 
    // and thereafter provide a way for updating the changes in record totals following filtering.
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

    // Instantiate a gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscription: Subscription = new Subscription();


    constructor(
        public countryService: CountryService,
        private changeDetectorRef: ChangeDetectorRef,
        private log: NGXLogger) { }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.countryService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscription.add(
            this.countryService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscription.add(
            this.countryService.loading$.subscribe(
                (loading) => {
                    this.animation.loading = loading;
                    this.changeDetectorRef.detectChanges();
                }));

    }

    ngOnDestroy() {

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscription.unsubscribe();
    }

    /**
     * Propagates search events to the table service
     * @param event The term to search by
     */
    onSearch(event: any) {
        this.log.trace(`${LOG_PREFIX} Searching for ${event}`);
        this.countryService.searchTerm = event;
    }

    /**
     * Propagates sort events to the table service
     * @param param0 The column / direction to sort by
     */
    onSort({ column, direction }: SortEvent) {
        this.log.trace(`${LOG_PREFIX} Sorting ${column} in ${direction} order`);
        this.sortedColumn = column;
        this.sortedDirection = direction;
        this.countryService.sortColumn = column;
        this.countryService.sortDirection = direction;
        this.changeDetectorRef.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.countryService.page = event;
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.countryService.pageSize = event;
    }
}
