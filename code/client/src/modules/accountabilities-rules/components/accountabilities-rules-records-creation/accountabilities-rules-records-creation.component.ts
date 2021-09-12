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
import { AccountabilityRule } from '@modules/accountabilities-rules/models/accountability-rule.model';
import { AccountabilitiesRulesDataService } from '@modules/accountabilities-rules/services/accountabilities-rules-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Accountabilities Rules Records Creation Component]";

@Component({
  selector: 'sb-accountabilities-rules-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './accountabilities-rules-records-creation.component.html',
  styleUrls: ['accountabilities-rules-records-creation.component.scss'],
})
export class AccountabilitiesRulesRecordsCreationComponent implements OnInit {

  // Instantiate and avail the accountability type id variable to the parent component.
  // This will allow the parent component to inject the details of the target parent accountability type
  @Input() accountabilityTypeId: number | null = null;

  // Instantiate and avail a previously selected Party Types Ids arrays to the parent component.
  // This will allow the parent component to specify the previously selected Party Types Ids.
  // This might then be used to disable the previously selected Party Types Ids if the next configuration is set to true
  @Input() previouslySelectedPartiesTypesIds: number[] = [];

  // Instantiate a selected ids array.
  // When a user selects a party type, add its corresponding id to the array.
  // Conversely, remove the id when its deselected
  @Input() selectedPartiesTypesIds: number[] = [];

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate a string to host Global Error Messages that cannot be handled by the Reactive Form Group
  // These include errors related to Categories / Units Selections
  public error: string | null = null;

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private accountabilitiesRulesDataService: AccountabilitiesRulesDataService,
    private cd: ChangeDetectorRef,
    private log: NGXLogger) { }

  ngOnInit() {
    this.log.trace(`${LOG_PREFIX} Initializing Component`);
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }


  /**
   * Validates and saves a new Accountability Rule record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.validateSelectedIds()) {

      // Make a copy of the current accountability rules that belong to the specified accountability type
      this.log.trace(`${LOG_PREFIX} Making a copy of the current accountability rules that belong to the specified accountability type`);
      let accountabilityRules: AccountabilityRule[] = Object.assign([], this.accountabilitiesRulesDataService.records.filter((a: AccountabilityRule) => a.accountabilityTypeId == this.accountabilityTypeId));

      // Sort the current accountability rules in ascending order by id
      this.log.trace(`${LOG_PREFIX} Sorting the current accountability rules in ascending order by id`);
      if (accountabilityRules.length > 0) {
        accountabilityRules = accountabilityRules.sort((n1, n2) => {
          if (n1 > n2) {
            return 1;
          }

          if (n1 < n2) {
            return -1;
          }

          return 0;
        });
      }

      // Get the Parent Party Type Id
      this.log.trace(`${LOG_PREFIX} Getting the Parent Party Type Id`);
      let parentPartyTypeId: number | null = null;
      if(accountabilityRules.length > 0){
        parentPartyTypeId = (accountabilityRules[accountabilityRules.length - 1]).subsidiaryPartyTypeId;
      }
      this.log.trace(`${LOG_PREFIX} Parent Party Type Id = ${parentPartyTypeId}`);

      // Read in the subsidiary id
      this.log.trace(`${LOG_PREFIX} Reading in the Subsidiary Party Type Id`);
      const subsidiaryPartyTypeId: number | null = this.selectedPartiesTypesIds[0];
      this.log.debug(`${LOG_PREFIX} Subsidiary Party Type Id = ${subsidiaryPartyTypeId}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Accountability Rule record`);
      this.accountabilitiesRulesDataService
        .createAccountabilityRule(new AccountabilityRule({ accountabilityTypeId: this.accountabilityTypeId, parentPartyTypeId, subsidiaryPartyTypeId}))
        .subscribe(
          (response: AccountabilityRule) => {

            // The Accountability Rule record was saved successfully
            this.log.trace(`${LOG_PREFIX} Accountability Rule record was saved successfuly`);

            // Reset the selected Idss
            this.log.trace(`${LOG_PREFIX} Resetting the selected ids`);
            this.selectedPartiesTypesIds = [];

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Accountability Rule record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Accountability Rule record was not saved successfuly`);

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
  * Adds selected ids to the selected ids array
  * @param id The selected id
  */
  onSelect(id: number) {

    this.log.trace(`${LOG_PREFIX} Selected Id: ${id}`);

    // Clear the selected ids since only a single selection is required
    this.selectedPartiesTypesIds = [];

    // Insert the newly selected id into the selected ids array
    this.selectedPartiesTypesIds.push(id);

  }


  /** 
  * Adds checked ids to the checked ids array
  * @param id The checked id
  */
  onCheck(id: number) {

    this.log.trace(`${LOG_PREFIX} Selected Id: ${id}`);

    // Insert the newly checked id into the checked ids array
    this.selectedPartiesTypesIds.push(id);

  }


  /** 
  * Removes checked ids from the checked ids array
  * @param id The unchecked id
  */
  onUncheck(id: number) {

    this.log.trace(`${LOG_PREFIX} Unchecked Id: ${id}`);

    // Removes the Unchecked id from the selected ids array - if in existence
    let index: number = this.selectedPartiesTypesIds.indexOf(id);
    if (index != -1) {
      this.selectedPartiesTypesIds.splice(index, 1);
    }
  }



  private validateSelectedIds(): boolean {

    if (this.selectedPartiesTypesIds.length == 0) {
      this.showSelectedIdsError("Please select the target level's administrative unit type");
      return false;
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


