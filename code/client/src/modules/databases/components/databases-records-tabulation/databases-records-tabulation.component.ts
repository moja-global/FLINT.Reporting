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
import { DatabasesRecordsTabulationService } from '../../services';
import { ConnectivityStatusService } from '@common/services';
import { DatabasesRecordsCreationModalComponent } from '@modules/databases/containers/databases-records-creation-modal/databases-records-creation-modal.component';
import { DatabasesRecordsDeletionModalComponent } from '@modules/databases/containers/databases-records-deletion-modal/databases-records-deletion-modal.component';
import { DatabasesRecordsUpdationModalComponent } from '@modules/databases/containers/databases-records-updation-modal/databases-records-updation-modal.component';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { HttpClient } from '@angular/common/http';

const LOG_PREFIX: string = "[Databases Records Tabulation]";
const API_PREFIX: string = "api/v1/crf_tables";
const HEADERS = { 'Content-Type': 'application/json' };

@Component({
    selector: 'sb-databases-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './databases-records-tabulation.component.html',
    styleUrls: ['databases-records-tabulation.component.scss'],
})
export class DatabasesRecordsTabulationComponent implements OnInit, AfterViewInit {

  // The base url of the server
  private _baseUrl: string = environment.baseUrl;    

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
        public databasesTableService: DatabasesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private modalService: NgbModal,
        private http: HttpClient,
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
        this.pagination.initialize(this.databasesTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.databasesTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.databasesTableService.loading$.subscribe(
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
        this.databasesTableService.searchTerm = event;
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
        this.databasesTableService.sortColumn = column;
        this.databasesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.databasesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.databasesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates Databases records Addition Requests to the responsible component
     */
    onAddDatabase() {
        this.log.trace(`${LOG_PREFIX} Adding a new Database record`);
        const modalRef = this.modalService.open(DatabasesRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
    }

    /**
     * Propagates Databases records Updation Requests to the responsible component
     */
    onUpdateDatabase(id: number) {
        this.log.trace(`${LOG_PREFIX} Updating Database record`);
        this.log.debug(`${LOG_PREFIX} Database record Id = ${id}`);
        const modalRef = this.modalService.open(DatabasesRecordsUpdationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }

    /**
     * Propagates Databases records Deletion Requests to the responsible component
     */
    onDeleteDatabase(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Database record`);
        this.log.debug(`${LOG_PREFIX} Database record Id = ${id}`);
        const modalRef = this.modalService.open(DatabasesRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
    }


    /**
     * Opens Databases records Home Window
     */
     onOpenDatabase(state: any) {
        this.log.trace(`${LOG_PREFIX} Opening Database record Home Window`);
        this.log.debug(`${LOG_PREFIX} State = ${JSON.stringify(state)}`);

        this.router.navigate(['/databases', state.id], {queryParams: {name: state.name}});

    }  

    downloadCRFTable(data: {databaseId: number, startYear: number, endYear: number}) { 
        this.log.trace(`${LOG_PREFIX} Downloading Database: ${JSON.stringify(data)}`);
        return this.http.get(`${this._baseUrl}/${API_PREFIX}/partyId/48/databaseId/${data.databaseId}/from/${data.startYear}/to/${data.endYear}`,{responseType: 'blob'}); 
      }


}
