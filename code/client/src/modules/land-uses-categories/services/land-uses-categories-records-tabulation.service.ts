import { Injectable, OnDestroy } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { LandUseCategory } from '../models/land-use-category.model';
import { LandUsesCategoriesDataService } from './land-uses-categories-data.service';

const LOG_PREFIX: string = "[Land Uses Categories Records Tabulation Service]";

export interface LandUseCategoryState extends State {
    reportingFrameworkId: number | null;
}

@Injectable({ providedIn: 'root' })
export class LandUsesCategoriesRecordsTabulationService implements OnDestroy {

    // The user defined search or sort criteria.
    private _state: LandUseCategoryState = { reportingFrameworkId: null, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // The first set of observables that will be updated / broadcasted whenever 
    // a background task is started or completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(false);
    private _loading$ = this._loadingSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // the user enters a search term or specifies a sort criteria    
    private _landUsesCategoriesSubject$ = new BehaviorSubject<LandUseCategory[]>([]);
    private _landUsesCategories$ = this._landUsesCategoriesSubject$.asObservable();

    // The third set of observables that will be updated / broadcasted whenever 
    // the user enters a search term or specifies a sort criteria 
    // and Land Uses Categories Records as a result
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();
  

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private landUsesCategoriesDataService: LandUsesCategoriesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.landUsesCategoriesDataService.landUsesCategories$
                .subscribe(
                    (landUsesCategories: LandUseCategory[]) => {
                        this.transform(landUsesCategories);
                    }));

    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing Land Uses Categories Records that have been filtered as per the Current User Defined Criteria
     */
    get landUsesCategories$() {
        this.log.trace(`${LOG_PREFIX} Getting landUsesCategories$ observable`);
        this.log.debug(`${LOG_PREFIX} Current landUsesCategories$ observable value = ${JSON.stringify(this._landUsesCategoriesSubject$.value)}`);
        return this._landUsesCategories$;
    }


    /**
     * Returns an observable containing the total number of Land Uses Categories Records that have been filtered as per the Current User Defined Criteria
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

        if(this._state.page != page) {
            this.log.trace(`${LOG_PREFIX} Setting page detail to ${JSON.stringify(page)}`);
        this.set({ page });
        }
 
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

        if(this._state.pageSize != pageSize) {
            this.log.debug(`${LOG_PREFIX} Setting page size to ${JSON.stringify(pageSize)}`);
            this.set({ pageSize });
        }        

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
        
        if(this._state.searchTerm != searchTerm) {
            this.log.debug(`${LOG_PREFIX} Setting search term to ${JSON.stringify(searchTerm)}`);
            this.set({ searchTerm });
        }
        
    }


    /**
     * Updates the currently set sort column detail and triggers the data transformation exercise
     */
    set sortColumn(sortColumn: string) {

        if(this._state.sortColumn != sortColumn) {
            this.log.debug(`${LOG_PREFIX} Setting sort column to ${JSON.stringify(sortColumn)}`);
            this.set({ sortColumn });
        }               


    }


    /**
     * Updates the currently set sort direction detail
     */
    set sortDirection(sortDirection: SortDirection) {
        this.log.debug(`${LOG_PREFIX} Setting sort direction to ${JSON.stringify(sortDirection)}`);
        this.set({ sortDirection });
    }

    /**
     * Returns the currently set parent id
     */
    get reportingFrameworkId() {
        this.log.trace(`${LOG_PREFIX} Getting parent id detail`);
        this.log.debug(`${LOG_PREFIX} Current parent id detail = ${JSON.stringify(this._state.reportingFrameworkId)}`);
        return this._state.reportingFrameworkId;
    }

    /**
     * Updates the currently set parent id detail and triggers the data transformation exercise
     */
    set reportingFrameworkId(reportingFrameworkId: number | null) {

        if(this._state.reportingFrameworkId != reportingFrameworkId) {
            this.log.debug(`${LOG_PREFIX} Setting parent id to ${JSON.stringify(reportingFrameworkId)}`);
            this.set({ reportingFrameworkId });
        }  


    }  

