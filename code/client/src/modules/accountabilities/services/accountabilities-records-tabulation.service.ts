import { Injectable, OnDestroy } from '@angular/core';
import { AccountabilitiesDataService } from './accountabilities-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { Accountability } from '../models/accountability.model';
import { PartiesDataService } from '@modules/parties/services/parties-data.service';
import { first } from 'rxjs/operators';
import { Party } from '@modules/parties/models/party.model';

const LOG_PREFIX: string = "[Accountabilities Records Tabulation Service]";

export interface AccountabilityState extends State {
    accountabilityRuleId: number | null;
}

@Injectable({ providedIn: 'root' })
export class AccountabilitiesRecordsTabulationService implements OnDestroy {

    // The observables that will be updated / broadcasted whenever 
    // a background task is started and completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(true);
    private _loading$ = this._loadingSubject$.asObservable();

    // The first set of observables that will be updated / broadcasted whenever 
    // an accountability rule is specified   
    private _accountabilityRuleAccountabilitiesSubject$ = new BehaviorSubject<Accountability[]>([]);
    private _accountabilityRuleAccountabilities$ = this._accountabilityRuleAccountabilitiesSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // an accountability rule is specified 
    private _accountabilityRuleAccountabilitiesTotalSubject$ = new BehaviorSubject<number>(0);
    private _accountabilityRuleAccountabilitiesTotal$ = this._accountabilityRuleAccountabilitiesTotalSubject$.asObservable();       

    // The third set of observables that will be updated / broadcasted whenever 
    // Accountabilities records are transformed as per the user defined search 
    // or sort criteria    
    private _accountabilitiesSubject$ = new BehaviorSubject<Accountability[]>([]);
    private _accountabilities$ = this._accountabilitiesSubject$.asObservable();

    // The fourth set of observables that will be updated / broadcasted whenever 
    // Accountabilities records are transformed as per the user defined search 
    // or sort criteria
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();

    // The user defined search or sort criteria.
    // Determines which & how many Accountabilities records should be displayed
    private _state: AccountabilityState = { accountabilityRuleId: null, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private accountabilitiesDataService: AccountabilitiesDataService,
        private partiesDataService: PartiesDataService,
        private log: NGXLogger) {

        this.initParties(
            () => this.initAccountabilitiesRecordsTabulationService());

    }

