import {
  AfterContentInit,
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output
} from '@angular/core';
import { AccountabilityType } from '@modules/accountabilities-types/models/accountability-type.model';
import { AccountabilitiesTypesDataService } from '@modules/accountabilities-types/services/accountabilities-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Accountabilities Types Records Deletion Component]";

@Component({
  selector: 'sb-accountabilities-types-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './accountabilities-types-records-deletion.component.html',
  styleUrls: ['accountabilities-types-records-deletion.component.scss'],
})
export class AccountabilitiesTypesRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the AccountabilityType record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  accountabilityType: AccountabilityType | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();


  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private accountabilitiesTypesDataService: AccountabilitiesTypesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the AccountabilityType with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the AccountabilityType with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} AccountabilityType record Id = ${this.id}`);
    this.accountabilityType = this.accountabilitiesTypesDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} AccountabilityType record = ${JSON.stringify(this.accountabilityType)}`);

  }

  ngAfterContentInit() {

  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }


  /**
   * Deletes AccountabilityType records.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Delete the record
    this.log.trace(`${LOG_PREFIX} Deleting the AccountabilityType record`);
    this.accountabilitiesTypesDataService
      .deleteAccountabilityType(this.id)
      .subscribe(
        (response: number) => {

          // The AccountabilityType record was deleted successfully
          this.log.trace(`${LOG_PREFIX} AccountabilityType record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The AccountabilityType record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} AccountabilityType record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
