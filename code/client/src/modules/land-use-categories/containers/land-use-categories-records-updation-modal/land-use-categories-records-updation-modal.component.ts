import {
    ChangeDetectionStrategy,
    Component,
    HostListener,
    Input,
    OnInit,
    ViewChild
} from '@angular/core';
import { ReportingFramework } from '@modules/reporting-frameworks/models';
import { LandUseCategoriesRecordsUpdationComponent } from '@modules/land-use-categories/components/land-use-categories-records-updation/land-use-categories-records-updation.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription, timer } from 'rxjs';


const LOG_PREFIX: string = "[Land Use Categories Records Updation Modal]";

@Component({
    selector: 'sb-land-use-categories-records-updation-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './land-use-categories-records-updation-modal.component.html',
    styleUrls: ['land-use-categories-records-updation-modal.component.scss'],
})
export class LandUseCategoriesRecordsUpdationModalComponent implements OnInit {

    // Instantiate and avail the id variable to the parent component.
    // This will allow the parent component to inject the id of the 
    // record that has been served up for updation during the component's initialization.
    @Input() id!: number;

    // Instantiate and avail the target landUseCategory category variable to the parent component.
    // This will allow the parent component to inject the details of the current target  
    // landUseCategory category  
    @Input() targetReportingFramework: ReportingFramework = new ReportingFramework(); 

    // Inject a reference to the Land Use Categories records updation component. 
    // This will provide a way of propagating save requests to it
    @ViewChild(LandUseCategoriesRecordsUpdationComponent) component!: LandUseCategoriesRecordsUpdationComponent;

    // Keep tabs on the current processing status.
    // These statuses include:
    // 1. new         - at the very beginning
    // 2. saving      - when the record is submitted for saving
    // 3. invalid     - when the record is submitted for saving and is found to include form control errors
    // 4. failed      - when the record is submitted for saving and an unexpected error occurs
    // 5. succeeded   - when the record is submitted for saving and all goes well
    // 6. retrying    - when the record is submitted for saving, fails and the saving is retried
    private _statusSubject$ = new BehaviorSubject<string>("new");
    readonly status$ = this._statusSubject$.asObservable();

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public activeLandUseCategoriesModal: NgbActiveModal,
        private log: NGXLogger) { }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    /**
     * Sets the processing status to 'saving', triggering a display change, and then 
     * propagates the saving request to the Land Use Categories records updation component
     */
    onSave() {
        this._statusSubject$.next("saving");
        this.component.save();
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
     * Sets the processing status to either 'failed' or 'invalid' triggering a display change.
     */
    onFailed(event: number) {
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
     * Clears the processing status and closes the modal
     */
    onQuit() {
        this._statusSubject$.next("");
        this.activeLandUseCategoriesModal.close();
    }

}
