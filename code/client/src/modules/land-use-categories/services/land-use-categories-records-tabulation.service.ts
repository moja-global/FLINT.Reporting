import { Injectable, OnDestroy } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { LandUseCategory } from '../models/land-use-category.model';
import { LandUseCategoriesDataService } from './land-use-categories-data.service';

const LOG_PREFIX: string = "[Land Use Categories Records Tabulation Service]";

export interface LandUseCategoryState extends State {
    reportingFrameworkId: number | null;
    parentLandUseCategoryId: number | null;
}

@Injectable({ providedIn: 'root' })
export class LandUseCategoriesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Land Use Categories records are transformed as per the user defined search 
    // or sort criteria    
    private _landUseCategoriesSubject$ = new BehaviorSubject<LandUseCategory[]>([]);
    private _landUseCategories$ = this._landUseCategoriesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Land Use Categories records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Land Use Categories records should be displayed
    private _state: LandUseCategoryState = { reportingFrameworkId: null, parentLandUseCategoryId: 0, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private landUseCategoriesDataService: LandUseCategoriesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.landUseCategoriesDataService.landUseCategories$
                .subscribe(
                    (landUseCategories: LandUseCategory[]) => {
                        this._transform(landUseCategories);
                    }));

    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing Land Use Categories records that have been filtered as per the user defined criteria
     */
    get landUseCategories$() {
        this.log.trace(`${LOG_PREFIX} Getting landUseCategories$ observable`);
        this.log.debug(`${LOG_PREFIX} Current landUseCategories$ observable value = ${JSON.stringify(this._landUseCategoriesSubject$.value)}`);
        return this._landUseCategories$;
    }


    /**
     * Returns an observable containing the total number of Land Use Categories records that have been filtered as per the user defined criteria
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
    private _set(patch: Partial<LandUseCategoryState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Land Use Categories records
        this._transform(this.landUseCategoriesDataService.records);

    }

    /**
     * Filters Land Use Categories records by Reporting Framework Id
     * @param landUseCategories 
     * @param reportingFrameworkId 
     * @returns 
     */
    filterByReportingFramework(landUseCategories: LandUseCategory[], reportingFrameworkId: number | null): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Filtering Land Use Categories records`);
        if (reportingFrameworkId == null) {
            return landUseCategories;
        } else {
            return landUseCategories.filter((u) => u.reportingFrameworkId == reportingFrameworkId);
        }
    }


    /**
     * Filters Land Use Categories records by parent Land Use Category id
     * @param landUseCategories 
     * @param parentLandUseCategoryId 
     * @returns 
     */
    filterByParentLandUseCategory(landUseCategories: LandUseCategory[], parentLandUseCategoryId: number | null): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Filtering Land Use Categories records`);
        return landUseCategories.filter((u) => u.parentLandUseCategoryId == parentLandUseCategoryId);
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
     * Sorts Land Use Categories Records
     * 
     * @param landUseCategories The Land Use Categories records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Land Use Categories records
     */
    sort(landUseCategories: LandUseCategory[], column: string, direction: string): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Sorting Land Use Categories records`);
        if (direction === '' || column == null) {
            return landUseCategories;
        } else {
            return [...landUseCategories].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Land Use Category record
     * 
     * @param landUseCategory The Land Use Category record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(landUseCategory: LandUseCategory, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in the Land Use Category record`);
        if (landUseCategory != null && landUseCategory != undefined) {

            // Try locating the search string in the Land Use Category's name
            if (landUseCategory.name != null && landUseCategory.name != undefined) {
                if (landUseCategory.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

        }

        return false;
    }


    /**
     * Paginates Land Use Categories Records
     * 
     * @param landUseCategories The Land Use Categories records to paginate
     * @returns The paginated Land Use Categories records
     */
    paginate(landUseCategories: LandUseCategory[], page: number, pageSize: number): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Paginating Land Use Categories records`);
        return landUseCategories.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Land Use Categories Records
     * 
     * @param landUseCategories The Land Use Categories records to sort
     * @returns The newly indexed Land Use Categories records
     */
    index(landUseCategories: LandUseCategory[]): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Indexing Land Use Categories records`);
        let pos: number = 0;
        return landUseCategories.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Land Use Categories records
     * 
     * @param records the original Land Use Categories records
     */
    private _transform(records: LandUseCategory[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Land Use Categories records`);

            const { reportingFrameworkId, parentLandUseCategoryId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Reporting Framework
            let transformed: LandUseCategory[] = this.filterByReportingFramework(records, reportingFrameworkId);


            // Filters by parent Land Use Category id
            transformed = this.filterByParentLandUseCategory(transformed, parentLandUseCategoryId);

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);

            // Filter by Search Term
            transformed = transformed.filter(landUseCategory => this.matches(landUseCategory, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._landUseCategoriesSubject$.next(transformed);
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._landUseCategoriesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
