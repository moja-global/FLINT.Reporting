import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { INDICATORS } from '../data/indicators';
import { Indicator } from '../models/indicator.model';

@Injectable()
export class IndicatorsDataService {

    constructor() { }

    getIndicators$(): Observable<Indicator[]> {
        return of(INDICATORS);
    }

    getIndicator$(IndicatorId: number): Observable<Indicator> {
        return this.getIndicators$()
            .pipe(
                map(indicators => indicators.filter(i => i.id == IndicatorId)),
                map(indicators => indicators[0]));
    }


}