import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    HostListener,
    Input,
    OnInit,
    ViewChild,
} from '@angular/core';
import { LoadingAnimationComponent, PaginationComponent } from '@common/components';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { SortEvent } from '@common/directives/sortable.directive';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UnitCategoriesRecordsTabulationService } from '../../services';
import { ConnectivityStatusService } from '@common/services';
import { UnitCategoriesRecordsCreationModalComponent } from '@modules/unit-categories/containers/unit-categories-records-creation-modal/unit-categories-records-creation-modal.component';
import { UnitCategoriesRecordsDeletionModalComponent } from '@modules/unit-categories/containers/unit-categories-records-deletion-modal/unit-categories-records-deletion-modal.component';
import { UnitCategoriesRecordsUpdationModalComponent } from '@modules/unit-categories/containers/unit-categories-records-updation-modal/unit-categories-records-updation-modal.component';
import { Router } from '@angular/router';

const LOG_PREFIX: string = "[Unit Categories Records Tabulation]";

@Component({
    selector: 'sb-unit-categories-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './unit-categories-records-tabulation.component.html',
    styleUrls: ['unit-categories-records-tabulation.component.scss'],
})
export class UnitCategoriesRecordsTabulationComponent implements OnInit, AfterViewInit {

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

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    state: any = {};


    constructor(
        public unitCategoriesTableService: UnitCategoriesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private modalService: NgbModal,
        public connectivityStatusService: ConnectivityStatusService,
        private router: Router,
        private log: NGXLogger) {
    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.unitCategoriesTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.unitCategoriesTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.unitCategoriesTableService.loading$.subscribe(
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
        this.unitCategoriesTableService.searchTerm = event;
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
        this.unitCategoriesTableService.sortColumn = column;
        this.unitCategoriesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.unitCategoriesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.unitCategoriesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates Unit Categories records Addition Requests to the responsible component
     */
    onAddUnitCategory() {
        this.log.trace(`${LOG_PREFIX} Adding a new Unit Category record`);
        const modalRef = this.modalService.open(UnitCategoriesRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
    }

    /**
     * Propagates Unit Categories records Updation Requests to the responsible component
     */
    onUpdateUnitCategory(id: number) {
        this.log.trace(`${LOG_PREFIX} Updating Unit Category record`);
        this.log.debug(`${LOG_PREFIX} Unit Category record Id = ${id}`);
        const modalRef = this.modalService.open(UnitCategoriesRecordsUpdationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }

    /**
     * Propagates Unit Categories records Deletion Requests to the responsible component
     */
    onDeleteUnitCategory(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Unit Category record`);
        this.log.debug(`${LOG_PREFIX} Unit Category record Id = ${id}`);
        const modalRef = this.modalService.open(UnitCategoriesRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }


    /**
     * Opens Unit Categories records Home Window
     */
     onOpenUnitCategory(state: any) {
        this.log.trace(`${LOG_PREFIX} Opening Unit Category record Home Window`);
        this.log.debug(`${LOG_PREFIX} State = ${JSON.stringify(state)}`);

        this.router.navigate(['/unit_categories', state.id], {queryParams: {name: state.name}});

    }

}
