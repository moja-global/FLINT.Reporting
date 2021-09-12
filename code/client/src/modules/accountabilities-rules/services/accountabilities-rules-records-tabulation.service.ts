import { Injectable, OnDestroy } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Subscription } from 'rxjs';
import { State } from '@common/models';
import { SortDirection } from '@common/directives/sortable.directive';
import { AccountabilityRule } from '../models/accountability-rule.model';
import { AccountabilitiesRulesDataService } from './accountabilities-rules-data.service';
import { first } from 'rxjs/operators';
import { PartiesTypesDataService } from '@modules/parties-types/services/parties-types-data.service';
import { PartyType } from '@modules/parties-types/models/party-type.model';

const LOG_PREFIX: string = "[Accountabilities Rules Records Tabulation Service]";

export interface AccountabilityRuleState extends State {
    accountabilityTypeId: number | null;
}

@Injectable({ providedIn: 'root' })
export class AccountabilitiesRulesRecordsTabulationService implements OnDestroy {

    // The user defined search or sort criteria.
    private _state: AccountabilityRuleState = { accountabilityTypeId: null, page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };

    // The first set of observables that will be updated / broadcasted whenever 
    // a background task is started or completed  
    private _loadingSubject$ = new BehaviorSubject<boolean>(false);
    private _loading$ = this._loadingSubject$.asObservable();

    // The second set of observables that will be updated / broadcasted whenever 
    // the user enters a search term or specifies a sort criteria    
    private _unpaginatedAccountabilitiesRulesSubject$ = new BehaviorSubject<AccountabilityRule[]>([]);
    private _unpaginatedAccountabilitiesRules$ = this._unpaginatedAccountabilitiesRulesSubject$.asObservable();    

    // The third set of observables that will be updated / broadcasted whenever 
    // the user enters a search term or specifies a sort criteria    
    private _accountabilitiesRulesSubject$ = new BehaviorSubject<AccountabilityRule[]>([]);
    private _accountabilitiesRules$ = this._accountabilitiesRulesSubject$.asObservable();

