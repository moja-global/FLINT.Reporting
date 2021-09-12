import { Component, EventEmitter, HostListener, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ActivityLocationSelectionPayload } from '@modules/activities-locations/components/activities-locations-records-selection/activities-locations-records-selection.component';
import { DateRangeSelectorModalComponent } from '@modules/filters/containers/date-range-selector-modal/date-range-selector-modal.component';
import { DateRange } from '@modules/filters/models';
import { DateRangeSelectionService } from '@modules/filters/services';
import { NgbDate, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs/internal/Subscription';

const LOG_PREFIX: string = "[Date Range Filter Component]";

export interface DateRangeSelectionPayload {
  accountabilityId: number | null | undefined,
  activityLocationId: number | null | undefined,
  fromDate: NgbDate | null;
  toDate: NgbDate | null; 
}

@Component({
  selector: 'sb-date-range-filter',
  templateUrl: './date-range-filter.component.html',
  styleUrls: ['./date-range-filter.component.scss']
})
export class DateRangeFilterComponent implements OnInit, OnDestroy {

  // Instantiate and avail an activityLocationSelectionPayload field to the parent component.
  // This will allow the parent component to initialize the target activity location.
  // All beneficiaries selections will then be tied to the provided activity location
  @Input() activityLocationSelectionPayload: ActivityLocationSelectionPayload | null = null;

  // Instantiate and avail a fromDate field to the parent component.
  // This will allow the parent component to initialize the fromDate  
  @Input() fromDate: NgbDate | null = null;

  // Instantiate and avail a toDate field to the parent component.
  // This will allow the parent component to initialize the toDate   
  @Input() toDate: NgbDate | null = null;

  // Instantiate a 'select' events emitter.
  // This will allow us to broadcast date range selection events to the parent component
  @Output() select: EventEmitter<DateRangeSelectionPayload> = new EventEmitter<DateRangeSelectionPayload>();  

  hoveredDate: NgbDate | null = null;

  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private dateRangeSelectionService: DateRangeSelectionService,
    private modalService: NgbModal,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);
    this._subscriptions.push(
      this.dateRangeSelectionService.dateRange$.subscribe((d: DateRange) => {
        this.select.emit({
          accountabilityId: this.activityLocationSelectionPayload?.accountabilityId,
          activityLocationId: this.activityLocationSelectionPayload?.activityLocationId,
          fromDate: d.fromDate,
          toDate: d.toDate
        });
      })
    );
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach(s => s.unsubscribe());
  }

  launchTimePeriodSelector() {
    const modalRef = this.modalService.open(DateRangeSelectorModalComponent, { centered: true, backdrop: 'static', size: 'lg', scrollable: true });
    modalRef.componentInstance.activityLocationSelectionPayload = this.activityLocationSelectionPayload;
    modalRef.componentInstance.fromDate = this.fromDate;
    modalRef.componentInstance.toDate = this.toDate;
  }


}
