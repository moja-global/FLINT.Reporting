import { Injectable, OnDestroy } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { ReportingTable } from '../models/reporting-table.model';
import { ReportingTablesDataService } from './reporting-tables-data.service';

const LOG_PREFIX: string = "[Reporting Tables Records Tabulation Service]";

export interface ReportingTableState extends State {
    reportingFrameworkId: number | null;
}

@Injectable({ providedIn: 'root' })
export class ReportingTablesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Reporting Tables records are transformed as per the user defined search 
    // or sort criteria    
    private _reportingTablesSubject$ = new BehaviorSubject<ReportingTable[]>([]);
    private _reportingTables$ = this._reportingTablesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Reporting Tables records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Reporting Tables records should be displayed
    private _state: ReportingTableState = { reportingFrameworkId: null, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private reportingTablesDataService: ReportingTablesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.reportingTablesDataService.reportingTables$
                .subscribe(
                    (reportingTables: ReportingTable[]) => {
                        this._transform(reportingTables);
                    }));

    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing Reporting Tables records that have been filtered as per the user defined criteria
     */
    get reportingTables$() {
        this.log.trace(`${LOG_PREFIX} Getting reportingTables$ observable`);
        this.log.debug(`${LOG_PREFIX} Current reportingTables$ observable value = ${JSON.stringify(this._reportingTablesSubject$.value)}`);
        return this._reportingTables$;
    }


    /**
     * Returns an observable containing the total number of Reporting Tables records that have been filtered as per the user defined criteria
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
    private _set(patch: Partial<ReportingTableState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Reporting Tables records
        this._transform(this.reportingTablesDataService.records);

    }

    /**
     * Filters reportingTable records by category id
     * @param reportingTables 
     * @param reportingFrameworkId 
     * @returns 
     */
    filterByReportingFramework(reportingTables: ReportingTable[], reportingFrameworkId: number | null): ReportingTable[] {
        this.log.trace(`${LOG_PREFIX} Filtering Reporting Tables records`);
        if (reportingFrameworkId == null) {
            return reportingTables;
        } else {
            return reportingTables.filter((u) => u.reportingFrameworkId == reportingFrameworkId);
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
     * Sorts Reporting Tables Records
     * 
     * @param reportingTables The Reporting Tables records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Reporting Tables records
     */
    sort(reportingTables: ReportingTable[], column: string, direction: string): ReportingTable[] {
        this.log.trace(`${LOG_PREFIX} Sorting Reporting Tables records`);
        if (direction === '' || column == null) {
            return reportingTables;
        } else {
            return [...reportingTables].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Reporting Table record
     * 
     * @param reportingTable The Reporting Table record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(reportingTable: ReportingTable, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in the Reporting Table record`);
        if (reportingTable != null && reportingTable != undefined) {

            // Try locating the search string in the Reporting Table's name
            if (reportingTable.name != null && reportingTable.name != undefined) {
                if (reportingTable.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Reporting Table's number
            if (reportingTable.number != null && reportingTable.number != undefined) {
                if (reportingTable.number.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }            

            // Try locating the search string in the Reporting Table's description
            if (reportingTable.description != null && reportingTable.description != undefined) {
                if (reportingTable.description.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Reporting Tables Records
     * 
     * @param reportingTables The Reporting Tables records to paginate
     * @returns The paginated Reporting Tables records
     */
    paginate(reportingTables: ReportingTable[], page: number, pageSize: number): ReportingTable[] {
        this.log.trace(`${LOG_PREFIX} Paginating Reporting Tables records`);
        return reportingTables.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Reporting Tables Records
     * 
     * @param reportingTables The Reporting Tables records to sort
     * @returns The newly indexed Reporting Tables records
     */
    index(reportingTables: ReportingTable[]): ReportingTable[] {
        this.log.trace(`${LOG_PREFIX} Indexing Reporting Tables records`);
        let pos: number = 0;
        return reportingTables.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Reporting Tables records
     * 
     * @param records the original Reporting Tables records
     */
    private _transform(records: ReportingTable[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Reporting Tables records`);

            const { reportingFrameworkId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Reporting Framework
            let transformed: ReportingTable[] = this.filterByReportingFramework(records, reportingFrameworkId);

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);

            // Filter by Search Term
            transformed = transformed.filter(reportingTable => this.matches(reportingTable, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._reportingTablesSubject$.next(transformed);
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._reportingTablesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
