import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output
} from '@angular/core';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { AccountabilityRule } from '@modules/accountabilities-rules/models';
import { AccountabilitiesRulesDataService } from '@modules/accountabilities-rules/services';
import { Accountability } from '@modules/accountabilities/models/accountability.model';
import { AccountabilitiesDataService } from '@modules/accountabilities/services/accountabilities-data.service';
import { PartyType } from '@modules/parties-types/models';
import { PartiesTypesDataService } from '@modules/parties-types/services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';

const LOG_PREFIX: string = "[Accountabilities Records Creation Component]";

@Component({
  selector: 'sb-accountabilities-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './accountabilities-records-creation.component.html',
  styleUrls: ['accountabilities-records-creation.component.scss'],
})
export class AccountabilitiesRecordsCreationComponent implements OnInit {

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate a 'loadedNext' state notification Emitter.
  // This will allow us to broadcast notifications of whether the next accountability 
  // has been loaded pending the satisfactorily filling of the current one
  @Output() loadedNext: EventEmitter<boolean> = new EventEmitter<boolean>();

  // Instantiate a 'loadedPrevious' state notification Emitter.
  // This will allow us to broadcast notifications of whether the previous accountability 
  // has been loaded
  @Output() loadedPrevious: EventEmitter<boolean> = new EventEmitter<boolean>();

  // Instantiate and avail a previously selected Parties arrays to the parent component.
  // This will allow the parent component to specify the previously selected Parties.
  // This might then be used to disable the previously selected Parties if the next configuration is set to true
  @Input() previouslySelectedPartiesIds: number[] = [];

  // Instantiate and avail the target accountability type id variable to the parent component.
  // This will allow the parent component to inject the details of the accountability type
  @Input() accountabilityRuleId: number | null = null;

  //Keep tabs on the accountability type id
  accountabilityTypeId: number | null = null;  

  //Keep tabs on the parent party type
  parentPartyType: PartyType | null | undefined;

  //Keep tabs on the selected parent party id
  selectedParentPartyId: number | null = null;

  //Keep tabs on the subsidiary party type
  subsidiaryPartyType: PartyType | null | undefined;

  //Keep tabs on the selected subsidiary parties ids
  selectedSubsidiaryPartiesIds: number[] = [];

  // Instantiate a string to host Global Error Messages that cannot be handled by the Reactive Form Group
  // These include errors related to Categories / Units Selections
  public error: string | null = null;

