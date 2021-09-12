import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DateRange } from '@modules/filters/models';
import { DateRangeSelectionService } from '@modules/filters/services';
import { NgbCalendar, NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'sb-date-range-selector',
  templateUrl: './date-range-selector.component.html',
  styleUrls: ['./date-range-selector.component.scss']
})
export class DateRangeSelectorComponent {

  // Instantiate and avail a fromDate field to the parent component.
  // This will allow the parent component to initialize the fromDate  
  @Input() fromDate: NgbDate | null;

  // Instantiate and avail a toDate field to the parent component.
  // This will allow the parent component to initialize the toDate   
  @Input() toDate: NgbDate | null;


  hoveredDate: NgbDate | null = null;  

  constructor(calendar: NgbCalendar, private dateRangeSelectionService: DateRangeSelectionService) {
    this.fromDate = calendar.getToday();
    this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
  }

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
