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
import { ActivatedRoute, Router } from '@angular/router';
import { LandUsesCategoriesRecordsCreationModalComponent, LandUsesCategoriesRecordsUpdationModalComponent, LandUsesCategoriesRecordsDeletionModalComponent } from '@modules/land-uses-categories/containers';
import { LandUsesCategoriesRecordsTabulationService } from '@modules/land-uses-categories/services/land-uses-categories-records-tabulation.service';

const LOG_PREFIX: string = "[Land Uses Categories Records Tabulation Component]";

@Component({
    selector: 'sb-land-uses-categories-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './land-uses-categories-records-tabulation.component.html',
    styleUrls: ['land-uses-categories-records-tabulation.component.scss'],
})
export class LandUsesCategoriesRecordsTabulationComponent implements OnInit, OnDestroy, AfterViewInit {

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

    //Keep tabs on the parent context's id
    parentReportingFrameworkId: number | null | undefined;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];



    constructor(
        public landUsesCategoriesRecordsTabulationService: LandUsesCategoriesRecordsTabulationService,
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
                const temp: string | null = params.get('reportingFrameworkId');
                this.parentReportingFrameworkId = (temp == null ? null : parseInt(temp));
                this.onContextChange(this.parentReportingFrameworkId);


            })
        );
    }



    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.landUsesCategoriesRecordsTabulationService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.landUsesCategoriesRecordsTabulationService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                    this.cd.detectChanges();
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.landUsesCategoriesRecordsTabulationService.loading$.subscribe(
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
        this.landUsesCategoriesRecordsTabulationService.searchTerm = event;
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
        this.landUsesCategoriesRecordsTabulationService.sortColumn = column;
        this.landUsesCategoriesRecordsTabulationService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.landUsesCategoriesRecordsTabulationService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.landUsesCategoriesRecordsTabulationService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates context id change events to the table service
     * @param event The newly desired context id
     */
    onContextChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Context Id to ${event}`);
        this.landUsesCategoriesRecordsTabulationService.reportingFrameworkId = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates LandUsesCategories records Addition Requests to the responsible component
     */
    onAddLandUseCategory() {
        this.log.trace(`${LOG_PREFIX} Adding a new Land Use Category Record`);
        this.log.debug(`${LOG_PREFIX} Land Use Category = ${JSON.stringify(this.parentReportingFrameworkId)}`);
        const modalRef = this.modalService.open(LandUsesCategoriesRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.reportingFrameworkId = this.parentReportingFrameworkId;
    }

    /**
     * Propagates LandUsesCategories records Updation Requests to the responsible component
     */
    onUpdateLandUseCategory(id: number) {
        this.log.trace(`${LOG_PREFIX} Updating Land Use Category Record`);
        this.log.debug(`${LOG_PREFIX} Land Use Category Record Id = ${id}`);
        this.log.debug(`${LOG_PREFIX} Land Use Category = ${JSON.stringify(this.parentReportingFrameworkId)}`);
        const modalRef = this.modalService.open(LandUsesCategoriesRecordsUpdationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;

    }

    /**
     * Propagates LandUsesCategories records Deletion Requests to the responsible component
     */
    onDeleteLandUseCategory(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Land Use Category Record`);
        this.log.debug(`${LOG_PREFIX} Land Use Category Record Id = ${id}`);
        this.log.debug(`${LOG_PREFIX} Land Use Category = ${JSON.stringify(this.parentReportingFrameworkId)}`);
        const modalRef = this.modalService.open(LandUsesCategoriesRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }


    /**
     * Domain Activities Indicators Page
     */
    onOpenLandUseCategoryIndicators(landUseCategory: any) {
        this.log.trace(`${LOG_PREFIX} Opening Domain Activities Indicators Page`);
        this.log.debug(`${LOG_PREFIX} Land Use Category = ${JSON.stringify(landUseCategory)}`);

        this.router.navigate(['/land_uses_categories', landUseCategory.id]);

    }  

}
