import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { QuantityObservation } from '../models';
import {
    NEW_CASES_QUANTITY_OBSERVATIONS,
    CUMULATIVE_CASES_QUANTITY_OBSERVATIONS,
    NEW_RECOVERIES_QUANTITY_OBSERVATIONS, 
    CUMULATIVE_RECOVERIES_QUANTITY_OBSERVATIONS,
    NEW_DEATHS_QUANTITY_OBSERVATIONS, 
    CUMULATIVE_DEATHS_QUANTITY_OBSERVATIONS,
    ISSUES_QUANTITY_OBSERVATIONS,
    COMPOSITIONS_QUANTITY_OBSERVATIONS
} from '@modules/situations/data/quantity-observations';
import { map } from 'rxjs/internal/operators/map';

@Injectable()
export class QuantityObservationsDataService {

    constructor() { }

    getProgressionsStatusQuantityObservations$(phenomenonTypeId: number, steps: number): Observable<QuantityObservation[]> {
        return of(CUMULATIVE_CASES_QUANTITY_OBSERVATIONS.concat(CUMULATIVE_RECOVERIES_QUANTITY_OBSERVATIONS).concat(CUMULATIVE_DEATHS_QUANTITY_OBSERVATIONS))
            .pipe(
                map(quantityObservations => quantityObservations.filter(q => q.phenomenonTypeId == phenomenonTypeId)),
                map(quantityObservations => quantityObservations.filter(q => q.idx % steps == 0)));
    }

    getProgressionsPaceQuantityObservations$(phenomenonTypeId: number, steps: number): Observable<QuantityObservation[]> {
        return of(NEW_CASES_QUANTITY_OBSERVATIONS.concat(NEW_RECOVERIES_QUANTITY_OBSERVATIONS).concat(NEW_DEATHS_QUANTITY_OBSERVATIONS))
            .pipe(
                map(quantityObservations => quantityObservations.filter(q => q.phenomenonTypeId == phenomenonTypeId)),
                map(quantityObservations => quantityObservations.filter(q => q.idx % steps == 0)));
    }    

    getIssuesQuantityObservations$(phenomenonTypeId: number): Observable<QuantityObservation> {
        return of(ISSUES_QUANTITY_OBSERVATIONS)
            .pipe(
                map(quantityObservations => quantityObservations.filter(q => q.phenomenonTypeId == phenomenonTypeId)),
                map(quantityObservations => quantityObservations[0]));
    }

    getCompositionsQuantityObservation$(phenomenonTypeId: number): Observable<QuantityObservation> {
        return of(COMPOSITIONS_QUANTITY_OBSERVATIONS)
            .pipe(
                map(quantityObservations => quantityObservations.filter(q => q.phenomenonTypeId == phenomenonTypeId)),
                map(quantityObservations => quantityObservations[0]));
    }    

}