    initParties(callback: () => void) {

        // Check if there's a need for Parties initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for Parties initialization`);
        if (this.partiesDataService.records.length != 0) {

            // The Parties have already been initialized
            this.log.trace(`${LOG_PREFIX} The Parties have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for Parties initialization
            this.log.trace(`${LOG_PREFIX} There's a need for Parties initialization`);

            // Initializing the Parties
            this.log.trace(`${LOG_PREFIX} Initializing the Parties`);
            this.partiesDataService
                .getAllParties()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Parties initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Parties initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
    }

    initAccountabilitiesRecordsTabulationService() {
        this._subscriptions.push(
            this.accountabilitiesDataService.accountabilities$
                .subscribe(
                    (accountabilities: Accountability[]) => {
                        this._transform(accountabilities);
                    }));
    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing all the Accountabilities records that fall under the specified accountability rule without any further filters
     */
    get accountabilityRuleAccountabilities$() {
        this.log.trace(`${LOG_PREFIX} Getting accountabilityRuleAccountabilities$ observable`);
        this.log.debug(`${LOG_PREFIX} Current accountabilityRuleAccountabilities$ observable value = ${JSON.stringify(this._accountabilityRuleAccountabilitiesTotalSubject$.value)}`);
        return this._accountabilityRuleAccountabilities$;
    }


    /**
     * Returns an observable containing the total Accountabilities records that fall under the specified accountability rule without any further filters
     */
    get accountabilityRuleAccountabilitiesTotal$() {
        this.log.trace(`${LOG_PREFIX} Getting accountabilityRuleAccountabilitiesTotal$ observable`);
        this.log.debug(`${LOG_PREFIX} Current accountabilityRuleAccountabilitiesTotal$ observable value = ${JSON.stringify(this._accountabilityRuleAccountabilitiesTotalSubject$.value)}`);
        return this._accountabilityRuleAccountabilitiesTotal$;
    }



    /**
     * Returns an observable containing Accountabilities records that have been filtered as per the user defined criteria
     */
    get accountabilities$() {
        this.log.trace(`${LOG_PREFIX} Getting accountabilities$ observable`);
        this.log.debug(`${LOG_PREFIX} Current accountabilities$ observable value = ${JSON.stringify(this._accountabilitiesSubject$.value)}`);
        return this._accountabilities$;
    }


    /**
     * Returns an observable containing the total number of Accountabilities records that have been filtered as per the user defined criteria
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
     * Returns the currently set accountability id
     */
    get accountabilityRuleId() {
        this.log.trace(`${LOG_PREFIX} Getting accountability rule id detail`);
        this.log.debug(`${LOG_PREFIX} Current accountability rule id detail = ${JSON.stringify(this._state.accountabilityRuleId)}`);
        return this._state.accountabilityRuleId;
    }

    /**
     * Updates the desired accountability id detail and then triggers data transformation
     */
    set accountabilityRuleId(accountabilityRuleId: number | null) {
        this.log.debug(`${LOG_PREFIX} Setting accountability rule id to ${JSON.stringify(accountabilityRuleId)}`);
        this._set({ accountabilityRuleId });
    }

    /**
     * Utility method for all the class setters.
     * Does the actual updating of details / transforming of data
     * @param patch the partially updated details
     */
    private _set(patch: Partial<AccountabilityState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Accountabilities records
        this._transform(this.accountabilitiesDataService.records);

    }

    /**
     * Filters accountability records by accountability type id
     * @param accountabilities 
     * @param accountabilityRuleId 
     * @returns 
     */
    filterByAccountabilityRule(accountabilities: Accountability[], accountabilityRuleId: number | null): Accountability[] {
        this.log.trace(`${LOG_PREFIX} Filtering Accountabilities records`);
        return accountabilities.filter((u) => u.accountabilityRuleId == accountabilityRuleId);
    }


    /**
     * Appends Parent / Subsidiary Party Names to the Accountabilities records
     * @param accountabilitiesRules 
     * @returns 
     */
    appendPartyNames(accountabilitiesRules: Accountability[]): Accountability[] {
        this.log.trace(`${LOG_PREFIX} Appending Party Names to the Accountabilities records`);

        let parentParty: Party | null | undefined;
        let subsidiaryParty: Party | undefined;

        return accountabilitiesRules.map((a: Accountability) => {

            // Get the target parent Party
            parentParty = a.parentPartyId == null ? null : this.partiesDataService.records.find((p: Party) => p.id == a.parentPartyId);

            // Set the parent party name in the accountability
            if (parentParty) {
                a.parentPartyName = parentParty.name;
            } else {
                a.parentPartyName = "";
            }

            // Get the target subsidiary Party
            subsidiaryParty = this.partiesDataService.records.find((p: Party) => p.id == a.subsidiaryPartyId);

            // Set the subsidiary party name in the accountability
            if (subsidiaryParty) {
                a.subsidiaryPartyName = subsidiaryParty.name;
            } else {
                a.subsidiaryPartyName = "";
            }

            // Return the updated Accountability Rule
            return a;

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
    compare(v1: string | number | null | undefined, v2: string | number | null | undefined) {
        this.log.trace(`${LOG_PREFIX} Comparing two values to find out if the first value preceeds the second`);
        return (v1 == undefined || v1 == null || v2 == undefined || v2 == null) ? 0 : v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
    }


    /**
     * Sorts Accountabilities Records
     * 
     * @param accountabilities The Accountabilities records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Accountabilities records
     */
    sort(accountabilities: Accountability[], column: string, direction: string): Accountability[] {
        this.log.trace(`${LOG_PREFIX} Sorting Accountabilities records`);
        if (direction === '' || column == null) {
            return accountabilities;
        } else {
            return [...accountabilities].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Accountability record
     * 
     * @param accountability The Accountability record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(accountability: Accountability, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in the Accountability record`);
        if (accountability != null && accountability != undefined) {

            // Try locating the search string in the Accountability's Parent Party Name
            if (accountability.parentPartyName != null && accountability.parentPartyName != undefined) {
                if (accountability.parentPartyName.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }

            // Try locating the search string in the Accountability's Subsidiary Party Name
            if (accountability.subsidiaryPartyName != null && accountability.subsidiaryPartyName != undefined) {
                if (accountability.subsidiaryPartyName.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Paginates Accountabilities Records
     * 
     * @param accountabilities The Accountabilities records to paginate
     * @returns The paginated Accountabilities records
     */
    paginate(accountabilities: Accountability[], page: number, pageSize: number): Accountability[] {
        this.log.trace(`${LOG_PREFIX} Paginating Accountabilities records`);
        return accountabilities.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }

    /**
     * Updates the index of the Accountabilities Records
     * 
     * @param accountabilities The Accountabilities records to sort
     * @returns The newly indexed Accountabilities records
     */
    index(accountabilities: Accountability[]): Accountability[] {
        this.log.trace(`${LOG_PREFIX} Indexing Accountabilities records`);
        let pos: number = 0;
        return accountabilities.map(d => {
            d.pos = ++pos;
            return d;
        });
    }


    /**
     * Sorts, filters and paginates Accountabilities records
     * 
     * @param records the original Accountabilities records
     */
    _transform(records: Accountability[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Accountabilities Records `);
            this.log.debug(`${LOG_PREFIX} Accountabilities Records before transformation = ${JSON.stringify(records)}`);

            const { accountabilityRuleId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Accountability Rule
            let transformed: Accountability[] = this.filterByAccountabilityRule(records, accountabilityRuleId);
            this.log.debug(`${LOG_PREFIX} Accountabilities Records after 'Filter by Accountability Rule' Transformation = ${JSON.stringify(transformed)}`);

            // Broadcast before further filtering
            this._accountabilityRuleAccountabilitiesSubject$.next(Object.assign([],transformed));
            this._accountabilityRuleAccountabilitiesTotalSubject$.next(transformed.length);
            
            // Append Party Names
            transformed = this.appendPartyNames(transformed);
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records after 'Append Party Names' Transformation = ${JSON.stringify(transformed)}`);            

            // Filter by Search Term
            transformed = transformed.filter(contextIssueIndicator => this.matches(contextIssueIndicator, searchTerm));
            const total: number = transformed.length;
            this.log.debug(`${LOG_PREFIX} Accountabilities Records after 'Filter by Search Term' Transformation = ${JSON.stringify(transformed)}`);

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);
            this.log.debug(`${LOG_PREFIX} Accountabilities Records after 'Sort' Transformation = ${JSON.stringify(transformed)}`);

            // Index
            transformed = this.index(transformed);
            this.log.debug(`${LOG_PREFIX} Accountabilities Records after 'Index' Transformation = ${JSON.stringify(transformed)}`);

            // Paginate
            transformed = this.paginate(transformed, page, pageSize);
            this.log.debug(`${LOG_PREFIX} Accountabilities Records after 'Paginate' Transformation = ${JSON.stringify(transformed)}`);


            // Broadcast post - pagination
            this._accountabilitiesSubject$.next(Object.assign([], transformed));
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._accountabilityRuleAccountabilitiesSubject$.next([]);
            this._accountabilityRuleAccountabilitiesTotalSubject$.next(0);
            this._accountabilitiesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }
}
