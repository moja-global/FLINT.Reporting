import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { PartiesDataService } from './parties-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { Party } from '../models/party.model';

const LOG_PREFIX: string = "[Parties Records Tabulation Service]";

interface PartyState extends State {
    partyTypeId: number | undefined | null;
}

@Injectable({ providedIn: 'root' })
export class PartiesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // Parties records are transformed as per the user defined search 
    // or sort criteria    
    private _partiesSubject$ = new BehaviorSubject<Party[]>([]);
    private _parties$ = this._partiesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // Parties records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Parties records should be displayed
    private _state: PartyState = { partyTypeId: null, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private partiesDataService: PartiesDataService,
        private log: NGXLogger) {

        this._subscriptions.push(
            this.partiesDataService.parties$
                .subscribe(
                    (parties: Party[]) => {
                        this._transform(parties);
                    }));

    }

  ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    /**
     * Returns an observable containing Parties records that have been filtered as per the user defined criteria
     */
    get parties$() {
        this.log.trace(`${LOG_PREFIX} Getting parties$ observable`);
        this.log.debug(`${LOG_PREFIX} Current parties$ observable value = ${JSON.stringify(this._partiesSubject$.value)}`);
        return this._parties$;
    }


    /**
     * Returns an observable containing the total number of Parties records that have been filtered as per the user defined criteria
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
     * Returns the currently set parent party id
     */
     get partyTypeId() {
        this.log.trace(`${LOG_PREFIX} Getting parent party id detail`);
        this.log.debug(`${LOG_PREFIX} Current parent party id detail = ${JSON.stringify(this._state.partyTypeId)}`);
        return this._state.partyTypeId;
    }

    /**
     * Updates the desired parent party id detail and then triggers data transformation
     */
    set partyTypeId(partyTypeId: number | undefined | null) {
        this.log.debug(`${LOG_PREFIX} Setting parent party id to ${JSON.stringify(partyTypeId)}`);
        this._set({ partyTypeId });
    }    

    /**
     * Utility method for all the class setters.
     * Does the actual updating of details / transforming of data
     * @param patch the partially updated details
     */
    private _set(patch: Partial<PartyState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Parties records
        this._transform(this.partiesDataService.records);

    }

    /**
     * Filters party records by parent party id
     * @param parties 
     * @param partyTypeId 
     * @returns 
     */
     filterByPartyType(parties: Party[], partyTypeId: number | null | undefined): Party[] {
        this.log.trace(`${LOG_PREFIX} Filtering parties records`);
        if (partyTypeId == null || partyTypeId == undefined) {
            return parties;
        } else {
            return parties.filter((u) => u.partyTypeId == partyTypeId);
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
     * Sorts Parties Records
     * 
     * @param parties The Parties records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Parties records
     */
    sort(parties: Party[], column: string, direction: string): Party[] {
        this.log.trace(`${LOG_PREFIX} Sorting Parties records`);
        if (direction === '' || column == null) {
            return parties;
        } else {
            return [...parties].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in Party record
     * 
     * @param party The Party record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(party: Party, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in Party record`);
        if (party != null && party != undefined) {

            // Try locating the search string in the Party's name
            if (party.name != null && party.name != undefined) {
                if (party.name.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Parties Records
     * 
     * @param parties The Parties records to paginate
     * @returns The paginated Parties records
     */
    paginate(parties: Party[], page: number, pageSize: number): Party[] {
        this.log.trace(`${LOG_PREFIX} Paginating Parties records`);
        return parties.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Parties Records
     * 
     * @param parties The Parties records to sort
     * @returns The newly indexed Parties records
     */
    index(parties: Party[]): Party[] {
        this.log.trace(`${LOG_PREFIX} Indexing Parties records`);
        let pos: number = 0;
        return parties.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Parties records
     * 
     * @param records the original Parties records
     */
    private _transform(records: Party[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Parties records`);

            const { partyTypeId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Parent Party
            let transformed: Party[] = this.filterByPartyType(records, partyTypeId);

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);

            // Filter by Search Term
            transformed = transformed.filter(party => this.matches(party, searchTerm));
            const total: number = transformed.length;

            // Index
            transformed = this.index(transformed);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);

            // Broadcast
            this._partiesSubject$.next(transformed);
            this._totalSubject$.next(total);

        } else {

            // Broadcast
            this._partiesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
