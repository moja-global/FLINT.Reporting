import { Injectable } from '@angular/core';
import { AccountabilityRule } from '@modules/accountabilities-rules/models';
import { AccountabilitiesRulesDataService } from '@modules/accountabilities-rules/services';
import { AccountabilitiesTypesDataService } from '@modules/accountabilities-types/services';
import { Accountability } from '@modules/accountabilities/models/accountability.model';
import { AccountabilitiesDataService } from '@modules/accountabilities/services';
import { PartyType } from '@modules/parties-types/models';
import { PartiesTypesDataService } from '@modules/parties-types/services';
import { Party } from '@modules/parties/models/party.model';
import { PartiesDataService } from '@modules/parties/services';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject } from 'rxjs';
import { first } from 'rxjs/operators';
import { Filter } from '../models/filter.model';

const LOG_PREFIX: string = "[Filter Service]";

@Injectable({ providedIn: 'root' })
export class FilterService {

    private _filterSubject$ = new BehaviorSubject<Filter>(new Filter());
    readonly filter$ = this._filterSubject$.asObservable();

    get currentFilter() : Filter {
        return this._filterSubject$.value;
    }

    constructor(
        private partiesTypesDataService: PartiesTypesDataService,
        private partiesDataService: PartiesDataService,
        private accountabilitiesTypesDataService: AccountabilitiesTypesDataService,
        private accountabilitiesRulesDataService: AccountabilitiesRulesDataService,
        private accountabilitiesDataService: AccountabilitiesDataService,
        private log: NGXLogger
    ) {

        this.initAccountabilitiesTypes(
            () => this.initAccountabilitiesRules(
                () => this.initPartiesTypes(
                    () => this.initParties(
                        () => this.initAccountabilities(
                            () => { }
                        )
                    ))));
    }

