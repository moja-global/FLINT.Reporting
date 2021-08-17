import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { QUANTITY_OBSERVATIONS } from '../data/quantity-observations';
import { QuantityObservation } from '../models';

@Injectable()
export class QuantityObservationsDataService {

    constructor() { }

    getQuantityObservations$(): Observable<QuantityObservation[]> {
        return of(QUANTITY_OBSERVATIONS);
    }

    getQuantityObservationsByIndicator$(phenomenonTypeId: number): Observable<QuantityObservation[]> {
        return this.getQuantityObservations$()
            .pipe(map(groups => groups.filter(i => i.phenomenonTypeId == phenomenonTypeId)));
    }


    getQuantityObservationsInStepsByIndicator$(indicatorId: number, steps: number): Observable<QuantityObservation[]> {
        return this.getQuantityObservations$()
            .pipe(

                // Filter by indicator id
                map(quantityObservations => quantityObservations.filter(q => q.phenomenonTypeId == indicatorId)),
                
                // Sort by date
                map(quantityObservations => {

                    quantityObservations.sort((q1, q2) => {

                        if (q1.timepointId < q2.timepointId) {
                            return -1;
                        }

                        if (q1.timepointId > q2.timepointId) {
                            return 1;
                        }

                        return 0;
                    });

                    return quantityObservations;

                }),

                // Asign a temporary index
                map(quantityObservations => {

                    

                    let index: number = 0;

                    quantityObservations.forEach(element => {
                        element.idx = ++index;
                    });

                    return quantityObservations;
                }),

                // Take after the specified number of steps
                map(quantityObservations => quantityObservations.filter(q => q.idx == undefined? false : q.idx % steps == 0)));
    }


    getQuantityObservation$(quantityObservationId: number): Observable<QuantityObservation> {
        return this.getQuantityObservations$()
            .pipe(
                map(groups => groups.filter(i => i.id == quantityObservationId)),
                map(groups => groups[0]));
    }


}