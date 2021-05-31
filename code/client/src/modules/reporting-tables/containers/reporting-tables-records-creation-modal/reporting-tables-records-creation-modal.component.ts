import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    HostListener,
    Input,
    OnDestroy,
    OnInit,
    ViewChild} from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { ReportingFramework } from '@modules/reporting-frameworks/models';
import { ReportingTablesRecordsCreationComponent } from '@modules/reporting-tables/components/reporting-tables-records-creation/reporting-tables-records-creation.component';

const LOG_PREFIX: string = "[Reporting Tables Records Creation Modal]";

@Component({
    selector: 'sb-reporting-tables-records-creation-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-tables-records-creation-modal.component.html',
    styleUrls: ['reporting-tables-records-creation-modal.component.scss'],
})
export class ReportingTablesRecordsCreationModalComponent implements OnInit, OnDestroy {

    // Instantiate and avail the target reportingTable category variable to the parent component.
    // This will allow the parent component to inject the details of the current target  
    // reportingTable category  
    @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();

    // Inject a reference to the Reporting Tables records creation component. 
    // This will provide a way of propagating save requests to it
    @ViewChild(ReportingTablesRecordsCreationComponent) component!: ReportingTablesRecordsCreationComponent;

    // Keep tabs on the current processing status.
    // These statuses include:
    // 1. initializing - when the component is being added into the container
    // 2. ready        - when the component has been initialized and is ready to accept entries
    // 3. saving       - when the record is submitted for saving
    // 4. invalid      - when the record is submitted for saving and is found to include form control errors
    // 5. failed       - when the record is submitted for saving and an unexpected error occurs
    // 6. succeeded    - when the record is submitted for saving and all goes well
    // 7. retrying     - when the record is submitted for saving, fails and the saving is retried
    private _statusSubject$ = new BehaviorSubject<string>("ready");
    readonly status$ = this._statusSubject$.asObservable();

    // Keep tabs on whether or not we are online
    online: boolean = false;

    // Keep tabs on whether or not we should display the child components
    displayChildren: boolean = false;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];


    constructor(
        public activeReportingTablesModal: NgbActiveModal,
        private cd: ChangeDetectorRef,
        private log: NGXLogger) { }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }


    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

    }

    /**
     * Sets the processing status to 'ready' triggering a display change
     */
    onInitialized() {
        this.log.trace(`${LOG_PREFIX} Initialized`);
        this.cd.detectChanges();
        this._statusSubject$.next("ready");
    }

    /**
     * Sets the processing status to 'saving', triggering a display change, and then 
     * propagates the saving request to the Reporting Tables records creation component
     */
    onSave() {
        this.log.trace(`${LOG_PREFIX} Saving`);
        this._statusSubject$.next("saving");
        this.component.save();
    }

    /**
     * Sets the processing status to 'succeeded' triggering a display change.
     */
    onSucceeded() {
        this.log.trace(`${LOG_PREFIX} Succeeded`);
        this._statusSubject$.next("succeeded");
    }

    /**
     * Sets the processing status to either 'failed' or 'invalid' triggering a display change.
     */
    onFailed(event: any) {
        this.log.trace(`${LOG_PREFIX} Failed`);
        switch (event) {
            case 400:
                this._statusSubject$.next("invalid");
                break;
            default:
                this._statusSubject$.next("failed");
        }

    }

    /**
     * Sets the processing status to 'retrying' triggering a display change.
     */
    onRetry() {
        this.log.trace(`${LOG_PREFIX} Retrying`);
        this._statusSubject$.next("retrying");
    }


    /**
     * Sets the processing status to 'new' triggering a display change
     */
    onContinue() {
        this.log.trace(`${LOG_PREFIX} Continuing`);
        this._statusSubject$.next("ready");
    }

    /**
     * Clears the processing status and closes the modal
     */
    onQuit() {
        this.log.trace(`${LOG_PREFIX} Quiting`);
        this._statusSubject$.next("");
        this.activeReportingTablesModal.close();
    }

}
