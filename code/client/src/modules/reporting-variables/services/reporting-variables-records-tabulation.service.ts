import { Injectable, OnDestroy } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { ReportingVariable } from '../models/reporting-variable.model';
import { ReportingVariablesDataService } from './reporting-variables-data.service';

const LOG_PREFIX: string = "[Reporting Variables Records Tabulation Service]";

export interface ReportingVariableState extends State {
    reportingFrameworkId: number | null;
}

@Injectable({ providedIn: 'root' })
export class ReportingVariablesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Reporting Variables records are transformed as per the user defined search 
    // or sort criteria    
    private _reportingVariablesSubject$ = new BehaviorSubject<ReportingVariable[]>([]);
    private _reportingVariables$ = this._reportingVariablesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Reporting Variables records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Reporting Variables records should be displayed
    private _state: ReportingVariableState = { reportingFrameworkId: null, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private reportingVariablesDataService: ReportingVariablesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.reportingVariablesDataService.reportingVariables$
                .subscribe(
                    (reportingVariables: ReportingVariable[]) => {
                        this._transform(reportingVariables);
                    }));

    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing Reporting Variables records that have been filtered as per the user defined criteria
     */
    get reportingVariables$() {
        this.log.trace(`${LOG_PREFIX} Getting reportingVariables$ observable`);
        this.log.debug(`${LOG_PREFIX} Current reportingVariables$ observable value = ${JSON.stringify(this._reportingVariablesSubject$.value)}`);
        return this._reportingVariables$;
    }


    /**
     * Returns an observable containing the total number of Reporting Variables records that have been filtered as per the user defined criteria
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
     * Returns the currently set Reporting Framework id
     */
     get reportingFrameworkId() {
        this.log.trace(`${LOG_PREFIX} Getting Reporting Framework id detail`);
        this.log.debug(`${LOG_PREFIX} Current Reporting Framework id detail = ${JSON.stringify(this._state.reportingFrameworkId)}`);
        return this._state.reportingFrameworkId;
    }

    /**
     * Updates the desired Reporting Framework id detail and then triggers data transformation
     */
    set reportingFrameworkId(reportingFrameworkId: number | null) {
        this.log.debug(`${LOG_PREFIX} Setting Reporting Framework id to ${JSON.stringify(reportingFrameworkId)}`);
        this._set({ reportingFrameworkId });
    }

    /**
     * Utility method for all the class setters.
     * Does the actual updating of details / transforming of data
     * @param patch the partially updated details
     */
    private _set(patch: Partial<ReportingVariableState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Reporting Variables records
        this._transform(this.reportingVariablesDataService.records);

    }

    /**
     * Filters reportingVariable records by category id
     * @param reportingVariables 
     * @param reportingFrameworkId 
     * @returns 
     */
    filterByReportingFramework(reportingVariables: ReportingVariable[], reportingFrameworkId: number | null): ReportingVariable[] {
        this.log.trace(`${LOG_PREFIX} Filtering Reporting Variables records`);
        if (reportingFrameworkId == null) {
            return reportingVariables;
        } else {
            return reportingVariables.filter((u) => u.reportingFrameworkId == reportingFrameworkId);
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
     * Sorts Reporting Variables Records
     * 
     * @param reportingVariables The Reporting Variables records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Reporting Variables records
     */
    sort(reportingVariables: ReportingVariable[], column: string, direction: string): ReportingVariable[] {
        this.log.trace(`${LOG_PREFIX} Sorting Reporting Variables records`);
        if (direction === '' || column == null) {
            return reportingVariables;
        } else {
            return [...reportingVariables].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Reporting Variable record
     * 
     * @param reportingVariable The Reporting Variable record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(reportingVariable: ReportingVariable, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in the Reporting Variable record`);
        if (reportingVariable != null && reportingVariable != undefined) {

            // Try locating the search string in the Reporting Variable's name
            if (reportingVariable.name != null && reportingVariable.name != undefined) {
                if (reportingVariable.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Reporting Variable's description
            if (reportingVariable.description != null && reportingVariable.description != undefined) {
                if (reportingVariable.description.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Reporting Variables Records
     * 
     * @param reportingVariables The Reporting Variables records to paginate
     * @returns The paginated Reporting Variables records
     */
    paginate(reportingVariables: ReportingVariable[], page: number, pageSize: number): ReportingVariable[] {
        this.log.trace(`${LOG_PREFIX} Paginating Reporting Variables records`);
        return reportingVariables.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Reporting Variables Records
     * 
     * @param reportingVariables The Reporting Variables records to sort
     * @returns The newly indexed Reporting Variables records
     */
    index(reportingVariables: ReportingVariable[]): ReportingVariable[] {
        this.log.trace(`${LOG_PREFIX} Indexing Reporting Variables records`);
        let pos: number = 0;
        return reportingVariables.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Reporting Variables records
     * 
     * @param records the original Reporting Variables records
     */
    private _transform(records: ReportingVariable[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Reporting Variables records`);

            const { reportingFrameworkId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Reporting Variable Framework
            let transformed: ReportingVariable[] = this.filterByReportingFramework(records, reportingFrameworkId);

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);

            // Filter by Search Term
            transformed = transformed.filter(reportingVariable => this.matches(reportingVariable, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._reportingVariablesSubject$.next(transformed);
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._reportingVariablesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
