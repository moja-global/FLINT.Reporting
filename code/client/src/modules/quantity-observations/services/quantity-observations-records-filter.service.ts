import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { QuantityObservationsDataService } from './quantity-observations-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Observable, of, Subject, Subscription } from 'rxjs';
import { DatabaseFilter } from '@common/models/database-filter.model';
import { QuantityObservation } from '../models/quantity-observation.model';
import { map } from 'rxjs/operators';

const LOG_PREFIX: string = "[Quantity Observations Records Filter Service]";

@Injectable({ providedIn: 'root' })
export class QuantityObservationsRecordsFilterService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Quantity Observations records are transformed as per the user defined search 
    // or sort criteria    
    private _quantityObservationsSubject$ = new BehaviorSubject<QuantityObservation[]>([]);
    private _quantityObservations$ = this._quantityObservationsSubject$.asObservable();


    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _filterSubject$ = new BehaviorSubject<DatabaseFilter>({ databaseId: -1, partyId: -1, startYear: -1, endYear: -1 });
    private _filter$ = this._filterSubject$.asObservable();

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private quantityObservationsDataService: QuantityObservationsDataService,
        private log: NGXLogger) {

    }

    ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    /**
     * Returns an observable containing Quantity Observations records that have been filtered as per the user defined criteria
     */
    get quantityObservations$() {
        this.log.trace(`${LOG_PREFIX} Getting quantityObservations$ observable`);
        this.log.debug(`${LOG_PREFIX} Current quantityObservations$ observable value = ${JSON.stringify(this._quantityObservationsSubject$.value)}`);
        return this._quantityObservations$;
    }


    /**
     * Returns an observable containing a boolean flag that indicates whether or not a data operation exercise (sorting, searching etc.) is currently underway
     */
    get loading$() {
        this.log.trace(`${LOG_PREFIX} Getting loading$ observable`);
        this.log.debug(`${LOG_PREFIX} Current loading$ observable value = ${JSON.stringify(this._loadingSubject$.value)}`);
        return this._loading$;
    }


    /**
     * Returns an observable containing the filter criteria
     */
    get filter$()  {
        this.log.trace(`${LOG_PREFIX} Getting filter$ observable`);
        this.log.debug(`${LOG_PREFIX} Current filter$ observable value = ${JSON.stringify(this._filterSubject$.value)}`);
        return this._filter$;
    }


    /**
     * Filters a database's quantity observations
     * @param filter the database's filter criteria
     */
    /**
     * Retrieves Quantity Observations
     * @param databaseFilter The criteria by which the quantity observations should be filtered
     */
    public filterQuantityObservations(databaseFilter: DatabaseFilter) {

        // Flag
        this._loadingSubject$.next(true);

        let filteredQuantityObservations: Observable<QuantityObservation[]>;

        if (this._filterSubject$.value.databaseId == -1 || this._filterSubject$.value.partyId == -1) {

            filteredQuantityObservations =
                this.quantityObservationsDataService
                    .getAllQuantityObservations({ databaseId: databaseFilter.databaseId, partiesIds: databaseFilter.partyId });

        } else {

            let startYear: number = databaseFilter.startYear == undefined || databaseFilter.startYear == null ? -1 : databaseFilter.startYear;
            let endYear: number = databaseFilter.endYear == undefined || databaseFilter.endYear == null ? -1 : databaseFilter.endYear;

            

            if (startYear != -1 && endYear != -1) {
                if (this._filterSubject$.value.databaseId != databaseFilter.databaseId || this._filterSubject$.value.partyId != databaseFilter.partyId) {

                    filteredQuantityObservations =
                        this.quantityObservationsDataService
                            .getAllQuantityObservations({ databaseId: databaseFilter.databaseId, partiesIds: databaseFilter.partyId })
                            .pipe(map(observations => observations.filter(observation => observation.year >= startYear && observation.year <= endYear)));
    
                } else {
    
                    filteredQuantityObservations =
                        of(this.quantityObservationsDataService.records)
                            .pipe(map(observations => observations.filter(observation => observation.year >= startYear && observation.year <= endYear)));
                }
            } else {
                if (this._filterSubject$.value.databaseId != databaseFilter.databaseId || this._filterSubject$.value.partyId != databaseFilter.partyId) {

                    filteredQuantityObservations =
                        this.quantityObservationsDataService
                            .getAllQuantityObservations({ databaseId: databaseFilter.databaseId, partiesIds: databaseFilter.partyId });
    
                } else {
    
                    filteredQuantityObservations =
                        of(this.quantityObservationsDataService.records);
                }
            }


            
        }

        filteredQuantityObservations.subscribe(
            filtered => {

                // Broadcast
                this._quantityObservationsSubject$.next(filtered);
                this._filterSubject$.next(Object.assign({}, databaseFilter));

                // Unflag
                this._loadingSubject$.next(false);

            },
            error => {
                console.log('Could not load todos.');
                // Unflag
                this._loadingSubject$.next(false);
            }
        );

    }


}
