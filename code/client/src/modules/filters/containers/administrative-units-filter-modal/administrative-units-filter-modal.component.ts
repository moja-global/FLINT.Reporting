import {
    ChangeDetectionStrategy,
    Component,
    HostListener,
    Input,
    OnInit,
    ViewChild
} from '@angular/core';
import { AdministrativeUnitsFilterComponent } from '@modules/filters/components/administrative-units-filter/administrative-units-filter.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription, timer } from 'rxjs';

const LOG_PREFIX: string = "[Accountabilities Records Creation Modal]";

@Component({
    selector: 'sb-administrative-units-filter-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './administrative-units-filter-modal.component.html',
    styleUrls: ['administrative-units-filter-modal.component.scss'],
})
export class AdministrativeUnitsFilterModalComponent implements OnInit {


    // Inject a reference to the Accountabilities records creation component. 
    // This will provide a way of propagating save requests to it
    @ViewChild(AdministrativeUnitsFilterComponent) component!: AdministrativeUnitsFilterComponent;

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
}
