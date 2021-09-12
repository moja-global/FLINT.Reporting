import { Injectable } from '@angular/core';
import { Message } from '@common/models/message.model';
import { Subject } from 'rxjs';
import { DateRange } from '../models';

const LOG_PREFIX: string = "[Date Range Selection Service]";

@Injectable({ providedIn: 'root' })
export class DateRangeSelectionService {

    private _dateRangeSubject$ = new Subject<DateRange>();
    readonly dateRange$ = this._dateRangeSubject$.asObservable();

    select(range: DateRange) {
        this._dateRangeSubject$.next(range);
    }

}