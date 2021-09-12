import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    HostListener,
    Input,
    OnDestroy,
    OnInit,
    ViewChild
} from '@angular/core';
import { AccountabilitiesRulesRecordsCreationComponent } from '../../components/accountabilities-rules-records-creation/accountabilities-rules-records-creation.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription, timer } from 'rxjs';

const LOG_PREFIX: string = "[Accountabilities Rules Records Creation Modal]";

@Component({
    selector: 'sb-accountabilities-rules-records-creation-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-rules-records-creation-modal.component.html',
    styleUrls: ['accountabilities-rules-records-creation-modal.component.scss'],
})
export class AccountabilitiesRulesRecordsCreationModalComponent implements OnInit, OnDestroy {

    // Instantiate and avail the parent id variable to the parent component.
    // This will allow the parent component to inject the details of the target parent  
    @Input() accountabilityTypeId: number | null = null;

    // Instantiate and avail a previously selected Party Types Ids arrays to the parent component.
    // This will allow the parent component to specify the previously selected Party Types Ids.
    // This might then be used to disable the previously selected Party Types Ids if the next configuration is set to true
    @Input() previouslySelectedPartiesTypesIds: number[] = [];


    // Inject a reference to the Accountabilities Rules records creation component. 
    // This will provide a way of propagating save requests to it
    @ViewChild(AccountabilitiesRulesRecordsCreationComponent) component!: AccountabilitiesRulesRecordsCreationComponent;

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
        public activeAccountabilitiesRulesModal: NgbActiveModal,
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
     * propagates the saving request to the Accountabilities Rules records creation component
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
        timer(1000).subscribe(x => { this.onQuit(); })
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
        this.activeAccountabilitiesRulesModal.close();
    }

}
