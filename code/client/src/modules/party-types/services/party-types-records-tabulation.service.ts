import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { PartyTypesDataService } from './party-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { PartyType } from '../models/party-type.model';

const LOG_PREFIX: string = "[Party Types Records Tabulation Service]";

interface PartyTypeState extends State {
    parentAdministrativeLevelId: number | undefined | null;
}

@Injectable({ providedIn: 'root' })
export class PartyTypesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Party Types records are transformed as per the user defined search 
    // or sort criteria    
    private _partyTypesSubject$ = new BehaviorSubject<PartyType[]>([]);
    private _partyTypes$ = this._partyTypesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Party Types records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Party Types records should be displayed
    private _state: PartyTypeState = { parentAdministrativeLevelId: null, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private partyTypesDataService: PartyTypesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.partyTypesDataService.partyTypes$
                .subscribe(
                    (partyTypes: PartyType[]) => {
                        this._transform(partyTypes);
                    }));

    }

  ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    /**
     * Returns an observable containing Party Types records that have been filtered as per the user defined criteria
     */
    get partyTypes$() {
        this.log.trace(`${LOG_PREFIX} Getting partyTypes$ observable`);
        this.log.debug(`${LOG_PREFIX} Current partyTypes$ observable value = ${JSON.stringify(this._partyTypesSubject$.value)}`);
        return this._partyTypes$;
    }


    /**
     * Returns an observable containing the total number of Party Types records that have been filtered as per the user defined criteria
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
     * Returns the currently set parent party type id
     */
     get parentAdministrativeLevelId() {
        this.log.trace(`${LOG_PREFIX} Getting parent party type id detail`);
        this.log.debug(`${LOG_PREFIX} Current parent party type id detail = ${JSON.stringify(this._state.parentAdministrativeLevelId)}`);
        return this._state.parentAdministrativeLevelId;
    }

    /**
     * Updates the desired parent party type id detail and then triggers data transformation
     */
    set parentAdministrativeLevelId(parentAdministrativeLevelId: number | undefined | null) {
        this.log.debug(`${LOG_PREFIX} Setting parent party type id to ${JSON.stringify(parentAdministrativeLevelId)}`);
        this._set({ parentAdministrativeLevelId });
    }    

    /**
     * Utility method for all the class setters.
     * Does the actual updating of details / transforming of data
     * @param patch the partially updated details
     */
    private _set(patch: Partial<PartyTypeState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Party Types records
        this._transform(this.partyTypesDataService.records);

    }

    /**
     * Filters party type records by parent party type id
     * @param partyTypes 
     * @param parentAdministrativeLevelId 
     * @returns 
     */
     filterByParentPartyType(partyTypes: PartyType[], parentAdministrativeLevelId: number | null | undefined): PartyType[] {
        this.log.trace(`${LOG_PREFIX} Filtering party types records`);
        if (parentAdministrativeLevelId == null || parentAdministrativeLevelId == undefined) {
            return partyTypes;
        } else {
            return partyTypes.filter((u) => u.parentAdministrativeLevelId == parentAdministrativeLevelId);
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
     * Sorts Party Types Records
     * 
     * @param partyTypes The Party Types records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Party Types records
     */
    sort(partyTypes: PartyType[], column: string, direction: string): PartyType[] {
        this.log.trace(`${LOG_PREFIX} Sorting Party Types records`);
        if (direction === '' || column == null) {
            return partyTypes;
        } else {
            return [...partyTypes].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in Party Type record
     * 
     * @param partyType The Party Type record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(partyType: PartyType, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in Party Type record`);
        if (partyType != null && partyType != undefined) {

            // Try locating the search string in the Party Type's name
            if (partyType.name != null && partyType.name != undefined) {
                if (partyType.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Party Types Records
     * 
     * @param partyTypes The Party Types records to paginate
     * @returns The paginated Party Types records
     */
    paginate(partyTypes: PartyType[], page: number, pageSize: number): PartyType[] {
        this.log.trace(`${LOG_PREFIX} Paginating Party Types records`);
        return partyTypes.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Party Types Records
     * 
     * @param partyTypes The Party Types records to sort
     * @returns The newly indexed Party Types records
     */
    index(partyTypes: PartyType[]): PartyType[] {
        this.log.trace(`${LOG_PREFIX} Indexing Party Types records`);
        let pos: number = 0;
        return partyTypes.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Party Types records
     * 
     * @param records the original Party Types records
     */
    private _transform(records: PartyType[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Party Types records`);

            const { parentAdministrativeLevelId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Parent Party Type
            let transformed: PartyType[] = this.filterByParentPartyType(records, parentAdministrativeLevelId);

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);

            // Filter by Search Term
            transformed = transformed.filter(partyType => this.matches(partyType, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._partyTypesSubject$.next(transformed);
            this._totalSubject$.next(total);

        } else {

            // Broadcast
            this._partyTypesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