    // The fourth set of observables that will be updated / broadcasted whenever 
    // the user enters a search term or specifies a sort criteria 
    // and Accountabilities Rules Records as a result
    private _totalSubject$ = new BehaviorSubject<number>(0);
    private _total$ = this._totalSubject$.asObservable();
  

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private accountabilitiesRulesDataService: AccountabilitiesRulesDataService,
        private partiesTypesDataService: PartiesTypesDataService,
        private log: NGXLogger) {

            this.initPartiesTypes(
                () => this.initAccountabilitiesRulesRecordsTabulationService());

    }

    initPartiesTypes(callback: () => void) {

        // Check if there's a need for Parties Types initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for Parties Types initialization`);
        if (this.partiesTypesDataService.records.length != 0) {

            // The Parties Types have already been initialized
            this.log.trace(`${LOG_PREFIX} The Parties Types have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for Parties Types initialization
            this.log.trace(`${LOG_PREFIX} There's a need for Parties Types initialization`);

            // Initializing the Parties Types
            this.log.trace(`${LOG_PREFIX} Initializing the Parties Types`);
            this.partiesTypesDataService
                .getAllPartiesTypes()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Parties Types initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Parties Types initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
    }
    
    initAccountabilitiesRulesRecordsTabulationService() {
        this._subscriptions.push(
            this.accountabilitiesRulesDataService.accountabilitiesRules$
                .subscribe(
                    (accountabilitiesRules: AccountabilityRule[]) => {
                        this.transform(accountabilitiesRules);
                    }));
    }

    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Service`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }


    /**
     * Returns an observable containing Accountabilities Rules Records that have been filtered as per the Current User Defined Criteria with the exception of pagination
     */
     get unpaginatedAccountabilitiesRules$() {
        this.log.trace(`${LOG_PREFIX} Getting unpaginatedAccountabilitiesRules$ observable`);
        this.log.debug(`${LOG_PREFIX} Current unpaginatedAccountabilitiesRules$ observable value = ${JSON.stringify(this._accountabilitiesRulesSubject$.value)}`);
        return this._unpaginatedAccountabilitiesRules$;
    }    


    /**
     * Returns an observable containing Accountabilities Rules Records that have been filtered as per the Current User Defined Criteria
     */
    get accountabilitiesRules$() {
        this.log.trace(`${LOG_PREFIX} Getting accountabilitiesRules$ observable`);
        this.log.debug(`${LOG_PREFIX} Current accountabilitiesRules$ observable value = ${JSON.stringify(this._accountabilitiesRulesSubject$.value)}`);
        return this._accountabilitiesRules$;
    }


    /**
     * Returns an observable containing the total number of Accountabilities Rules Records that have been filtered as per the Current User Defined Criteria
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
     * Returns the currently set accountability type id
     */
     get accountabilityTypeId() {
        this.log.trace(`${LOG_PREFIX} Getting accountability type id detail`);
        this.log.debug(`${LOG_PREFIX} Current accountability type id detail = ${JSON.stringify(this._state.accountabilityTypeId)}`);
        return this._state.accountabilityTypeId;
    }

    /**
     * Updates the currently set accountability type id detail and triggers the data transformation exercise
     */
    set accountabilityTypeId(accountabilityTypeId: number | null) {

        if(this._state.accountabilityTypeId != accountabilityTypeId) {
            this.log.debug(`${LOG_PREFIX} Setting accountability type id to ${JSON.stringify(accountabilityTypeId)}`);
            this.set({ accountabilityTypeId });
        }  
    }  


    /**
     * Filters Accountabilities Rules records by the Accountability Type id
     * @param accountabilitiesRules 
     * @param accountabilityTypeId 
     * @returns 
     */
     filterByAccountabilityTypeId(accountabilitiesRules: AccountabilityRule[], accountabilityTypeId: number | null): AccountabilityRule[] {
        this.log.trace(`${LOG_PREFIX} Filtering Accountabilities Rules Records By Accountability Type Id`);

        if(accountabilityTypeId == null) {
            return accountabilitiesRules;
        }

        return accountabilitiesRules.filter((u: AccountabilityRule) => u.accountabilityTypeId == accountabilityTypeId);
    }      
    
    
    /**
     * Appends Subsidiary Party Type Names to the Accountabilities Rules records
     * @param accountabilitiesRules 
     * @returns 
     */
     appendSubsidiaryPartyTypeName(accountabilitiesRules: AccountabilityRule[]): AccountabilityRule[] {
        this.log.trace(`${LOG_PREFIX} Appending Party Type Names to the Accountabilities Rules records`);
        let partyType: PartyType | undefined;
        return accountabilitiesRules.map((a: AccountabilityRule) => {

            // Get the target Party Type
            partyType = this.partiesTypesDataService.records.find((p: PartyType) => p.id == a.subsidiaryPartyTypeId);

            // Set the name in the accountability type
            if(partyType){
                a.subsidiaryPartyTypeName = partyType.name;
            } else {
                a.subsidiaryPartyTypeName = "";
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
    compare(v1: number | string | undefined | null, v2: number | string | undefined | null) {
        this.log.trace(`${LOG_PREFIX} Comparing two values to find out if the first value preceeds the second`);
        return (v1 == undefined || v1 == null || v2 == undefined || v2 == null) ? 0 : v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
    }


    /**
     * Sorts Accountabilities Rules Records 
     * 
     * @param accountabilitiesRules The Accountabilities Rules Records to sort
     * @param column The table column to sort the records by 
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Accountabilities Rules Records 
     */
    sort(accountabilitiesRules: AccountabilityRule[], column: string, direction: string): AccountabilityRule[] {
        this.log.trace(`${LOG_PREFIX} Sorting Accountabilities Rules Records `);
        if (direction === '' || column == null) {
            return accountabilitiesRules;
        } else {
            return [...accountabilitiesRules].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }


    /**
     * Checks if search string is present in the Accountability Rule Record
     * 
     * @param accountabilityRule The Accountability Rule Record
     * @param term The Search String
     * @returns A boolean result indicating whether or not a match was found
     */
    matches(accountabilityRule: AccountabilityRule, term: string): boolean {
        this.log.trace(`${LOG_PREFIX} Checking if the search string is present in the Accountability Rule Record`);
        if (accountabilityRule != null && accountabilityRule != undefined) {

            // Try locating the search string in the Accountability Rule's indicator name
            if (accountabilityRule.subsidiaryPartyTypeName != null && accountabilityRule.subsidiaryPartyTypeName != undefined) {
                if (accountabilityRule.subsidiaryPartyTypeName?.toLowerCase().includes(term.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Updates the level of the Accountabilities Rules Records 
     * 
     * @param accountabilitiesRules The Accountabilities Rules Records to sort
     * @returns The newly leveled Accountabilities Rules Records 
     */
     level(accountabilitiesRules: AccountabilityRule[]): AccountabilityRule[] {
        this.log.trace(`${LOG_PREFIX} Levelling Accountabilities Rules Records `);

        let temp: AccountabilityRule[] = Object.assign([], accountabilitiesRules.sort((n1, n2) => {
            if (n1 > n2) {
              return 1;
            }
  
            if (n1 < n2) {
              return -1;
            }
  
            return 0;
          }));        


        let level: number = 0;
        return temp.map(d => {
            d.level = ++level;
            return d;
        });
    }     


    /**
     * Updates the index of the Accountabilities Rules Records 
     * 
     * @param accountabilitiesRules The Accountabilities Rules Records to sort
     * @returns The newly indexed Accountabilities Rules Records 
     */
     index(accountabilitiesRules: AccountabilityRule[]): AccountabilityRule[] {
        this.log.trace(`${LOG_PREFIX} Indexing Accountabilities Rules Records `);
        let pos: number = 0;
        return accountabilitiesRules.map(d => {
            d.pos = ++pos;
            return d;
        });
    }    


    /**
     * Paginates Accountabilities Rules Records 
     * 
     * @param accountabilitiesRules The Accountabilities Rules Records to paginate
     * @returns The paginated Accountabilities Rules Records 
     */
    paginate(accountabilitiesRules: AccountabilityRule[], page: number, pageSize: number): AccountabilityRule[] {
        this.log.trace(`${LOG_PREFIX} Paginating Accountabilities Rules Records `);
        return accountabilitiesRules.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }




    /**
     * Utility method for all the class setters.
     * Updates the sort / filter criteria and triggers the data transformation exercise
     * @param patch the partially updated details
     */
    set(patch: Partial<AccountabilityRuleState>) {

        // Update the state
        Object.assign(this._state, patch);


        // Transform the Accountabilities Rules Records 
        this.transform(this.accountabilitiesRulesDataService.records);

    }


    /**
     * Sorts, filters and paginates Accountabilities Rules Records 
     * 
     * @param records the original Accountabilities Rules Records 
     */
    transform(records: AccountabilityRule[]) {

        // Flag
        this._loadingSubject$.next(true);

        if (records.length != 0) {

            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Accountabilities Rules Records `);
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records before transformation = ${JSON.stringify(records)}`);

            const { accountabilityTypeId, sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;

            // Filter by Accountability Type Id
            let transformed: AccountabilityRule[] = this.filterByAccountabilityTypeId(records, accountabilityTypeId);
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records after 'Filter by Accountability Type Id' Transformation = ${JSON.stringify(transformed)}`);


            // Append Subsidiary Party Types Names
            transformed = this.appendSubsidiaryPartyTypeName(transformed);
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records after 'Append Subsidiary Party Types Names' Transformation = ${JSON.stringify(transformed)}`);            


            // Filter by Search Term
            transformed = transformed.filter(accountabilityRule => this.matches(accountabilityRule, searchTerm));
            const total: number = transformed.length;
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records after 'Filter by Search Term' Transformation = ${JSON.stringify(transformed)}`);            
            

            // Sort
            transformed = this.sort(transformed, sortColumn, sortDirection);
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records after 'Sort' Transformation = ${JSON.stringify(transformed)}`);


            // Index
            transformed = this.index(transformed);
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records after 'Index' Transformation = ${JSON.stringify(transformed)}`);


            // Level
            transformed = this.level(transformed);
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records after 'Level' Transformation = ${JSON.stringify(transformed)}`);            

            
            // Broadcast pre - pagination
            this._unpaginatedAccountabilitiesRulesSubject$.next(Object.assign([],transformed));            


            // Paginate
            transformed = this.paginate(transformed, page, pageSize);
            this.log.debug(`${LOG_PREFIX} Accountabilities Rules Records after 'Paginate' Transformation = ${JSON.stringify(transformed)}`);


            // Broadcast post - pagination
            this._accountabilitiesRulesSubject$.next(Object.assign([],transformed));
            this._totalSubject$.next(total);


        } else {

            // Broadcast
            this._accountabilitiesRulesSubject$.next([]);
            this._totalSubject$.next(0);
        }

        // Flag
        this._loadingSubject$.next(false);

    }

}
