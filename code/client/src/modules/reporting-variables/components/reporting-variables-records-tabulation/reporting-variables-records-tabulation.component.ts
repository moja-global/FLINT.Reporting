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
import { ActivatedRoute } from '@angular/router';
import { ReportingFramework } from '@modules/reporting-frameworks/models/reporting-framework.model';
import { ReportingVariablesRecordsTabulationService } from '@modules/reporting-variables/services/reporting-variables-records-tabulation.service';
import { ReportingVariablesRecordsCreationModalComponent, ReportingVariablesRecordsUpdationModalComponent, ReportingVariablesRecordsDeletionModalComponent } from '@modules/reporting-variables/containers';

const LOG_PREFIX: string = "[Reporting Variables Records Tabulation Component]";

@Component({
    selector: 'sb-reporting-variables-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-variables-records-tabulation.component.html',
    styleUrls: ['reporting-variables-records-tabulation.component.scss'],
})
export class ReportingVariablesRecordsTabulationComponent implements OnInit, OnDestroy, AfterViewInit {

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

    //Keep tabs on the target Reporting Framework
    targetReportingFramework: ReportingFramework = new ReportingFramework();    

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];


    constructor(
        public reportingVariablesTableService: ReportingVariablesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private modalService: NgbModal,
        private activatedRoute: ActivatedRoute,
        private log: NGXLogger) {

            
    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(

            this.activatedRoute.queryParamMap
                .subscribe(params => {

                    // Get and update the id from the query parameters
                    this.log.trace(`${LOG_PREFIX} Getting and updating the id from the query parameters`);
                    const id: string | null = params.get('reportingFrameworkId');
                    this.log.debug(`${LOG_PREFIX} Id = ${id}`);

                    // Get the target Reporting Framework name from the query parameters
                    this.log.trace(`${LOG_PREFIX} Getting the target Reporting Framework name from the query parameters`);
                    const name: string | null = params.get('reportingFrameworkName');
                    this.log.debug(`${LOG_PREFIX} Reporting Framework name = ${name}`);


                    if (id != null && name != null) {

                        // Set the target Reporting Framework id in the current component
                        this.log.trace(`${LOG_PREFIX} Setting the target Reporting Framework id in the current component`);
                        this.targetReportingFrameworkId = parseInt(id);

                        // Set the target Reporting Framework name in the current component
                        this.log.trace(`${LOG_PREFIX} Setting the target Reporting Framework name in the current component`);
                        this.targetReportingFrameworkName = name;

                    }



                })
        );
       
       
    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.reportingVariablesTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.reportingVariablesTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.reportingVariablesTableService.loading$.subscribe(
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
     * Sets the target reporting framework's id
     */
     set targetReportingFrameworkId(id: number | null) {
        this.log.trace(`${LOG_PREFIX} Setting Reporting Framework id to ${id}`);
        this.targetReportingFramework.id = id;
        if(id) {
            this.onReportingFrameworkChange(id);
        }
    }
    
    
    /**
     * Sets the target reporting framework's name
     */
     set targetReportingFrameworkName(name: string | null) {
        this.log.trace(`${LOG_PREFIX} Setting Reporting Framework name to ${name}`);         
        this.targetReportingFramework.name = name;
    }    

    /**
     * Propagates search events to the table service
     * @param event The term to search by
     */
    onSearch(event: any) {
        this.log.trace(`${LOG_PREFIX} Searching for ${event}`);
        this.reportingVariablesTableService.searchTerm = event;
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
        this.reportingVariablesTableService.sortColumn = column;
        this.reportingVariablesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.reportingVariablesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.reportingVariablesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates Reporting Framework id change events to the table service
     * @param event The newly desired Reporting Framework id
     */
    onReportingFrameworkChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Reporting Variable Framework Id to ${event}`);
        this.reportingVariablesTableService.reportingFrameworkId = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates Reporting Variables records Addition Requests to the responsible component
     */
    onAddReportingVariable() {
        this.log.trace(`${LOG_PREFIX} Adding a new Reporting Variable record`);
        this.log.debug(`${LOG_PREFIX} Reporting Variable Category = ${JSON.stringify(this.targetReportingFramework)}`);
        const modalRef = this.modalService.open(ReportingVariablesRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.targetReportingFramework = this.targetReportingFramework;
    }

    /**
     * Propagates Reporting Variables records Updation Requests to the responsible component
     */
    onUpdateReportingVariable(id: number) {
        this.log.trace(`${LOG_PREFIX} Updating Reporting Variable record`);
        this.log.debug(`${LOG_PREFIX} Reporting Variable record Id = ${id}`);
        this.log.debug(`${LOG_PREFIX} Reporting Variable Category = ${JSON.stringify(this.targetReportingFramework)}`);
        const modalRef = this.modalService.open(ReportingVariablesRecordsUpdationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
        modalRef.componentInstance.targetReportingFramework = this.targetReportingFramework;
        
    }

    /**
     * Propagates Reporting Variables records Deletion Requests to the responsible component
     */
    onDeleteReportingVariable(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Reporting Variable record`);
        this.log.debug(`${LOG_PREFIX} Reporting Variable record Id = ${id}`);
        this.log.debug(`${LOG_PREFIX} Reporting Variable Category = ${JSON.stringify(this.targetReportingFramework)}`);
        const modalRef = this.modalService.open(ReportingVariablesRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
        modalRef.componentInstance.targetReportingFramework = this.targetReportingFramework;
    }

}
