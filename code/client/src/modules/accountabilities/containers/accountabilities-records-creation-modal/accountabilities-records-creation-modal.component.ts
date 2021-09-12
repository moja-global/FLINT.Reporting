import {
    ChangeDetectionStrategy,
    Component,
    HostListener,
    Input,
    OnInit,
    ViewChild
} from '@angular/core';
import { AccountabilitiesRecordsCreationComponent } from '../../components/accountabilities-records-creation/accountabilities-records-creation.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription, timer } from 'rxjs';

const LOG_PREFIX: string = "[Accountabilities Records Creation Modal]";

@Component({
    selector: 'sb-accountabilities-records-creation-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-records-creation-modal.component.html',
    styleUrls: ['accountabilities-records-creation-modal.component.scss'],
})
export class AccountabilitiesRecordsCreationModalComponent implements OnInit {


    // Inject a reference to the Accountabilities records creation component. 
    // This will provide a way of propagating save requests to it
    @ViewChild(AccountabilitiesRecordsCreationComponent) component!: AccountabilitiesRecordsCreationComponent;

    // Instantiate and avail a previously selected Parties arrays to the parent component.
    // This will allow the parent component to specify the previously selected Parties.
    // This might then be used to disable the previously selected Parties if the next configuration is set to true
    @Input() previouslySelectedPartiesIds: number[] = [];

    // Instantiate and avail the target accountability type id variable to the parent component.
    // This will allow the parent component to inject the details of the accountability type
    @Input() accountabilityRuleId: number | null = null;

    // Keep tabs on the current processing status.
    // These statuses include:
    // 1. new         - at the very beginning
    // 2. saving      - when the record is submitted for saving
    // 3. invalid     - when the record is submitted for saving and is found to include form control errors
    // 4. failed      - when the record is submitted for saving and an unexpected error occurs
    // 5. succeeded   - when the record is submitted for saving and all goes well
    // 6. retrying    - when the record is submitted for saving, fails and the saving is retried
    private _statusSubject$ = new BehaviorSubject<string>("nextable");
    readonly status$ = this._statusSubject$.asObservable();

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public activeAccountabilitiesModal: NgbActiveModal,
        private log: NGXLogger) { }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }

    /**
     * Sets the processing status to 'saving', triggering a display change, and then 
     * propagates the saving request to the Accountabilities records creation component
     */
    onSave() {
        this._statusSubject$.next("saving");
        this.component.save();
    }


    /**
     * Calls the View Child's onNext() function
     */
    onNext() {
        this.component.onNext();
    }

    /**
     * Sets the processing status to 'saveable' triggering a display change.
     */
    onLoadedNext() {
        this._statusSubject$.next("saveable");
    }


    /**
     * Calls the View Child's onPrevious() function
     */
    onPrevious() {
        this.component.onPrevious();
    }

    /**
     * Sets the processing status to 'nextable' triggering a display change.
     */
    onLoadedPrevious() {
        this._statusSubject$.next("nextable");
    }

    /**
     * Sets the processing status to 'succeeded' triggering a display change.
     */
    onSucceeded() {
        this._statusSubject$.next("succeeded");
        timer(1000).subscribe(x => { this.onQuit(); })
    }

    /**
     * Sets the processing status to either 'failed' or 'invalid' triggering a display change.
     */
    onFailed(event: any) {
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
        this._statusSubject$.next("retrying");
    }


    /**
     * Sets the processing status to 'new' triggering a display change
     */
    onContinue() {
        this._statusSubject$.next("nextable");
    }

    /**
     * Clears the processing status and closes the modal
     */
    onQuit() {
        this._statusSubject$.next("");
        this.activeAccountabilitiesModal.close();
    }

}