    /**
     * Filters Land Uses Categories records by the reporting framework id
     * @param landUsesCategories 
     * @param reportingFrameworkId 
     * @returns 
     */
     filterByReportingFrameworkId(landUsesCategories: LandUseCategory[], reportingFrameworkId: number | null): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Filtering Land Uses Categories Records By Parent Id`);
        this.log.trace(`${LOG_PREFIX} Parent Id = ${reportingFrameworkId}`);
        if (reportingFrameworkId) {
            return landUsesCategories.filter((u: LandUseCategory) => u.reportingFrameworkId == reportingFrameworkId);
        } else {
            return landUsesCategories.filter((u: LandUseCategory) => u.reportingFrameworkId == null);
        }
    }
    
    /**
     * Append parent Land Uses Categories names
     * @param landUsesCategories 
     * @param reportingFrameworkId 
     * @returns 
     */
     appendParentLandUseCategoriesNames(landUsesCategories: LandUseCategory[]): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Appending Parent Land Use Categories Names`);

        return landUsesCategories.map(l => {

            if(l.parentLandUseCategoryId != null){
                const p: LandUseCategory | undefined = this.landUsesCategoriesDataService.records.find(p => p.id == l.parentLandUseCategoryId);
                if(p){
                    l.parentLandUseCategoryName = p.name;
                }
            }
            return l;
        });
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
     * Sorts Land Uses Categories Records 
     * 
     * @param landUsesCategories The Land Uses Categories Records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Land Uses Categories Records 
     */
    sort(landUsesCategories: LandUseCategory[], column: string, direction: string): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Sorting Land Uses Categories Records `);
        if (direction === '' || column == null) {
            return landUsesCategories;
        } else {
            return [...landUsesCategories].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Land Use Category Record
     * 
     * @param landUseCategory The Land Use Category Record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(landUseCategory: LandUseCategory, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if the search string is present in the Land Use Category Record`);
        if (landUseCategory != null && landUseCategory != undefined) {

            // Try locating the search string in the Land Use Category's name
            if (landUseCategory.name != null && landUseCategory.name != undefined) {
                if (landUseCategory.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Land Use Category's parent name
            if (landUseCategory.parentLandUseCategoryName != null && landUseCategory.parentLandUseCategoryName != undefined) {
                if (landUseCategory.parentLandUseCategoryName.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }            
        }

        return false;
    }


    /**
     * Updates the index of the Land Uses Categories Records 
     * 
     * @param landUsesCategories The Land Uses Categories Records to sort
     * @returns The newly indexed Land Uses Categories Records 
     */
     index(landUsesCategories: LandUseCategory[]): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Indexing Land Uses Categories Records `);
        let pos: number = 0;
        return landUsesCategories.map(d => {
            d.pos = ++pos;
            return d;
        });
    }    


    /**
     * Paginates Land Uses Categories Records 
     * 
     * @param landUsesCategories The Land Uses Categories Records to paginate
     * @returns The paginated Land Uses Categories Records 
     */
    paginate(landUsesCategories: LandUseCategory[], page: number, pageSize: number): LandUseCategory[] {
        this.log.trace(`${LOG_PREFIX} Paginating Land Uses Categories Records `);
        return landUsesCategories.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }




    /**
     * Utility method for all the class setters.
     * Updates the sort / filter criteria and triggers the data transformation exercise
     * @param patch the partially updated details
     */
    set(patch: Partial<LandUseCategoryState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Land Uses Categories Records 
        this.transform(this.landUsesCategoriesDataService.records);

    }


    /**
     * Sorts, filters and paginates Land Uses Categories Records 
     * 
     * @param records the original Land Uses Categories Records 
     */
    transform(records: LandUseCategory[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Land Uses Categories Records `);
            this.log.debug(`${LOG_PREFIX} Land Uses Categories Records before transformation = ${JSON.stringify(records)}`);

            const { reportingFrameworkId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Reporting Framework Id
            let transformed: LandUseCategory[] = this.filterByReportingFrameworkId(records, reportingFrameworkId);
            this.log.debug(`${LOG_PREFIX} Land Uses Categories Records after 'Filter by Parent Id' Transformation = ${JSON.stringify(transformed)}`);

            // Append parent land use category name
            transformed = this.appendParentLandUseCategoriesNames(records);
            this.log.debug(`${LOG_PREFIX} Land Uses Categories Records after 'Appending Parent Land Use Category Name' Transformation = ${JSON.stringify(transformed)}`);            

            // Filter by Search Term
            transformed = transformed.filter(landUseCategory => this.matches(landUseCategory, searchTerm));
            const total: number = transformed.length;
            this.log.debug(`${LOG_PREFIX} Land Uses Categories Records after 'Filter by Search Term' Transformation = ${JSON.stringify(transformed)}`);            
            
            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);
            this.log.debug(`${LOG_PREFIX} Land Uses Categories Records after 'Sort' Transformation = ${JSON.stringify(transformed)}`);


            // Index
            transformed = this.index(transformed);
            this.log.debug(`${LOG_PREFIX} Land Uses Categories Records after 'Index' Transformation = ${JSON.stringify(transformed)}`);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);
            this.log.debug(`${LOG_PREFIX} Land Uses Categories Records after 'Paginate' Transformation = ${JSON.stringify(transformed)}`);

            // Broadcast
            this._landUsesCategoriesSubject$.next(Object.assign([],transformed));
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._landUsesCategoriesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
