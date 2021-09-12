import {
    ChangeDetectionStrategy,
    Component,
    EventEmitter,
    HostListener,
    Input,
    OnInit,
    Output,
    ViewChild
} from '@angular/core';
import { ThemesService } from '@common/services';
import { DateRangeSelectorComponent } from '@modules/filters/components/date-range-selector/date-range-selector.component';
import { DateRange } from '@modules/filters/models';
import { DateRangeSelectionService } from '@modules/filters/services';
import { NgbActiveModal, NgbCalendar, NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription, timer } from 'rxjs';

const LOG_PREFIX: string = "[Date Range Selector Modal]";

@Component({
    selector: 'sb-date-range-selector-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './date-range-selector-modal.component.html',
    styleUrls: ['date-range-selector-modal.component.scss'],
})
export class DateRangeSelectorModalComponent implements OnInit {

    // Inject a reference to the date range selector component. 
    // This will provide a way of propagating ui requests to it
    @ViewChild(DateRangeSelectorComponent) component!: DateRangeSelectorComponent;

    // Instantiate and avail a fromDate field to the parent component.
    // This will allow the parent component to initialize the fromDate  
    @Input() fromDate: NgbDate | null = null;

    // Instantiate and avail a toDate field to the parent component.
    // This will allow the parent component to initialize the toDate   
    @Input() toDate: NgbDate | null = null;

    @Output() closed: EventEmitter<void> = new EventEmitter<void>();

    // Classes that are adjusted on the fly based on the prevailing theme
    customClasses: string[] = [];

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
        public activeModal: NgbActiveModal,
        private themesService: ThemesService,
        calendar: NgbCalendar, 
        private dateRangeSelectionService: DateRangeSelectionService,
        private log: NGXLogger) { }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
        this._subscriptions.push(
            this.themesService.themes$.subscribe((theme) => {
                this.customClasses = [];
                this.customClasses.push(`modal-${theme}`);
            })
        );
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    onClose() {
        this.activeModal.dismiss('Cross Click');
        this.closed.emit();
    }


  hoveredDate: NgbDate | null = null;  



  onDateSelection(date: NgbDate) {

    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date.after(this.fromDate)) {
      this.toDate = date;
    } else {
      this.toDate = null;
      this.fromDate = date;
    }

    this.dateRangeSelectionService.select(new DateRange({fromDate: this.fromDate,toDate: this.toDate}));

  }

  isHovered(date: NgbDate) {
    return this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate) {
    return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate) {
    return date.equals(this.fromDate) || (this.toDate && date.equals(this.toDate)) || this.isInside(date) || this.isHovered(date);
  }    
}
