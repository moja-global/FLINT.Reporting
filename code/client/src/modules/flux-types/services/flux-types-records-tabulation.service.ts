import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { FluxTypesDataService } from './flux-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, fromEvent, merge, Observable, Observer, of, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { first, map, mapTo } from 'rxjs/operators';
import { ConnectivityStatusService } from '@common/services';
import { FluxType } from '../models/flux-type.model';

const LOG_PREFIX: string = "[Flux Types Records Tabulation Data Service]";

@Injectable({ providedIn: 'root' })
export class FluxTypesRecordsTabulationService implements OnInit, OnDestroy {

    // Instantiate a loading status observable field.
    // This field's value will be updated / broadcasted whenever a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // Instantiate a Flux Types records observable field.
    // This field's value will be updated / broadcasted whenever Flux Types records are transformed as per the user defined criteria    
    private _fluxTypesSubject$ = new BehaviorSubject<FluxType[]>([]);
    private _fluxTypes$ = this._fluxTypesSubject$.asObservable();

    // Instantiate a total Flux Types records observable field.
    // This field's value will be updated / broadcasted whenever Flux Types records are transformed as per the user defined criteria.
    // It is basically the number of records that meet the user defined criteria    
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // Instantiate a state field.
    // This field represents the user defined criteria of which & how many Flux Types records should be displayed
    private _state: State = { page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // Instantiate a gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    // Keep tabs on whether or not we are online
    online: boolean = true;

    // Keeps tabs on whether or not the data service has been initialized 
    // i.e. initial data loaded from the data service
    isInitialized: boolean = false;

    constructor(
        private fluxTypesDataService: FluxTypesDataService,
        private connectivityStatusService: ConnectivityStatusService,
        private log: NGXLogger) {


        this._subscriptions.push(
            this.fluxTypesDataService.fluxTypes$
                .subscribe(
                    (fluxTypes: FluxType[]) => {
                        this._transform(fluxTypes);
                    }));

        this._subscriptions.push(
            this.connectivityStatusService.online$.subscribe(status => {

                // Update the connection status
                this.log.trace(`${LOG_PREFIX} Updating the connection status`);
                this.online = status;

                // React based on whether or not the user is online
                if (this.online) {

                    // Check if the table data service has been initialized
                    this.log.trace(`${LOG_PREFIX} Checking if the table data service has been initialized`);
                    this.log.debug(`${LOG_PREFIX} Initialization status = ${this.isInitialized}`);
                    if (!this.isInitialized) {

                        // Check if there is a need to initialize the table data service
                        this.log.trace(`${LOG_PREFIX} Checking if there is a need to initialize the table data service`);
                        if (this.fluxTypesDataService.records.length == 0) {

                            // Data is not available at the local data store
                            // There is a need to initialize the table data service
                            this.log.trace(`${LOG_PREFIX} There is a need to initialize the table data service`);

                            // Initialize the table data service
                            this.log.trace(`${LOG_PREFIX} Initializing the table data service`);
                            this.fluxTypesDataService
                                .getAllFluxTypes()
                                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                                .subscribe((fluxTypes: FluxType[]) => {

                                    // Initialization is complete
                                    this.log.trace(`${LOG_PREFIX} Initialization is complete`);
                                    this.isInitialized = true;
                                });

                        } else {

                            // Data is already available at the local data store
                            // There is no need to initialize the table data service
                            this.log.trace(`${LOG_PREFIX} There is a need to initialize the table data service`);
                            this.isInitialized = true;
                        }
                    }
                }

            })
        );

    }

    ngOnInit() {

        ononline
    }

    ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    // Observables & Subscriptions to check online/offline connectivity status


    isOnline$() {
        return merge<boolean>(
            fromEvent(window, 'offline').pipe(map(() => false)),
            fromEvent(window, 'online').pipe(map(() => true)),
            new Observable((sub: Observer<boolean>) => {
                sub.next(navigator.onLine);
                sub.complete();
            }));
    }

    /**
     * Returns an observable containing Flux Types records that have been filtered as per the desired state setting
     */
    get fluxTypes$() {
        this.log.trace(`${LOG_PREFIX} Getting fluxTypes$ observable`);
        this.log.debug(`${LOG_PREFIX} Current fluxTypes$ observable value = ${JSON.stringify(this._fluxTypesSubject$.value)}`);
        return this._fluxTypes$;
    }


    /**
     * Returns an observable containing the total number of Flux Types records that have been filtered as per the desired state setting
     */
    get total$() {
        this.log.trace(`${LOG_PREFIX} Getting total$ observable`);
        this.log.debug(`${LOG_PREFIX} Current total$ observable value = ${JSON.stringify(this._totalSubject$.value)}`);
        return this._total$;
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
     * Returns the currently active page
     */
    get page() {
        this.log.trace(`${LOG_PREFIX} Getting page detail`);
        this.log.debug(`${LOG_PREFIX} Current page detail value = ${JSON.stringify(this._state.page)}`);
        return this._state.page;
    }


    /**
     * Updates the currently active page detail and then triggers data transformation
     */
    set page(page: number) {
        this.log.trace(`${LOG_PREFIX} Setting page detail to ${JSON.stringify(page)}`);
        this._set({ page });
    }


    /**
     * Returns the currently set page size
     */
    get pageSize() {
        this.log.trace(`${LOG_PREFIX} Getting page size detail`);
        this.log.debug(`${LOG_PREFIX} Current page size detail = ${JSON.stringify(this._state.pageSize)}`);
        return this._state.pageSize;
    }


    /**
     * Updates the desired page size detail and then triggers data transformation
     */
    set pageSize(pageSize: number) {
        this.log.debug(`${LOG_PREFIX} Setting page size to ${JSON.stringify(pageSize)}`);
        this._set({ pageSize });
    }


    /**
     * Gets the currently entered search term
     */
    get searchTerm() {
        this.log.debug(`${LOG_PREFIX} Getting search term detail`);
        this.log.debug(`${LOG_PREFIX} Current search term detail = ${JSON.stringify(this._state.searchTerm)}`);
        return this._state.searchTerm;
    }


    /**
     * Updates the search term detail and then triggers data transformation
     */
    set searchTerm(searchTerm: string) {
        this.log.debug(`${LOG_PREFIX} Setting search term to ${JSON.stringify(searchTerm)}`);
        this._set({ searchTerm });
    }


    /**
     * Updates the sort column detail and then triggers data transformation
     */
    set sortColumn(sortColumn: string) {
        this.log.debug(`${LOG_PREFIX} Setting sort column to ${JSON.stringify(sortColumn)}`);
        this._set({ sortColumn });
    }


    /**
     * Updates the sort direction detail and then triggers data transformation
     */
    set sortDirection(sortDirection: SortDirection) {
        this.log.debug(`${LOG_PREFIX} Setting sort direction to ${JSON.stringify(sortDirection)}`);
        this._set({ sortDirection });
    }


    /**
     * Utility method for all the class setters.
     * Does the actual updating of details / transforming of data
     * @param patch the partially updated details
     */
    private _set(patch: Partial<State>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Flux Types records
        this._transform(this.fluxTypesDataService.records);

    }


    /**
     * Compares two values to find out if the first value preceeds the second.
     * When comparing string values, please note that this method is case sensitive
     * 
     * @param v1 The first value
     * @param v2 The second value
     * @returns -1 if v1 preceeds v2, 0 if v1 is equal to v2 or 1 if v1 is greater than v2
     */
    compare(v1: number | string | undefined | null, v2: number | string | undefined | null) {
        this.log.trace(`${LOG_PREFIX} Comparing two values to find out if the first value preceeds the second`);
        return (v1 == undefined || v1 == null || v2 == undefined || v2 == null) ? 0 : v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
    }


    /**
     * Sorts Flux Types Records
     * 
     * @param fluxTypes The Flux Types records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Flux Types records
     */
    sort(fluxTypes: FluxType[], column: string, direction: string): FluxType[] {
        this.log.trace(`${LOG_PREFIX} Sorting Flux Types records`);
        if (direction === '' || column == null) {
            return fluxTypes;
        } else {
            return [...fluxTypes].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in Flux Type record
     * 
     * @param fluxType The Flux Type record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(fluxType: FluxType, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in Flux Type record`);
        if (fluxType != null && fluxType != undefined) {

            // Try locating the search string in the Flux Type's name
            if (fluxType.name != null && fluxType.name != undefined) {
                if (fluxType.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Flux Type's description if not yet found
            if (fluxType.description != null && fluxType.description != undefined) {
                if (fluxType.description.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Flux Types Records
     * 
     * @param fluxTypes The Flux Types records to paginate
     * @returns The paginated Flux Types records
     */
    paginate(fluxTypes: FluxType[], page: number, pageSize: number): FluxType[] {
        this.log.trace(`${LOG_PREFIX} Paginating Flux Types records`);
        return fluxTypes.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Flux Types Records
     * 
     * @param fluxTypes The Flux Types records to sort
     * @returns The newly indexed Flux Types records
     */
    index(fluxTypes: FluxType[]): FluxType[] {
        this.log.trace(`${LOG_PREFIX} Indexing Flux Types records`);
        let pos: number = 0;
        return fluxTypes.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Flux Types records
     * 
     * @param records the original Flux Types records
     */
    private _transform(records: FluxType[]) {


        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Flux Types records`);

            const { sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Flag
            this._loadingSubject$.next(true);

            // Sort
            let transformed: FluxType[] = this.sort(records, sortColumn, sortDirection);

            // Filter
            transformed = transformed.filter(fluxType => this.matches(fluxType, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._fluxTypesSubject$.next(transformed);
            this._totalSubject$.next(total);

            // Flag
            this._loadingSubject$.next(false);

        } else {

            // Broadcast
            this._fluxTypesSubject$.next([]);
            this._totalSubject$.next(0);
        }

    }

}