  // Instantiate a string to host the currently visible content
  // Content options include: parents and subsidiaries
  page: string = "parents";


  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private accountabilitiesRulesDataService: AccountabilitiesRulesDataService,
    private partiesTypesDataService: PartiesTypesDataService,
    private accountabilitiesDataService: AccountabilitiesDataService,
    private cd: ChangeDetectorRef,
    private log: NGXLogger) { }

  ngOnInit() {
    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    this.initPartiesTypes(
      () => this.initAccountabilitiesRules(
        () => this.init()));
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

  init() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    if (this.accountabilityRuleId) {

      // Get the Accountability Rule Record corresponding to the Accountability Rule id
      this.log.trace(`${LOG_PREFIX} Getting the Accountability Rule Record corresponding to the Accountability Rule id`);
      const accountabilityRule: AccountabilityRule | undefined = this.accountabilitiesRulesDataService.records.find((a: AccountabilityRule) => a.id == this.accountabilityRuleId);
      this.log.debug(`${LOG_PREFIX} Accountability Rule = ${JSON.stringify(accountabilityRule)}`);

      if (accountabilityRule) {

        // Get the Accountability Type id
        this.log.trace(`${LOG_PREFIX} Getting the Accountability Type id`);
        this.accountabilityTypeId = accountabilityRule.accountabilityTypeId;
        this.log.debug(`${LOG_PREFIX} Accountability Type Id = ${JSON.stringify(this.accountabilityTypeId)}`);


        if (accountabilityRule.parentPartyTypeId) {

          // Get the Parent Party Type Record
          this.log.trace(`${LOG_PREFIX} Getting the Parent Party Type Record`);
          this.parentPartyType = this.partiesTypesDataService.records.find((p: PartyType) => p.id == accountabilityRule.parentPartyTypeId);
          this.log.debug(`${LOG_PREFIX} Parent Party Type = ${JSON.stringify(this.parentPartyType)}`);

        }

        if (accountabilityRule.subsidiaryPartyTypeId) {

          // Get the Subsidiary Party Type Record
          this.log.trace(`${LOG_PREFIX} Getting the Subsidiary Party Type Record`);
          this.subsidiaryPartyType = this.partiesTypesDataService.records.find((p: PartyType) => p.id == accountabilityRule.subsidiaryPartyTypeId);
          this.log.debug(`${LOG_PREFIX} Subsidiary Party Type = ${JSON.stringify(this.subsidiaryPartyType)}`);

        }
      }
    }
  }


  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  /**
   * Internal validator that checks whether a Accountability record already exists
   * @returns 
   */
  private exists(): ValidatorFn {


    const values: string[] = <Array<string>>this.accountabilitiesDataService.records.map(d => d.name);

    return (control: AbstractControl): ValidationErrors | null => {

      if (values != null && values.length > 0) {

        if (control.value) {

          const s: string = control.value;

          if (values.map(v => v.toLowerCase()).includes(s.toLowerCase())) {
            return { 'exists': true }
          }
        }
      }


      return null;
    }
  }

  /**
   * Validates and saves a new Accountability record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.validateSelectedIds()) {

      // Create a container to host the details of the incoming accountabilities records
      this.log.trace(`${LOG_PREFIX} Creating a container to host the details of the incoming accountabilities records`);
      const accountabilities: Accountability[] = [];

      // Loop through each subsidiary record and create a corresponding accountability record
      this.log.trace(`${LOG_PREFIX} Looping through each subsidiary record and creating a corresponding accountability record`);
      this.selectedSubsidiaryPartiesIds.forEach((id: number) => {
        accountabilities.push(
          new Accountability({
            accountabilityTypeId: this.accountabilityTypeId,
            accountabilityRuleId: this.accountabilityRuleId,
            parentPartyId: this.selectedParentPartyId,
            subsidiaryPartyId: id
          })
        );
      });

      this.log.debug(`${LOG_PREFIX} Accountabilities = ${JSON.stringify(accountabilities)}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Accountabilities record`);
      this.accountabilitiesDataService
        .createAccountabilities(accountabilities)
        .subscribe(
          (response: Accountability[]) => {

            // The Accountabilities records were saved successfully
            this.log.trace(`${LOG_PREFIX} The Accountabilities records were saved successfully`);

            // Reset the currently visible form page
            this.log.trace(`${LOG_PREFIX} Resetting the current visible form page`);
            this.page = "parents";

            // Reset the selected Ids
            this.log.trace(`${LOG_PREFIX} Resetting the selected ids`);
            this.selectedParentPartyId = null;
            this.selectedSubsidiaryPartiesIds = []

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Accountabilities records were not saved successfully
            this.log.trace(`${LOG_PREFIX} Accountabilities records were not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Emit an 'invalid' event
      this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
      this.failed.emit(400);
    }
  }


  /**
   * Load the next page if the current one is valid
   */
  public onNext() {

    if (this.validateSelectedIds()) {

      // Load the next page

      this.log.trace(`${LOG_PREFIX} Showing Subsidiary Party Selection Form`);
      this.page = "subsidiaries";
      this.loadedNext.emit(true);

      this.cd.detectChanges();

    } else {

      this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
      this.failed.emit(400);
    }
  }


  /**
  * Load the previous page
  */
  public onPrevious() {

    this.log.trace(`${LOG_PREFIX} Showing Parent Selection Form`);
    this.page = "parents";
    this.loadedPrevious.emit(true);
  }


  /** 
  * Capture the selected id
  * @param id The selected id
  */
  onSelect(id: number) {

    this.log.trace(`${LOG_PREFIX} Selected Id: ${id}`);

    switch (this.page) {

      case "parents":

        // Set the selected id as the parent id
        this.selectedParentPartyId = id;

        break;

      case "subsidiaries":

        // Clear the selected ids since only a single selection is required
        this.selectedSubsidiaryPartiesIds = [];

        // Insert the newly selected id into the selected ids array
        this.selectedSubsidiaryPartiesIds.push(id);

        break;
    }

  }


  /** 
  * Adds checked ids to the checked ids array
  * @param id The checked id
  */
  onCheck(id: number) {

    this.log.trace(`${LOG_PREFIX} Selected Id: ${id}`);

    switch (this.page) {

      case "parents":

        // Set the selected id as the parent id
        this.selectedParentPartyId = id;

        break;

      case "subsidiaries":

        // Insert the newly checked id into the checked ids array
        this.selectedSubsidiaryPartiesIds.push(id);

        break;
    }

  }


  /** 
  * Removes checked ids from the checked ids array
  * @param id The unchecked id
  */
  onUncheck(id: number) {

    this.log.trace(`${LOG_PREFIX} Unchecked Id: ${id}`);

    switch (this.page) {

      case "parents":

        // Unset the selected parent id
        this.selectedParentPartyId = null;

        break;

      case "subsidiaries":

        // Removes the Unchecked id from the selected ids array - if in existence
        let index: number = this.selectedSubsidiaryPartiesIds.indexOf(id);
        if (index != -1) {
          this.selectedSubsidiaryPartiesIds.splice(index, 1);
        }

        break;
    }

  }

  private validateSelectedIds(): boolean {

    switch (this.page) {

      case "parents":

        if (this.selectedParentPartyId == null) {

          if (this.parentPartyType) {

            // Print an error message
            this.log.trace(`${LOG_PREFIX} Printing an error message`);
            this.showSelectedIdsError("Please select the Parent Administrative Unit");
            return false;

          }

        }

        break;

      case "subsidiaries":


        if (this.selectedSubsidiaryPartiesIds.length == 0) {

          // Print an error message
          this.log.trace(`${LOG_PREFIX} Printing an error message`);
          this.showSelectedIdsError("Please select the Subsidiary Administrative Units");
          return false;

        }

        break;
    }

    return true;

  }


  private showSelectedIdsError(msg: string): void {
    this.error = msg;

    setTimeout(() => {
      console.log('clearing error');
      this.error = null;
      this.cd.detectChanges();
    }, 7500);

  }







}


