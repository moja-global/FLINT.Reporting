import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { QuantityObservationsDataService } from './quantity-observations-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Observable, of, Subscription } from 'rxjs';
import { DatabaseFilter } from '@common/models/database-filter.model';
import { QuantityObservation } from '../models/quantity-observation.model';
import { debounceTime, map } from 'rxjs/operators';
import { ConfigService } from '@common/services/config.service';
import { DatabaseFilterService } from '@common/services/database-filter.service';

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


    // Keep tabs of the current filter to determine whether the filtering criteria changed
    private _filter: DatabaseFilter = { databaseId: -1, startYear: -1, endYear: -1, landUseCategoryId: -1, partyId: -1 };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private quantityObservationsDataService: QuantityObservationsDataService,
        private configService: ConfigService,
        private databaseFilterService: DatabaseFilterService,
        private log: NGXLogger) {

        // Subscribe to database filters changes notifications.
        this.log.trace(`${LOG_PREFIX} Subscribing to database filter changes notifications`);
        this._subscriptions.push(
            this.databaseFilterService.filter$
            .pipe(debounceTime(500))
            .subscribe(
                databaseFilter => {

                    // Database filter changed.
                    this.log.debug(`${LOG_PREFIX} Database Filter = ${JSON.stringify(databaseFilter)}`);

                    // Filter quantity observations
                    this.filterQuantityObservations(databaseFilter);

                }));

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
     * Returns the filter's current database id
     */
    get databaseId() {
        this.log.trace(`${LOG_PREFIX} Getting the filter's current database id`);
        this.log.debug(`${LOG_PREFIX} Filter's current database id = ${JSON.stringify(this._filter.databaseId)}`);
        return this._filter.databaseId;
    }


    /**
     * Returns the filter's current start year
     */
    get startYearId() {
        this.log.trace(`${LOG_PREFIX} Getting the filter's current start year`);
        this.log.debug(`${LOG_PREFIX} Filter's current start year id = ${JSON.stringify(this._filter.startYear)}`);
        return this._filter.startYear;
    }


    /**
     * Returns the filter's current end year
     */
    get endYearId() {
        this.log.trace(`${LOG_PREFIX} Getting the filter's current end year`);
        this.log.debug(`${LOG_PREFIX} Filter's current end year id = ${JSON.stringify(this._filter.endYear)}`);
        return this._filter.endYear;
    }


    /**
     * Returns the filter's current land use category id
     */
    get landUseCategoryId() {
        this.log.trace(`${LOG_PREFIX} Getting the filter's current land use category id`);
        this.log.debug(`${LOG_PREFIX} Filter's current land use category id = ${JSON.stringify(this._filter.landUseCategoryId)}`);
        return this._filter.landUseCategoryId;
    }


    /**
     * Returns the filter's current party id
     */
    get partyId() {
        this.log.trace(`${LOG_PREFIX} Getting the filter's current party id`);
        this.log.debug(`${LOG_PREFIX} Filter's current party id = ${JSON.stringify(this._filter.partyId)}`);
        return this._filter.partyId;
    }


    /**
     * Filters a database's quantity observations
     * @param filter the database's filter criteria
     */
    private filterQuantityObservations(databaseFilter: DatabaseFilter) {

        // Flag
        this._loadingSubject$.next(true);



        // Get the incoming filters
        const incomingDatabaseId: number = databaseFilter.databaseId == undefined || databaseFilter.databaseId == null ? -1 : databaseFilter.databaseId;
        const incomingPartyId: number = databaseFilter.partyId == undefined || databaseFilter.partyId == null ? -1 : databaseFilter.partyId;
        const incomingLandUseCategoryId: number = databaseFilter.landUseCategoryId == undefined || databaseFilter.landUseCategoryId == null ? -1 : databaseFilter.landUseCategoryId;
        const incomingStartYear: number = databaseFilter.startYear == undefined || databaseFilter.startYear == null ? -1 : databaseFilter.startYear;
        const incomingEndYear: number = databaseFilter.endYear == undefined || databaseFilter.endYear == null ? -1 : databaseFilter.endYear;

        // If the incoming database id or the party id is null, return an empty array
        if (incomingDatabaseId == -1 || incomingPartyId == -1) {

            this._quantityObservationsSubject$.next([]);
            this._filter = databaseFilter;

            // Unflag
            this._loadingSubject$.next(true);

            return;

        } else {

            let filteredQuantityObservations: Observable<QuantityObservation[]>;


            // If the incoming database or party ids are different from the current database or party ids,
            // get new observations that correspond to the incoming database id / party id pair from the api;
            // Otherwise, get the observations currently held in the data service local storage
            if (incomingDatabaseId != this.databaseId || incomingPartyId != this.partyId) {

                this.log.trace(`${LOG_PREFIX} Getting new observations from the api`);
                filteredQuantityObservations =
                    this.quantityObservationsDataService
                        .getAllQuantityObservations({ databaseId: databaseFilter.databaseId, partiesIds: databaseFilter.partyId });

            } else {

                this.log.trace(`${LOG_PREFIX} Getting observations from the data service`);
                filteredQuantityObservations = of(this.quantityObservationsDataService.records);
            }

            // Subscribe and broadcast results
            filteredQuantityObservations
                .pipe(
                    map(observations => this.conditionallyApplyLandUseCategoryFilter(observations, incomingLandUseCategoryId)),
                    map(observations => this.conditionallyApplyStartYearFilter(observations, incomingStartYear)),
                    map(observations => this.conditionallyApplyEndYearFilter(observations, incomingEndYear)))
                .subscribe(
                    filtered => {

                        this.log.debug(`${LOG_PREFIX} Filtered Quantity Observations = ${JSON.stringify(filtered)}`);

                        // Broadcast
                        this._quantityObservationsSubject$.next(filtered);

                        // Update local database filter field
                        this._filter = databaseFilter;

                        // Unflag
                        this._loadingSubject$.next(false);

                    },
                    error => {
                        console.log('Could not load observations.');
                        // Unflag
                        this._loadingSubject$.next(false);
                    }
                );
        }

    }



    private conditionallyApplyLandUseCategoryFilter(observations: QuantityObservation[], incomingLandUseCategoryId: number): QuantityObservation[] {

        // Apply the land use category filter
        if (incomingLandUseCategoryId != -1) {

            this.log.debug(`${LOG_PREFIX} Filtering by Land Use Category ${incomingLandUseCategoryId}`);

            if (incomingLandUseCategoryId == this.configService.forestLandId) {

                return observations.filter(o =>
                    o.landUseCategoryId == this.configService.forestLandRemainingForestLandId ||
                    o.landUseCategoryId == this.configService.croplandConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.grasslandConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.wetlandsConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToForestLandId);

            } else if (incomingLandUseCategoryId == this.configService.cropland) {

                return observations.filter(
                    o => o.landUseCategoryId == this.configService.croplandRemainingCroplandId ||
                        o.landUseCategoryId == this.configService.forestLandConvertedToCroplandId ||
                        o.landUseCategoryId == this.configService.grasslandConvertedToCroplandId ||
                        o.landUseCategoryId == this.configService.wetlandsConvertedToCroplandId ||
                        o.landUseCategoryId == this.configService.settlementsConvertedToCroplandId ||
                        o.landUseCategoryId == this.configService.otherLandConvertedToCroplandId);

            } else if (incomingLandUseCategoryId == this.configService.grasslandId) {

                return observations.filter(
                    o => o.landUseCategoryId == this.configService.grasslandRemainingGrasslandId ||
                        o.landUseCategoryId == this.configService.croplandConvertedToGrasslandId ||
                        o.landUseCategoryId == this.configService.forestLandConvertedToGrasslandId ||
                        o.landUseCategoryId == this.configService.wetlandsConvertedToGrasslandId ||
                        o.landUseCategoryId == this.configService.settlementsConvertedToGrasslandId ||
                        o.landUseCategoryId == this.configService.otherLandConvertedToGrasslandId);

            } else if (incomingLandUseCategoryId == this.configService.wetlandsId) {

                return observations.filter(o => o.landUseCategoryId == this.configService.wetlandsRemainingWetlandsId ||
                    o.landUseCategoryId == this.configService.croplandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.grasslandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.forestLandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToWetlandsId);
            }
        }

        return observations;

    }

    private conditionallyApplyStartYearFilter(observations: QuantityObservation[], incomingStartYear: number): QuantityObservation[] {

        if (incomingStartYear != -1) {

            this.log.debug(`${LOG_PREFIX} Filtering by Start Year ${incomingStartYear}`);

            return observations.filter(observation => observation.year >= incomingStartYear);
        }

        return observations;
    }


    private conditionallyApplyEndYearFilter(observations: QuantityObservation[], incomingEndYear: number): QuantityObservation[] {

        if (incomingEndYear != -1) {

            this.log.debug(`${LOG_PREFIX} Filtering by End Year ${incomingEndYear}`);

            return observations.filter(observation => observation.year <= incomingEndYear);
        }

        return observations;
    }
}
