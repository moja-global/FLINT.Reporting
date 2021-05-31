import { HostListener, Injectable, OnDestroy, OnInit } from '@angular/core';
import { UnitsDataService } from './units-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { Unit } from '../models/unit.model';
import { Action } from 'rxjs/internal/scheduler/Action';
import { ActivatedRoute, Router } from '@angular/router';

const LOG_PREFIX: string = "[Units Records Tabulation Service]";

export interface UnitState extends State {
    unitCategoryId: number | null;
}

@Injectable({ providedIn: 'root' })
export class UnitsRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Units records are transformed as per the user defined search 
    // or sort criteria    
    private _unitsSubject$ = new BehaviorSubject<Unit[]>([]);
    private _units$ = this._unitsSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Units records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Units records should be displayed
    private _state: UnitState = { unitCategoryId: null, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private unitsDataService: UnitsDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.unitsDataService.units$
                .subscribe(
                    (units: Unit[]) => {
                        this._transform(units);
                    }));

    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing Units records that have been filtered as per the user defined criteria
     */
    get units$() {
        this.log.trace(`${LOG_PREFIX} Getting units$ observable`);
        this.log.debug(`${LOG_PREFIX} Current units$ observable value = ${JSON.stringify(this._unitsSubject$.value)}`);
        return this._units$;
    }


    /**
     * Returns an observable containing the total number of Units records that have been filtered as per the user defined criteria
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
     * Returns the currently set unit category id
     */
    get unitCategoryId() {
        this.log.trace(`${LOG_PREFIX} Getting unit category id detail`);
        this.log.debug(`${LOG_PREFIX} Current unit category id detail = ${JSON.stringify(this._state.unitCategoryId)}`);
        return this._state.unitCategoryId;
    }

    /**
     * Updates the desired unit category id detail and then triggers data transformation
     */
    set unitCategoryId(unitCategoryId: number | null) {
        this.log.debug(`${LOG_PREFIX} Setting unit category id to ${JSON.stringify(unitCategoryId)}`);
        this._set({ unitCategoryId });
    }

    /**
     * Utility method for all the class setters.
     * Does the actual updating of details / transforming of data
     * @param patch the partially updated details
     */
    private _set(patch: Partial<UnitState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Units records
        this._transform(this.unitsDataService.records);

    }

    /**
     * Filters unit records by category id
     * @param units 
     * @param unitCategoryId 
     * @returns 
     */
    filterByUnitCategory(units: Unit[], unitCategoryId: number | null): Unit[] {
        this.log.trace(`${LOG_PREFIX} Filtering Units records`);
        if (unitCategoryId == null) {
            return units;
        } else {
            return units.filter((u) => u.unitCategoryId == unitCategoryId);
        }
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
     * Sorts Units Records
     * 
     * @param units The Units records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Units records
     */
    sort(units: Unit[], column: string, direction: string): Unit[] {
        this.log.trace(`${LOG_PREFIX} Sorting Units records`);
        if (direction === '' || column == null) {
            return units;
        } else {
            return [...units].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Unit record
     * 
     * @param unit The Unit record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(unit: Unit, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in the Unit record`);
        if (unit != null && unit != undefined) {

            // Try locating the search string in the Unit's name
            if (unit.name != null && unit.name != undefined) {
                if (unit.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Unit's plural name
            if (unit.plural != null && unit.plural != undefined) {
                if (unit.plural.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Unit's symbol
            if (unit.symbol != null && unit.symbol != undefined) {
                if (unit.symbol.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Units Records
     * 
     * @param units The Units records to paginate
     * @returns The paginated Units records
     */
    paginate(units: Unit[], page: number, pageSize: number): Unit[] {
        this.log.trace(`${LOG_PREFIX} Paginating Units records`);
        return units.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Units Records
     * 
     * @param units The Units records to sort
     * @returns The newly indexed Units records
     */
    index(units: Unit[]): Unit[] {
        this.log.trace(`${LOG_PREFIX} Indexing Units records`);
        let pos: number = 0;
        return units.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Units records
     * 
     * @param records the original Units records
     */
    private _transform(records: Unit[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Units records`);

            const { unitCategoryId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Unit Category
            let transformed: Unit[] = this.filterByUnitCategory(records, unitCategoryId);

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);

            // Filter by Search Term
            transformed = transformed.filter(unit => this.matches(unit, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._unitsSubject$.next(transformed);
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._unitsSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
