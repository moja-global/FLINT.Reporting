import {
    ChangeDetectionStrategy,
    Component,
    HostListener,
    Input,
    OnInit,
    ViewChild
} from '@angular/core';
import { ReportingFramework } from '@modules/reporting-frameworks/models';
import { ReportingVariablesRecordsDeletionComponent } from '@modules/reporting-variables/components/reporting-variables-records-deletion/reporting-variables-records-deletion.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription, timer } from 'rxjs';

const LOG_PREFIX: string = "[Reporting Variables Records Deletion Modal]";

@Component({
    selector: 'sb-reporting-variables-records-deletion-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-variables-records-deletion-modal.component.html',
    styleUrls: ['reporting-variables-records-deletion-modal.component.scss'],
})
export class ReportingVariablesRecordsDeletionModalComponent implements OnInit {

    // Instantiate and avail the id variable to the parent component.
    // This will allow the parent component to inject the id of the 
    // record that has been served up for deletion during the component's initialization.
    @Input() id!: number;

    // Instantiate and avail the target Reporting Framework variable to the parent component.
    // This will allow the parent component to inject the details of the current target  
    // Reporting Framework  
    @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();   

    // Inject a reference to the Reporting Variables records deletion component. 
    // This will provide a way of propagating delete requests to it
    @ViewChild(ReportingVariablesRecordsDeletionComponent) component!: ReportingVariablesRecordsDeletionComponent;

    // Keep tabs on the current processing status.
    // These statuses include:
    // 1. new         - at the very beginning
    // 2. deleting    - when the record is submitted for saving
    // 3. failed      - when the record is submitted for saving and an unexpected error occurs
    // 4. succeeded   - when the record is submitted for saving and all goes well
    // 5. retrying    - when the record is submitted for saving, fails and the saving is retried
    private _statusSubject$ = new BehaviorSubject<string>("new");
    readonly status$ = this._statusSubject$.asObservable();

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public activeReportingVariablesModal: NgbActiveModal,
        private log: NGXLogger) { }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }

    /**
     * Sets the processing status to 'deleting', triggering a display change, and then 
     * propagates the deletion request to the Reporting Variables records creation component
     */
    onDelete() {
        this._statusSubject$.next("deleting");
        this.component.delete();
    }

    /**
     * Sets the processing status to 'succeeded', triggering a display change, and then 
     * autocloses the modal after a short delay.
     */
    onSucceeded() {
        this._statusSubject$.next("succeeded");
        timer(1000).subscribe(x => { this.onQuit(); })
    }

    /**
     * Sets the processing status to 'failed' triggering a display change.
     */
    onFailed(event: number) {
        this._statusSubject$.next("failed");
    }

    /**
     * Sets the processing status to 'retrying' triggering a display change.
     */
    onRetry() {
        this._statusSubject$.next("retrying");
    }

    /**
     * Clears the processing status and closes the modal
     */
    onQuit() {
        this._statusSubject$.next("");
        this.activeReportingVariablesModal.close();
    }

}
