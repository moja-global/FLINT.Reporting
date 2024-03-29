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
import { UnitsRecordsTabulationService } from '../../services';
import { UnitsRecordsCreationModalComponent } from '@modules/units/containers/units-records-creation-modal/units-records-creation-modal.component';
import { UnitsRecordsDeletionModalComponent } from '@modules/units/containers/units-records-deletion-modal/units-records-deletion-modal.component';
import { UnitsRecordsUpdationModalComponent } from '@modules/units/containers/units-records-updation-modal/units-records-updation-modal.component';
import { ActivatedRoute } from '@angular/router';
import { UnitCategory } from '@modules/unit-categories/models';

const LOG_PREFIX: string = "[Units Records Tabulation Component]";

@Component({
    selector: 'sb-units-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './units-records-tabulation.component.html',
    styleUrls: ['units-records-tabulation.component.scss'],
})
export class UnitsRecordsTabulationComponent implements OnInit, OnDestroy, AfterViewInit {

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

    //Keep tabs on the target unit category
    targetUnitCategory: UnitCategory = new UnitCategory();    

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];



    constructor(
        public unitsTableService: UnitsRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private modalService: NgbModal,
        private activatedRoute: ActivatedRoute,
        private log: NGXLogger) {

            
    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(
            this.activatedRoute.queryParamMap.subscribe(params => {

                // Get and update the name from the query parameters
                const name: string | null = params.get('name');
                this.targetUnitCategory.name = name;

            })
        );         

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {

                // Get and update the id from the path parameter
                const id: string | null = params.get('unitCategoryId');
                this.targetUnitCategory.id = (id == null? null : parseInt(id));

                // Update the unit category id in the tabulation service tool
                this.onUnitCategoryChange(params.get('unitCategoryId'));
            })
        );

       
    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.unitsTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.unitsTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.unitsTableService.loading$.subscribe(
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
        this.unitsTableService.searchTerm = event;
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
        this.unitsTableService.sortColumn = column;
        this.unitsTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.unitsTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.unitsTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates unit category id change events to the table service
     * @param event The newly desired unit category id
     */
    onUnitCategoryChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Unit Category Id to ${event}`);
        this.unitsTableService.unitCategoryId = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates Units records Addition Requests to the responsible component
     */
    onAddUnit() {
        this.log.trace(`${LOG_PREFIX} Adding a new Unit record`);
        this.log.debug(`${LOG_PREFIX} Unit Category = ${JSON.stringify(this.targetUnitCategory)}`);
        const modalRef = this.modalService.open(UnitsRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.targetUnitCategory = this.targetUnitCategory;
    }

    /**
     * Propagates Units records Updation Requests to the responsible component
     */
    onUpdateUnit(id: number) {
        this.log.trace(`${LOG_PREFIX} Updating Unit record`);
        this.log.debug(`${LOG_PREFIX} Unit record Id = ${id}`);
        this.log.debug(`${LOG_PREFIX} Unit Category = ${JSON.stringify(this.targetUnitCategory)}`);
        const modalRef = this.modalService.open(UnitsRecordsUpdationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
        modalRef.componentInstance.targetUnitCategory = this.targetUnitCategory;
        
    }

    /**
     * Propagates Units records Deletion Requests to the responsible component
     */
    onDeleteUnit(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Unit record`);
        this.log.debug(`${LOG_PREFIX} Unit record Id = ${id}`);
        this.log.debug(`${LOG_PREFIX} Unit Category = ${JSON.stringify(this.targetUnitCategory)}`);
        const modalRef = this.modalService.open(UnitsRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
        modalRef.componentInstance.targetUnitCategory = this.targetUnitCategory;
    }

}