    initAccountabilitiesTypes(callback: () => void) {

        // Check if there's a need for Accountabilities Types initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for Accountabilities Types initialization`);
        if (this.accountabilitiesTypesDataService.records.length != 0) {

            // The Accountabilities Types have already been initialized
            this.log.trace(`${LOG_PREFIX} The Accountabilities Types have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for Accountabilities Types initialization
            this.log.trace(`${LOG_PREFIX} There's a need for Accountabilities Types initialization`);

            // Initializing the Accountabilities Types
            this.log.trace(`${LOG_PREFIX} Initializing the Accountabilities Types`);
            this.accountabilitiesTypesDataService
                .getAllAccountabilitiesTypes()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Accountabilities Types initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Accountabilities Types initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
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


    initAccountabilitiesRules(callback: () => void) {

        // Check if there's a need for Accountabilities Rules initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for Accountabilities Rules initialization`);
        if (this.accountabilitiesRulesDataService.records.length != 0) {

            // The Accountabilities Rules have already been initialized
            this.log.trace(`${LOG_PREFIX} The Accountabilities Rules have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for Accountabilities Rules initialization
            this.log.trace(`${LOG_PREFIX} There's a need for Accountabilities Rules initialization`);

            // Initializing the Accountabilities Rules
            this.log.trace(`${LOG_PREFIX} Initializing the Accountabilities Rules`);
            this.accountabilitiesRulesDataService
                .getAllAccountabilitiesRules()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Accountabilities Rules initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Accountabilities Rules initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
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
            this.log.trace(`${LOG_PREFIX} Initializing the Parties `);
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

    initAccountabilities(callback: () => void) {

        // Check if there's a need for Accountabilities initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for Accountabilities  initialization`);
        if (this.accountabilitiesDataService.records.length != 0) {

            // The Accountabilities  have already been initialized
            this.log.trace(`${LOG_PREFIX} The Accountabilities  have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for Accountabilities initialization
            this.log.trace(`${LOG_PREFIX} There's a need for Accountabilities  initialization`);

            // Initializing the Accountabilities 
            this.log.trace(`${LOG_PREFIX} Initializing the Accountabilities `);
            this.accountabilitiesDataService
                .getAllAccountabilities()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Accountabilities  initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Accountabilities  initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }
    }

    public onAccountabilityTypeIdChange(accountabilityTypeId: number | null) {

        this.log.trace(`${LOG_PREFIX} Entering onAccountabilityTypeIdChange()`);

        // Ignore the update if the incoming value is the same as the existing one
        if (this._filterSubject$.value.accountabilityTypeId == accountabilityTypeId) {
            return;
        }

        // Make a copy of the current filter 
        const filter: Filter = Object.assign({}, this._filterSubject$.value);

        // Carry out the corresponding updates and propagate
        this.setAccountabilityTypeId(filter, accountabilityTypeId,
            () => this.resetHierarchicalLevel(filter, 0,
                () => this.resetAccountabilityRules(filter, accountabilityTypeId,
                    () => this.resetParentPartiesIds(filter,
                        () => this.resetAccountabilities(filter, accountabilityTypeId,
                            () => this.broadcast(filter))))));

    }


    public onDrillDown(subsidiaryPartyId: number) {

        this.log.trace(`${LOG_PREFIX} Entering onDrillDown()`);

        // Make a copy of the current filter 
        const filter: Filter = Object.assign({}, this._filterSubject$.value);

        // Carry out the corresponding updates and propagate
        this.incrementHierarchicalLevel(filter,
            () => this.setAccountabilityRule(filter,
                () => this.appendParentPartyId(filter, subsidiaryPartyId, 
                    () => this.setAccountabilities(filter, filter.accountabilityTypeId, subsidiaryPartyId, 
                        () => this.broadcast(filter)))));

    } 
    
    
    public onDrillUp() {

        this.log.trace(`${LOG_PREFIX} Entering onDrillUp()`);

        // Make a copy of the current filter 
        const filter: Filter = Object.assign({}, this._filterSubject$.value);

        // Carry out the corresponding updates and propagate
        this.decrementHierarchicalLevel(filter,
            () => this.setAccountabilityRule(filter,
                () => this.removeLastAppendParentPartyId(filter, 
                    () => this.setAccountabilities(filter, filter.accountabilityTypeId, filter.parentPartiesIds[filter.parentPartiesIds.length - 1], 
                        () => this.broadcast(filter)))));

    }     


    private setAccountabilityTypeId(filter: Filter, accountabilityTypeId: number | null, callback: () => void) {

        this.log.trace(`${LOG_PREFIX} Entering setAccountabilityTypeId()`);

        // Set the accountability type id
        filter.accountabilityTypeId = accountabilityTypeId;

        // Transfer control to the callback() method
        callback();
    }


    private resetHierarchicalLevel(filter: Filter, level: number, callback: () => void) {

        this.log.trace(`${LOG_PREFIX} Entering resetHierarchicalLevel()`);

        // Set the current hierarchical level
        filter.level = level;

        // Transfer control to the callback() method
        callback();
    }


    private incrementHierarchicalLevel(filter: Filter, callback: () => void) {

        this.log.trace(`${LOG_PREFIX} Entering incrementHierarchicalLevel()`);

        // Increment the current hierarchical level
        filter.level = ((filter.level + 1) > (filter.accountabilitiesRules.length - 1)) ? filter.accountabilitiesRules.length - 1 : filter.level + 1;

        // Transfer control to the callback() method
        callback();
    }  
    
    private decrementHierarchicalLevel(filter: Filter, callback: () => void) {

        this.log.trace(`${LOG_PREFIX} Entering decrementHierarchicalLevel()`);

        // Decrement the current hierarchical level
        filter.level = (filter.level - 1) < 0 ? 0 : (filter.level - 1);

        // Transfer control to the callback() method
        callback();
    }    


    private resetAccountabilityRules(filter: Filter, accountabilityTypeId: number | null, callback: () => void): void {

        this.log.trace(`${LOG_PREFIX} Entering resetAccountabilityRules()`);

        // Retrieve the corresponding accountabilities rules
        filter.accountabilitiesRules = this.accountabilitiesRulesDataService.records.filter((a: AccountabilityRule) => a.accountabilityTypeId == accountabilityTypeId);

        if (filter.accountabilitiesRules.length > 0) {

            // Append subsidiary parties names
            let partyType: PartyType | undefined;
            filter.accountabilitiesRules = filter.accountabilitiesRules.map((a: AccountabilityRule) => {

                // Get the target Party Type
                partyType = this.partiesTypesDataService.records.find((p: PartyType) => p.id == a.subsidiaryPartyTypeId);

                // Set the name in the accountability type
                if (partyType) {
                    a.subsidiaryPartyTypeName = partyType.name;
                } else {
                    a.subsidiaryPartyTypeName = "";
                }

                // Return the updated Accountability Rule
                return a;

            });

            // Sort the accountability rules by id
            filter.accountabilitiesRules = filter.accountabilitiesRules.sort((n1, n2) => {

                if (n1.id) {

                    if (n2.id) {

                        if (n1.id > n2.id) {
                            return 1;
                        }

                        if (n1.id < n2.id) {
                            return -1;
                        }

                    }
                }

                return 0;
            });

            // Set the current accountability rule
            filter.accountabilityRule = filter.accountabilitiesRules[filter.level];

        } else {
            filter.accountabilityRule = null;
        }

                // Transfer control to the callback() method
                callback();
    }

    private setAccountabilityRule(filter: Filter, callback: () => void) {

        this.log.trace(`${LOG_PREFIX} Entering setAccountabilityRule()`);

        // Decrement the current hierarchical level
        filter.accountabilityRule = filter.accountabilitiesRules[filter.level];

        // Transfer control to the callback() method
        callback();
    }      

    private resetParentPartiesIds(filter: Filter, callback: () => void) {

        this.log.trace(`${LOG_PREFIX} Entering resetParentPartiesIds()`);

        // Set the parent parties id with a default parent id value of -1 which simply represents no parent
        filter.parentPartiesIds = [-1];

        // Transfer control to the callback() method
        callback();
    }

    private appendParentPartyId(filter: Filter, parentPartyId: number, callback: () => void) {

        this.log.trace(`${LOG_PREFIX} Entering appendParentPartyId()`);

        // Add the parent id to the parent ids array
        filter.parentPartiesIds.push(parentPartyId);

        // Transfer control to the callback() method
        callback();
    }
    
    private removeLastAppendParentPartyId(filter: Filter, callback: () => void) {

        this.log.trace(`${LOG_PREFIX} Entering removeLastAppendParentPartyId()`);

        // Remove the last appended parent id from the parent ids array
        if(filter.parentPartiesIds.length > 1) {
        filter.parentPartiesIds.splice(-1);
        }

        // Transfer control to the callback() method
        callback();
    }    

    private resetAccountabilities(filter: Filter, accountabilityTypeId: number | null, callback: () => void): void {

        this.log.trace(`${LOG_PREFIX} Entering resetAccountabilities()`);

        this.setAccountabilities(filter, accountabilityTypeId,-1,callback);
    }

    private setAccountabilities(filter: Filter, accountabilityTypeId: number | null, parentPartyId: number | null, callback: () => void): void {

        this.log.trace(`${LOG_PREFIX} Entering setAccountabilities()`);


        // Retrieve the corresponding accountabilities 
        filter.accountabilities = this.accountabilitiesDataService.records.filter((a: Accountability) => (a.accountabilityTypeId == accountabilityTypeId) && a.parentPartyId == (parentPartyId == -1 ? null : parentPartyId));

        if (filter.accountabilities.length > 0) {

            // Append subsidiary parties names
            let party: Party | undefined;
            filter.accountabilities = filter.accountabilities.map((a: Accountability) => {

                // Get the target Party
                party = this.partiesDataService.records.find((p: Party) => p.id == a.subsidiaryPartyId);

                // Set the name in the accountability
                if (party) {
                    a.subsidiaryPartyName = party.name;
                } else {
                    a.subsidiaryPartyName = "";
                }

                // Return the updated Accountability 
                return a;

            });

            // Sort the accountability  by id
            filter.accountabilities = filter.accountabilities.sort((n1: Accountability, n2: Accountability) => {

                if (n1.subsidiaryPartyName) {

                    if (n2.subsidiaryPartyName) {

                        if (n1.subsidiaryPartyName > n2.subsidiaryPartyName) {
                            return 1;
                        }

                        if (n1.subsidiaryPartyName < n2.subsidiaryPartyName) {
                            return -1;
                        }

                    }
                }

                return 0;
            });

        }

                // Transfer control to the callback() method
                callback();
    }    



    broadcast(filter: Filter) {
        this.log.trace(`${LOG_PREFIX} Entering broadcast()`);
        this._filterSubject$.next(filter);
    }



    clearFilters() {
        this._filterSubject$.next(new Filter());
    }

}