import { Injectable, OnDestroy } from '@angular/core';
import { EmissionsTypesDataService } from './emissions-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { EmissionType } from '../models/emission-type.model';

const LOG_PREFIX: string = "[Emissions Types Records Tabulation Service]";

export interface EmissionTypeState extends State {

}

@Injectable({ providedIn: 'root' })
export class EmissionsTypesRecordsTabulationService implements OnDestroy {

    // The user defined search or sort criteria.
    private _state: EmissionTypeState = { page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // The first set of observables that will be updated / broadcasted whenever 
    // a background task is started or completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(false);
    private _loading$ = this._loadingSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // the user enters a search term or specifies a sort criteria    
    private _emissionsTypesSubject$ = new BehaviorSubject<EmissionType[]>([]);
    private _emissionsTypes$ = this._emissionsTypesSubject$.asObservable();

    // The third set of observables that will be updated / broadcasted whenever 
    // the user enters a search term or specifies a sort criteria 
    // and Emissions Types Records as a result
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();
  

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private emissionsTypesDataService: EmissionsTypesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.emissionsTypesDataService.emissionsTypes$
                .subscribe(
                    (emissionsTypes: EmissionType[]) => {
                        this.transform(emissionsTypes);
                    }));

    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing Emissions Types Records that have been filtered as per the Current User Defined Criteria
     */
    get emissionsTypes$() {
        this.log.trace(`${LOG_PREFIX} Getting emissionsTypes$ observable`);
        this.log.debug(`${LOG_PREFIX} Current emissionsTypes$ observable value = ${JSON.stringify(this._emissionsTypesSubject$.value)}`);
        return this._emissionsTypes$;
    }


    /**
     * Returns an observable containing the total number of Emissions Types Records that have been filtered as per the Current User Defined Criteria
     */
    get total$() {
        this.log.trace(`${LOG_PREFIX} Getting total$ observable`);
        this.log.debug(`${LOG_PREFIX} Current total$ observable value = ${JSON.stringify(this._totalSubject$.value)}`);
        return this._total$;
    }


    /**
     * Returns an observable indicating whether or not a data operation exercise (sorting, searching etc.) is currently underway
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
     * Updates the currently set active page detail and triggers the data transformation exercise
     */
    set page(page: number) {
        this.log.trace(`${LOG_PREFIX} Setting page detail to ${JSON.stringify(page)}`);
        this.set({ page });
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
     * Updates the currently set page size detail and triggers the data transformation exercise
     */
    set pageSize(pageSize: number) {
        this.log.debug(`${LOG_PREFIX} Setting page size to ${JSON.stringify(pageSize)}`);
        this.set({ pageSize });
    }


    /**
     * Returns the currently set search term
     */
    get searchTerm() {
        this.log.debug(`${LOG_PREFIX} Getting search term detail`);
        this.log.debug(`${LOG_PREFIX} Current search term detail = ${JSON.stringify(this._state.searchTerm)}`);
        return this._state.searchTerm;
    }


    /**
     * Updates the currently set search term and triggers the data transformation exercise
     */
    set searchTerm(searchTerm: string) {
        this.log.debug(`${LOG_PREFIX} Setting search term to ${JSON.stringify(searchTerm)}`);
        this.set({ searchTerm });
    }


    /**
     * Updates the currently set sort column detail and triggers the data transformation exercise
     */
    set sortColumn(sortColumn: string) {
        this.log.debug(`${LOG_PREFIX} Setting sort column to ${JSON.stringify(sortColumn)}`);
        this.set({ sortColumn });
    }


    /**
     * Updates the currently set sort direction detail
     */
    set sortDirection(sortDirection: SortDirection) {
        this.log.debug(`${LOG_PREFIX} Setting sort direction to ${JSON.stringify(sortDirection)}`);
        this.set({ sortDirection });
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
     * Sorts Emissions Types Records
     * 
     * @param emissionsTypes The Emissions Types Records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Emissions Types Records
     */
    sort(emissionsTypes: EmissionType[], column: string, direction: string): EmissionType[] {
        this.log.trace(`${LOG_PREFIX} Sorting Emissions Types Records`);
        if (direction === '' || column == null) {
            return emissionsTypes;
        } else {
            return [...emissionsTypes].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Emission Type record
     * 
     * @param emissionType The Emission Type record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(emissionType: EmissionType, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if the search string is present in the Emission Type record`);
        if (emissionType != null && emissionType != undefined) {

            // Try locating the search string in the Emission Type's name
            if (emissionType.name != null && emissionType.name != undefined) {
                if (emissionType.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Emission Type's abbreviation
            if (emissionType.abbreviation != null && emissionType.abbreviation != undefined) {
                if (emissionType.abbreviation.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
            
            // Try locating the search string in the Emission Type's description
            if (emissionType.description != null && emissionType.description != undefined) {
                if (emissionType.description.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }            
        }

        return false;
    }


    /**
     * Updates the index of the Emissions Types Records
     * 
     * @param emissionsTypes The Emissions Types Records to sort
     * @returns The newly indexed Emissions Types Records
     */
     index(emissionsTypes: EmissionType[]): EmissionType[] {
        this.log.trace(`${LOG_PREFIX} Indexing Emissions Types Records`);
        let pos: number = 0;
        return emissionsTypes.map(d => {
            d.pos = ++pos;
            return d;
        });
    }    


    /**
     * Paginates Emissions Types Records
     * 
     * @param emissionsTypes The Emissions Types Records to paginate
     * @returns The paginated Emissions Types Records
     */
    paginate(emissionsTypes: EmissionType[], page: number, pageSize: number): EmissionType[] {
        this.log.trace(`${LOG_PREFIX} Paginating Emissions Types Records`);
        return emissionsTypes.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }




    /**
     * Utility method for all the class setters.
     * Updates the sort / filter criteria and triggers the data transformation exercise
     * @param patch the partially updated details
     */
    set(patch: Partial<EmissionTypeState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Emissions Types Records
        this.transform(this.emissionsTypesDataService.records);

    }


    /**
     * Sorts, filters and paginates Emissions Types Records
     * 
     * @param records the original Emissions Types Records
     */
    transform(records: EmissionType[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Emissions Types Records`);
            this.log.debug(`${LOG_PREFIX} Emissions Types Records before transformation = ${JSON.stringify(records)}`);

            const { sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Sort
            let transformed: EmissionType[] = this.sort(records, sortColumn, sortDirection);
            this.log.debug(`${LOG_PREFIX} Emissions Types Records after 'Sort' Transformation = ${JSON.stringify(transformed)}`);

            // Filter by Search Term
            transformed = transformed.filter(emissionType => this.matches(emissionType, searchTerm));
            const total: number = transformed.length;
            this.log.debug(`${LOG_PREFIX} Emissions Types Records after 'Filter by Search Term' Transformation = ${JSON.stringify(transformed)}`);

            // Index
            transformed = this.index(transformed);
            this.log.debug(`${LOG_PREFIX} Emissions Types Records after 'Index' Transformation = ${JSON.stringify(transformed)}`);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);
            this.log.debug(`${LOG_PREFIX} Emissions Types Records after 'Paginate' Transformation = ${JSON.stringify(transformed)}`);

            // Broadcast
            this._emissionsTypesSubject$.next(Object.assign([],transformed));
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._emissionsTypesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
