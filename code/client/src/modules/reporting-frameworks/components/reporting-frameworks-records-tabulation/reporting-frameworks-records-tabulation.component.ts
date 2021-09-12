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
import { ReportingFrameworksRecordsTabulationService } from '../../services';
import { ReportingFrameworksRecordsCreationModalComponent } from '@modules/reporting-frameworks/containers/reporting-frameworks-records-creation-modal/reporting-frameworks-records-creation-modal.component';
import { ReportingFrameworksRecordsDeletionModalComponent } from '@modules/reporting-frameworks/containers/reporting-frameworks-records-deletion-modal/reporting-frameworks-records-deletion-modal.component';
import { ReportingFrameworksRecordsUpdationModalComponent } from '@modules/reporting-frameworks/containers/reporting-frameworks-records-updation-modal/reporting-frameworks-records-updation-modal.component';
import { Router } from '@angular/router';

const LOG_PREFIX: string = "[Reporting Frameworks Records Tabulation Component]";

@Component({
    selector: 'sb-reporting-frameworks-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-frameworks-records-tabulation.component.html',
    styleUrls: ['reporting-frameworks-records-tabulation.component.scss'],
})
export class ReportingFrameworksRecordsTabulationComponent implements OnInit, OnDestroy, AfterViewInit {

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

    constructor(
        private router: Router,
        private cd: ChangeDetectorRef,
        public reportingFrameworksRecordsTabulationService: ReportingFrameworksRecordsTabulationService,        
        private modalService: NgbModal,
        private log: NGXLogger) {


    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.reportingFrameworksRecordsTabulationService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.reportingFrameworksRecordsTabulationService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.reportingFrameworksRecordsTabulationService.loading$.subscribe(
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
        this.reportingFrameworksRecordsTabulationService.searchTerm = event;
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
        this.reportingFrameworksRecordsTabulationService.sortColumn = column;
        this.reportingFrameworksRecordsTabulationService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.reportingFrameworksRecordsTabulationService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.reportingFrameworksRecordsTabulationService.pageSize = event;
        this.cd.detectChanges();
    }


    /**
     * Propagates Reporting Frameworks Records Addition Requests to the responsible component
     */
    onAddReportingFramework() {
        this.log.trace(`${LOG_PREFIX} Adding a new Reporting Framework record`);
        const modalRef = this.modalService.open(ReportingFrameworksRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
    }

    /**
     * Propagates Reporting Frameworks Records Updation Requests to the responsible component
     */
    onUpdateReportingFramework(id: number) {
        this.log.trace(`${LOG_PREFIX} Updating Reporting Framework record`);
        this.log.debug(`${LOG_PREFIX} Reporting Framework record Id = ${id}`);
        const modalRef = this.modalService.open(ReportingFrameworksRecordsUpdationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;

    }

    /**
     * Propagates Reporting Frameworks Records Deletion Requests to the responsible component
     */
    onDeleteReportingFramework(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Reporting Framework record`);
        this.log.debug(`${LOG_PREFIX} Reporting Framework record Id = ${id}`);
        const modalRef = this.modalService.open(ReportingFrameworksRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }


    /**
     * Open Land Use Classes Home Window
     */
     onOpenLandUseClasses(reportingFramework: any) {
        this.log.trace(`${LOG_PREFIX} Opening Land Use Classes Home Window`);
        this.log.debug(`${LOG_PREFIX} Reporting Framework = ${JSON.stringify(reportingFramework)}`);

        this.router.navigate(['/land_uses_categories', reportingFramework.id]);

    }     

}
