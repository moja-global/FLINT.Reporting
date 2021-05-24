import { Injectable, OnInit, OnDestroy } from "@angular/core";
import { SortDirection } from "@common/directives";
import { State } from "@common/models";
import { NGXLogger } from "ngx-logger";
import { BehaviorSubject, Subscription } from "rxjs";
import { EmissionTypesDataService } from "./emission-types-data.service";
import { EmissionType } from "../models/emission-type.model";


const LOG_PREFIX: string = "[Emission Types Records Tabulation Service]";

@Injectable({ providedIn: 'root' })
export class EmissionTypesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Emission Types records are transformed as per the user defined search 
    // or sort criteria    
    private _emissionTypesSubject$ = new BehaviorSubject<EmissionType[]>([]);
    private _emissionTypes$ = this._emissionTypesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Emission Types records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Emission Types records should be displayed
    private _state: State = { page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private emissionTypesDataService: EmissionTypesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.emissionTypesDataService.emissionTypes$
                .subscribe(
                    (emissionTypes: EmissionType[]) => {
                        this._transform(emissionTypes);
                    }));

    }

    ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    /**
     * Returns an observable containing Emission Types records that have been filtered as per the user defined criteria
     */
    get emissionTypes$() {
        this.log.trace(`${LOG_PREFIX} Getting emissionTypes$ observable`);
        this.log.debug(`${LOG_PREFIX} Current emissionTypes$ observable value = ${JSON.stringify(this._emissionTypesSubject$.value)}`);
        return this._emissionTypes$;
    }


    /**
     * Returns an observable containing the total number of Emission Types records that have been filtered as per the user defined criteria
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


        // Transform the Emission Types records
        this._transform(this.emissionTypesDataService.records);

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
     * Sorts Emission Types Records
     * 
     * @param emissionTypes The Emission Types records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Emission Types records
     */
    sort(emissionTypes: EmissionType[], column: string, direction: string): EmissionType[] {
        this.log.trace(`${LOG_PREFIX} Sorting Emission Types records`);
        if (direction === '' || column == null) {
            return emissionTypes;
        } else {
            return [...emissionTypes].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in Emission Type record
     * 
     * @param emissionType The Emission Type record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(emissionType: EmissionType, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in Emission Type record`);
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

            // Try locating the search string in the Emission Type's description if not yet found
            if (emissionType.description != null && emissionType.description != undefined) {
                if (emissionType.description.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Emission Types Records
     * 
     * @param emissionTypes The Emission Types records to paginate
     * @returns The paginated Emission Types records
     */
    paginate(emissionTypes: EmissionType[], page: number, pageSize: number): EmissionType[] {
        this.log.trace(`${LOG_PREFIX} Paginating Emission Types records`);
        return emissionTypes.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Emission Types Records
     * 
     * @param emissionTypes The Emission Types records to sort
     * @returns The newly indexed Emission Types records
     */
    index(emissionTypes: EmissionType[]): EmissionType[] {
        this.log.trace(`${LOG_PREFIX} Indexing Emission Types records`);
        let pos: number = 0;
        return emissionTypes.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Emission Types records
     * 
     * @param records the original Emission Types records
     */
    private _transform(records: EmissionType[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Emission Types records`);

            const { sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Sort
            let transformed: EmissionType[] = this.sort(records, sortColumn, sortDirection);

            // Filter
            transformed = transformed.filter(emissionType => this.matches(emissionType, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._emissionTypesSubject$.next(transformed);
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._emissionTypesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
