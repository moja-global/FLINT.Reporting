import { Injectable, OnDestroy } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { LandUseSubcategory } from '../models/land-use-subcategory.model';
import { LandUseSubcategoriesDataService } from './land-use-subcategories-data.service';

const LOG_PREFIX: string = "[Land Use Subcategories Records Tabulation Service]";

export interface LandUseSubcategoryState extends State {
    reportingFrameworkId: number | null;
    parentLandUseCategoryId: number | null;
}

@Injectable({ providedIn: 'root' })
export class LandUseSubcategoriesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Land Use Subcategories records are transformed as per the user defined search 
    // or sort criteria    
    private _landUseSubcategoriesSubject$ = new BehaviorSubject<LandUseSubcategory[]>([]);
    private _landUseSubcategories$ = this._landUseSubcategoriesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Land Use Subcategories records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Land Use Subcategories records should be displayed
    private _state: LandUseSubcategoryState = { reportingFrameworkId: null, parentLandUseCategoryId: 0, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private landUseSubcategoriesDataService: LandUseSubcategoriesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.landUseSubcategoriesDataService.landUseSubcategories$
                .subscribe(
                    (landUseSubcategories: LandUseSubcategory[]) => {
                        this._transform(landUseSubcategories);
                    }));

    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing Land Use Subcategories records that have been filtered as per the user defined criteria
     */
    get landUseSubcategories$() {
        this.log.trace(`${LOG_PREFIX} Getting landUseSubcategories$ observable`);
        this.log.debug(`${LOG_PREFIX} Current landUseSubcategories$ observable value = ${JSON.stringify(this._landUseSubcategoriesSubject$.value)}`);
        return this._landUseSubcategories$;
    }


    /**
     * Returns an observable containing the total number of Land Use Subcategories records that have been filtered as per the user defined criteria
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
     * Returns the currently set Parent Land Use Category id
     */
     get parentLandUseCategoryId() {
        this.log.trace(`${LOG_PREFIX} Getting Parent Land Use Category id detail`);
        this.log.debug(`${LOG_PREFIX} Current Parent Land Use Category id detail = ${JSON.stringify(this._state.parentLandUseCategoryId)}`);
        return this._state.parentLandUseCategoryId;
    }

    /**
     * Updates the desired Parent Land Use Category id detail and then triggers data transformation
     */
    set parentLandUseCategoryId(parentLandUseCategoryId: number | null) {
        this.log.debug(`${LOG_PREFIX} Setting Parent Land Use Category id to ${JSON.stringify(parentLandUseCategoryId)}`);
        this._set({ parentLandUseCategoryId });
    }    

    /**
     * Utility method for all the class setters.
     * Does the actual updating of details / transforming of data
     * @param patch the partially updated details
     */
    private _set(patch: Partial<LandUseSubcategoryState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Land Use Subcategories records
        this._transform(this.landUseSubcategoriesDataService.records);

    }

    /**
     * Filters Land Use Subcategories records by Reporting Framework Id
     * @param landUseSubcategories 
     * @param reportingFrameworkId 
     * @returns 
     */
    filterByReportingFramework(landUseSubcategories: LandUseSubcategory[], reportingFrameworkId: number | null): LandUseSubcategory[] {
        this.log.trace(`${LOG_PREFIX} Filtering Land Use Subcategories records`);
        if (reportingFrameworkId == null) {
            return landUseSubcategories;
        } else {
            return landUseSubcategories.filter((u) => u.reportingFrameworkId == reportingFrameworkId);
        }
    }


    /**
     * Filters Land Use Subcategories records by parent Land Use Subcategory id
     * @param landUseSubcategories 
     * @param parentLandUseCategoryId 
     * @returns 
     */
     filterByParentLandUseSubcategory(landUseSubcategories: LandUseSubcategory[], parentLandUseCategoryId: number | null): LandUseSubcategory[] {
        this.log.trace(`${LOG_PREFIX} Filtering Land Use Subcategories records`);
        return landUseSubcategories.filter((u) => u.parentLandUseCategoryId == parentLandUseCategoryId);
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
     * Sorts Land Use Subcategories Records
     * 
     * @param landUseSubcategories The Land Use Subcategories records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Land Use Subcategories records
     */
    sort(landUseSubcategories: LandUseSubcategory[], column: string, direction: string): LandUseSubcategory[] {
        this.log.trace(`${LOG_PREFIX} Sorting Land Use Subcategories records`);
        if (direction === '' || column == null) {
            return landUseSubcategories;
        } else {
            return [...landUseSubcategories].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Land Use Subcategory record
     * 
     * @param landUseSubcategory The Land Use Subcategory record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(landUseSubcategory: LandUseSubcategory, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in the Land Use Subcategory record`);
        if (landUseSubcategory != null && landUseSubcategory != undefined) {

            // Try locating the search string in the Land Use Subcategory's name
            if (landUseSubcategory.name != null && landUseSubcategory.name != undefined) {
                if (landUseSubcategory.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

        }

        return false;
    }


    /**
     * Paginates Land Use Subcategories Records
     * 
     * @param landUseSubcategories The Land Use Subcategories records to paginate
     * @returns The paginated Land Use Subcategories records
     */
    paginate(landUseSubcategories: LandUseSubcategory[], page: number, pageSize: number): LandUseSubcategory[] {
        this.log.trace(`${LOG_PREFIX} Paginating Land Use Subcategories records`);
        return landUseSubcategories.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Land Use Subcategories Records
     * 
     * @param landUseSubcategories The Land Use Subcategories records to sort
     * @returns The newly indexed Land Use Subcategories records
     */
    index(landUseSubcategories: LandUseSubcategory[]): LandUseSubcategory[] {
        this.log.trace(`${LOG_PREFIX} Indexing Land Use Subcategories records`);
        let pos: number = 0;
        return landUseSubcategories.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Land Use Subcategories records
     * 
     * @param records the original Land Use Subcategories records
     */
    private _transform(records: LandUseSubcategory[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Land Use Subcategories records`);

            const { reportingFrameworkId, parentLandUseCategoryId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Reporting Framework
            let transformed: LandUseSubcategory[] = this.filterByReportingFramework(records, reportingFrameworkId);

            // Filters by parent Land Use Subcategory id
            transformed = this.filterByParentLandUseSubcategory(transformed, parentLandUseCategoryId);

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);

            // Filter by Search Term
            transformed = transformed.filter(landUseSubcategory => this.matches(landUseSubcategory, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._landUseSubcategoriesSubject$.next(transformed);
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._landUseSubcategoriesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
