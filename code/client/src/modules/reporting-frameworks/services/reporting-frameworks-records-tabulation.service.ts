import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { ReportingFrameworksDataService } from './reporting-frameworks-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { ReportingFramework } from '../models/reporting-framework.model';

const LOG_PREFIX: string = "[Reporting Frameworks Records Tabulation Service]";

@Injectable({ providedIn: 'root' })
export class ReportingFrameworksRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Reporting Frameworks records are transformed as per the user defined search 
    // or sort criteria    
    private _reportingFrameworksSubject$ = new BehaviorSubject<ReportingFramework[]>([]);
    private _reportingFrameworks$ = this._reportingFrameworksSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Reporting Frameworks records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Reporting Frameworks records should be displayed
    private _state: State = { page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private reportingFrameworksDataService: ReportingFrameworksDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.reportingFrameworksDataService.reportingFrameworks$
                .subscribe(
                    (reportingFrameworks: ReportingFramework[]) => {
                        this._transform(reportingFrameworks);
                    }));

    }
    
  ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    /**
     * Returns an observable containing Reporting Frameworks records that have been filtered as per the user defined criteria
     */
    get reportingFrameworks$() {
        this.log.trace(`${LOG_PREFIX} Getting reportingFrameworks$ observable`);
        this.log.debug(`${LOG_PREFIX} Current reportingFrameworks$ observable value = ${JSON.stringify(this._reportingFrameworksSubject$.value)}`);
        return this._reportingFrameworks$;
    }


    /**
     * Returns an observable containing the total number of Reporting Frameworks records that have been filtered as per the user defined criteria
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


        // Transform the Reporting Frameworks records
        this._transform(this.reportingFrameworksDataService.records);

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
     * Sorts Reporting Frameworks Records
     * 
     * @param reportingFrameworks The Reporting Frameworks records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Reporting Frameworks records
     */
    sort(reportingFrameworks: ReportingFramework[], column: string, direction: string): ReportingFramework[] {
        this.log.trace(`${LOG_PREFIX} Sorting Reporting Frameworks records`);
        if (direction === '' || column == null) {
            return reportingFrameworks;
        } else {
            return [...reportingFrameworks].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in Reporting Framework record
     * 
     * @param reportingFramework The Reporting Framework record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(reportingFramework: ReportingFramework, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in Reporting Framework record`);
        if (reportingFramework != null && reportingFramework != undefined) {

            // Try locating the search string in the Reporting Framework's name
            if (reportingFramework.name != null && reportingFramework.name != undefined) {
                if (reportingFramework.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Reporting Framework's description if not yet found
            if (reportingFramework.description != null && reportingFramework.description != undefined) {
                if (reportingFramework.description.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Reporting Frameworks Records
     * 
     * @param reportingFrameworks The Reporting Frameworks records to paginate
     * @returns The paginated Reporting Frameworks records
     */
    paginate(reportingFrameworks: ReportingFramework[], page: number, pageSize: number): ReportingFramework[] {
        this.log.trace(`${LOG_PREFIX} Paginating Reporting Frameworks records`);
        return reportingFrameworks.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Reporting Frameworks Records
     * 
     * @param reportingFrameworks The Reporting Frameworks records to sort
     * @returns The newly indexed Reporting Frameworks records
     */
    index(reportingFrameworks: ReportingFramework[]): ReportingFramework[] {
        this.log.trace(`${LOG_PREFIX} Indexing Reporting Frameworks records`);
        let pos: number = 0;
        return reportingFrameworks.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Reporting Frameworks records
     * 
     * @param records the original Reporting Frameworks records
     */
    private _transform(records: ReportingFramework[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Reporting Frameworks records`);

            const { sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Sort
            let transformed: ReportingFramework[] = this.sort(records, sortColumn, sortDirection);

            // Filter
            transformed = transformed.filter(reportingFramework => this.matches(reportingFramework, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._reportingFrameworksSubject$.next(transformed);
            this._totalSubject$.next(total);

        } else {

            // Broadcast
            this._reportingFrameworksSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
