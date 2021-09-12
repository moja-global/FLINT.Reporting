import { NgbDate } from "@ng-bootstrap/ng-bootstrap/datepicker/datepicker.module";

export class DateRange {
 
  fromDate!: NgbDate | null;
  toDate!: NgbDate | null;

	constructor(options?: Partial<DateRange>) {
		Object.assign(this, options);
	}
}
