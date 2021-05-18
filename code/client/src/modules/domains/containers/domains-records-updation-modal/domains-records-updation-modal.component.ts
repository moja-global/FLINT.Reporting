import {
    ChangeDetectionStrategy,
    Component,
    Input,
    OnInit,
    ViewChild
} from '@angular/core';
import { DomainsRecordsUpdationComponent } from '../../components/domains-records-updation/domains-records-updation.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription, timer } from 'rxjs';
import { ConnectivityStatusService } from '@common/services';


const LOG_PREFIX: string = "[Domains Records Updation Modal]";

@Component({
    selector: 'sb-domains-records-updation-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './domains-records-updation-modal.component.html',
    styleUrls: ['domains-records-updation-modal.component.scss'],
})
export class DomainsRecordsUpdationModalComponent implements OnInit {

    // Instantiate and avail the id variable to the parent component
    @Input() id!: number;

    // Inject a reference to the domain updation component. 
    // This will provide a way of propagating save requests to it
    @ViewChild(DomainsRecordsUpdationComponent) component!: DomainsRecordsUpdationComponent;

    // Add a status flag that will toggle the status of the save button
    saveable: boolean = true;

    // Status: the saving status
    private _statusSubject$ = new BehaviorSubject<string>("new");
    readonly status$ = this._statusSubject$.asObservable();

    // Keep tabs on whether or not we are online
    online: boolean = false;

    // Instantiate a gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public activeDomainsModal: NgbActiveModal,
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

    onSave() {
        this._statusSubject$.next("saving");
        this.component.save();
    }

    onSucceeded() {
        this._statusSubject$.next("succeeded");
        timer(1000).subscribe(x => { this.onQuit(); })
    }

    onFailed(event: number) {
        switch (event) {
            case 400:
                this._statusSubject$.next("invalid");
                break;
            default:
                this._statusSubject$.next("failed");
        }

    }

    onRetry() {
        this._statusSubject$.next("retrying");
    }

    onQuit() {
        this._statusSubject$.next("");
        this.activeDomainsModal.close();
    }

}
