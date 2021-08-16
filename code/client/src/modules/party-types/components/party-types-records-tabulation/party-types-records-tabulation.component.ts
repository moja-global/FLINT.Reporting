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
import { PartyTypesRecordsTabulationService } from '@modules/party-types/services/party-types-records-tabulation.service';
import { BasePartyTypes} from '@modules/party-types/data/base-party-types';
import { ConnectivityStatusService } from '@common/services';
import { PartyTypesRecordsCreationModalComponent } from '@modules/party-types/containers/party-types-records-creation-modal/party-types-records-creation-modal.component';
import { PartyTypesRecordsDeletionModalComponent } from '@modules/party-types/containers/party-types-records-deletion-modal/party-types-records-deletion-modal.component';
import { PartyTypesRecordsUpdationModalComponent } from '@modules/party-types/containers/party-types-records-updation-modal/party-types-records-updation-modal.component';
import { ActivatedRoute, Router } from '@angular/router';
import { NavigationService } from '@modules/navigation/services';
import { PartyType } from '@modules/party-types/models';

const LOG_PREFIX: string = "[Party Types Records Tabulation]";

@Component({
    selector: 'sb-party-types-records-tabulation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './party-types-records-tabulation.component.html',
    styleUrls: ['party-types-records-tabulation.component.scss'],
})
export class PartyTypesRecordsTabulationComponent implements OnInit, AfterViewInit {

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

    // Keep tabs on the target part type
    @Input() targetPartyType!: PartyType | undefined;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    state: any = {};
 

    constructor(
        public partyTypesTableService: PartyTypesRecordsTabulationService,
        private cd: ChangeDetectorRef,
        private modalService: NgbModal,
        public connectivityStatusService: ConnectivityStatusService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private navigationService: NavigationService,
        private log: NGXLogger) {
    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {
                this.targetPartyType = Object.assign({}, BasePartyTypes.find(p => p.id == params.get('parentAdministrativeLevelId')));
                this.onParentPartyTypeChange(params.get('parentAdministrativeLevelId'));
            })
        );        
    }


    ngAfterViewInit() {

        // Set the initial page and page size values on the pagination component.
        this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
        this.pagination.initialize(this.partyTypesTableService.page, this.pageSize);

        // Subscribe to the total value changes and propagate them to the pagination component.
        // These values typically change in response to the user filtering the records.
        this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
        this._subscriptions.push(
            this.partyTypesTableService.total$.subscribe(
                (total) => {
                    this.pagination.total = total;
                }));

        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user searches, sorts or moves from one record page to another.
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.partyTypesTableService.loading$.subscribe(
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
        this.partyTypesTableService.searchTerm = event;
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
        this.partyTypesTableService.sortColumn = column;
        this.partyTypesTableService.sortDirection = direction;
        this.cd.detectChanges();
    }

    /**
     * Propagates page change events to the table service
     * @param event The page to load
     */
    onPageChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
        this.partyTypesTableService.page = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates page size change events to the table service
     * @param event The newly desired page size
     */
    onPageSizeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
        this.partyTypesTableService.pageSize = event;
        this.cd.detectChanges();
    }

    /**
     * Propagates parent party type id change events to the table service
     * @param event The newly desired parent party type id
     */
     onParentPartyTypeChange(event: any) {
        this.log.trace(`${LOG_PREFIX} Changing Parent Party Type Id to ${event}`);
        this.partyTypesTableService.parentAdministrativeLevelId = event;
        this.cd.detectChanges();
    }    

    /**
     * Propagates Party Types records Addition Requests to the responsible component
     */
    onAddPartyType() {
        this.log.trace(`${LOG_PREFIX} Adding a new Party Type record`);
        const modalRef = this.modalService.open(PartyTypesRecordsCreationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.targetPartyType = this.targetPartyType;
    }

    /**
     * Propagates Party Types records Updation Requests to the responsible component
     */
    onUpdatePartyType(id: number) {
        this.log.trace(`${LOG_PREFIX} Updating Party Type record`);
        this.log.debug(`${LOG_PREFIX} Party Type record Id = ${id}`);
        const modalRef = this.modalService.open(PartyTypesRecordsUpdationModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
        modalRef.componentInstance.targetPartyType = this.targetPartyType;
    }

    /**
     * Propagates Party Types records Deletion Requests to the responsible component
     */
    onDeletePartyType(id: number) {
        this.log.trace(`${LOG_PREFIX} Deleting Party Type record`);
        this.log.debug(`${LOG_PREFIX} Party Type record Id = ${id}`);
        const modalRef = this.modalService.open(PartyTypesRecordsDeletionModalComponent, { centered: true, backdrop: 'static' });
        modalRef.componentInstance.id = id;
        modalRef.componentInstance.targetPartyType = this.targetPartyType;
    }


    /**
     * Opens Party Types Home Window
     */
     onOpenPartyType(state: any) {
        this.log.trace(`${LOG_PREFIX} Opening Party Type Home Window`);
        this.log.debug(`${LOG_PREFIX} State = ${JSON.stringify(state)}`);

        this.router.navigate(['/parties', state.id], {queryParams: {name: state.name}});

    }

}
