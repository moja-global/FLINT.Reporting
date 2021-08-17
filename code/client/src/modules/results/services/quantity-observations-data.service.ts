import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';
import { ACTIVITIES_QUANTITY_OBSERVATIONS } from '../data/quantity-observations';
import { QuantityObservation } from '../models';

@Injectable()
export class QuantityObservationsDataService {

    constructor() { }

    getActivitiesQuantityObservations$(phenomenonTypeId: number): Observable<QuantityObservation> {
        return of(ACTIVITIES_QUANTITY_OBSERVATIONS)
            .pipe(
                map(quantityObservations => quantityObservations.filter(q => q.phenomenonTypeId == phenomenonTypeId)),
                map(quantityObservations => quantityObservations[0]));
    }    

}
