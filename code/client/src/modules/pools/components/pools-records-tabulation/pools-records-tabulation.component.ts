import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    Input,
    OnInit,
    ViewChild,
} from '@angular/core';
import { LoadingAnimationComponent, PaginationComponent } from '@common/components';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { SortEvent } from '@common/directives/sortable.directive';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PoolsRecordsTabulationService } from '../../services';
import { ConnectivityStatusService } from '@common/services';
import { PoolsRecordsCreationModalComponent } from '@modules/pools/containers/pools-records-creation-modal/pools-records-creation-modal.component';
import { PoolsRecordsDeletionModalComponent } from '@modules/pools/containers/pools-records-deletion-modal/pools-records-deletion-modal.component';
import { PoolsRecordsUpdationModalComponent } from '@modules/pools/containers/pools-records-updation-modal/pools-records-updation-modal.component';

const LOG_PREFIX: string = "[Pools Records Tabulation]";

@Component({
    selector: 'sb-pools-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './pools-records-tabulation.component.html',
    styleUrls: ['pools-records-tabulation.component.scss'],
})
export class PoolsRecordsTabulationComponent implements OnInit, AfterViewInit {

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
    @Input() pageSize: number = 10;

    // Keep tabs on the column that the records are currently sorted by.
    sortedColumn!: string;

    // Keep tabs on the direction that the records are currently sorted by: ascending or descending.
    sortedDirection!: string;

    // Keep tabs on whether or not we are online
    online: boolean = false;

    // Instantiate a central gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscriptions: Subscription[] = [];


    constructor(
        public poolsTableService: PoolsRecordsTabulationService,
        private changeDetectorRef: ChangeDetectorRef,
        private modalService: NgbModal,
        public connectivityStatusService: ConnectivityStatusService,
        private log: NGXLogger) {
    }

    ngOnInit() {

        // Subscribe to connectivity status notifications.
        this.log.trace(`${LOG_PREFIX} Subscribing to connectivity status notifications`);
        this._subscriptions.push(
            this.connectivityStatusService.online$.subscribe(
                (status) => {
                    this.online = status;
                }));
    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.poolsTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.poolsTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.poolsTableService.loading$.subscribe(
                (loading) => {
                    this.animation.loading = loading;
                    this.changeDetectorRef.detectChanges();
                }));

    }

    ngOnDestroy() {

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
        this.poolsTableService.searchTerm = event;
        this.changeDetectorRef.detectChanges();
    }

    /**
     * Propagates sort events to the table service
     * @param param0 The column / direction to sort by
     */
    onSort({ column, direction }: SortEvent) {
        this.log.trace(`${LOG_PREFIX} Sorting ${column} in ${direction} order`);
        this.sortedColumn = column;
        this.sortedDirection = direction;
        this.poolsTableService.sortColumn = column;
        this.poolsTableService.sortDirection = direction;
        this.changeDetectorRef.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.poolsTableService.page = event;
        this.changeDetectorRef.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.poolsTableService.pageSize = event;
        this.changeDetectorRef.detectChanges();
    }

    /**
     * Propagates Pools records Addition Requests to the responsible component
     */
    onAddPool() {
        this.log.trace(`${LOG_PREFIX} Adding a new Pool record`);
        const modalRef = this.modalService.open(PoolsRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
    }

    /**
     * Propagates Pools records Updation Requests to the responsible component
     */    
    onUpdatePool(id: number) {
        this.log.trace(`${LOG_PREFIX} Updating Pool record`);
        this.log.debug(`${LOG_PREFIX} Pool record Id = ${id}`);
        const modalRef = this.modalService.open(PoolsRecordsUpdationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }

    /**
     * Propagates Pools records Deletion Requests to the responsible component
     */    
    onDeletePool(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Pool record`);
        this.log.debug(`${LOG_PREFIX} Pool record Id = ${id}`);
        const modalRef = this.modalService.open(PoolsRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }

}
