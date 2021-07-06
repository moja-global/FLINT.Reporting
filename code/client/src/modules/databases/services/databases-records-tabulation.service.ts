import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { DatabasesDataService } from './databases-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { Database } from '../models/database.model';

const LOG_PREFIX: string = "[Databases Records Tabulation Service]";

@Injectable({ providedIn: 'root' })
export class DatabasesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Databases records are transformed as per the user defined search 
    // or sort criteria    
    private _databasesSubject$ = new BehaviorSubject<Database[]>([]);
    private _databases$ = this._databasesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Databases records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Databases records should be displayed
    private _state: State = { page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private databasesDataService: DatabasesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.databasesDataService.databases$
                .subscribe(
                    (databases: Database[]) => {
                        this._transform(databases);
                    }));

    }

  ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    /**
     * Returns an observable containing Databases records that have been filtered as per the user defined criteria
     */
    get databases$() {
        this.log.trace(`${LOG_PREFIX} Getting databases$ observable`);
        this.log.debug(`${LOG_PREFIX} Current databases$ observable value = ${JSON.stringify(this._databasesSubject$.value)}`);
        return this._databases$;
    }


    /**
     * Returns an observable containing the total number of Databases records that have been filtered as per the user defined criteria
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


        // Transform the Databases records
        this._transform(this.databasesDataService.records);

    }


    /**
     * Compares two values to find out if the first value preceeds the second.
     * When comparing string values, please note that this method is case sensitive
     * 
     * @param v1 The first value
     * @param v2 The second value
     * @returns -1 if v1 preceeds v2, 0 if v1 is equal to v2 or 1 if v1 is greater than v2
     */
    compare(v1: string | number | boolean | undefined | null, v2: string | number | boolean | undefined | null) {
        this.log.trace(`${LOG_PREFIX} Comparing two values to find out if the first value preceeds the second`);
        return (v1 == undefined || v1 == null || v2 == undefined || v2 == null) ? 0 : v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
    }


    /**
     * Sorts Databases Records
     * 
     * @param databases The Databases records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Databases records
     */
    sort(databases: Database[], column: string, direction: string): Database[] {
        this.log.trace(`${LOG_PREFIX} Sorting Databases records`);
        if (direction === '' || column == null) {
            return databases;
        } else {
            return [...databases].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Database record
     * 
     * @param database The Database record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(database: Database, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in the Database record`);
        if (database != null && database != undefined) {

            // Try locating the search string in the Database's name
            if (database.label != null && database.label != undefined) {
                if (database.label.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Databases Records
     * 
     * @param databases The Databases records to paginate
     * @returns The paginated Databases records
     */
    paginate(databases: Database[], page: number, pageSize: number): Database[] {
        this.log.trace(`${LOG_PREFIX} Paginating Databases records`);
        return databases.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Databases Records
     * 
     * @param databases The Databases records to sort
     * @returns The newly indexed Databases records
     */
    index(databases: Database[]): Database[] {
        this.log.trace(`${LOG_PREFIX} Indexing Databases records`);
        let pos: number = 0;
        return databases.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Databases records
     * 
     * @param records the original Databases records
     */
    private _transform(records: Database[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Databases records`);

            const { sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Sort
            let transformed: Database[] = this.sort(records, sortColumn, sortDirection);

            // Filter
            transformed = transformed.filter(database => this.matches(database, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._databasesSubject$.next(transformed);
            this._totalSubject$.next(total);

        } else {

            // Broadcast
            this._databasesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
