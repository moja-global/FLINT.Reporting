import {
    ChangeDetectionStrategy,
    Component,
    Input,
    OnInit,
    ViewChild
} from '@angular/core';
import { ReportingFrameworksRecordsDeletionComponent } from '../../components/reporting-frameworks-records-deletion/reporting-frameworks-records-deletion.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription, timer } from 'rxjs';
import { ConnectivityStatusService } from '@common/services';

const LOG_PREFIX: string = "[Reporting Frameworks Records Deletion Modal]";

@Component({
    selector: 'sb-reporting-frameworks-records-deletion-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-frameworks-records-deletion-modal.component.html',
    styleUrls: ['reporting-frameworks-records-deletion-modal.component.scss'],
})
export class ReportingFrameworksRecordsDeletionModalComponent implements OnInit {

    // Instantiate and avail the id variable to the parent component.
    // This will allow the parent component to inject the id of the 
    // record that has been served up for deletion during the component's initialization.
    @Input() id!: number;

    // Inject a reference to the Reporting Frameworks records deletion component. 
    // This will provide a way of propagating delete requests to it
    @ViewChild(ReportingFrameworksRecordsDeletionComponent) component!: ReportingFrameworksRecordsDeletionComponent;

    // Keep tabs on the current processing status.
    // These statuses include:
    // 1. new         - at the very beginning
    // 2. deleting    - when the record is submitted for saving
    // 3. failed      - when the record is submitted for saving and an unexpected error occurs
    // 4. succeeded   - when the record is submitted for saving and all goes well
    // 5. retrying    - when the record is submitted for saving, fails and the saving is retried
    private _statusSubject$ = new BehaviorSubject<string>("new");
    readonly status$ = this._statusSubject$.asObservable();

    // Keep tabs on whether or not we are online
    online: boolean = false;

    // Instantiate a central gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public activeReportingFrameworksModal: NgbActiveModal,
        public connectivityStatusService: ConnectivityStatusService,
        private log: NGXLogger) { }

    ngOnInit() {

        // Subscribe to connectivity status notifications.
        this.log.trace(`${LOG_PREFIX} Subscribing to connectivity status notifications`);
        this._subscriptions.push(
            this.connectivityStatusService.online$.subscribe(
                (status) => {
                    this.online = status;
                }));
    }

    ngOnDestroy() {
    }

    /**
     * Sets the processing status to 'deleting', triggering a display change, and then 
     * propagates the deletion request to the Reporting Frameworks records creation component
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
        this.activeReportingFrameworksModal.close();
    }

}
