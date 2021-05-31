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
import { PartyTypesDataService } from '../../services/party-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { PartyType } from '../../../party-types/models/party-type.model';

const LOG_PREFIX: string = "[Party Types Records Deletion Component]";

@Component({
  selector: 'sb-party-types-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './party-types-records-deletion.component.html',
  styleUrls: ['party-types-records-deletion.component.scss'],
})
export class PartyTypesRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Party Type record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  partyType: PartyType | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Keep tabs on the target part type
  @Input() targetPartyType!: PartyType | undefined;

  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private partyTypesDataService: PartyTypesDataService,
    private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);  

    // Retrieve the Party Type with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Party Type with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Party Type record Id = ${this.id}`);
    this.partyType = this.partyTypesDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Party Type record = ${JSON.stringify(this.partyType)}`);

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
   * Deletes Party Type record.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Save the record
    this.log.trace(`${LOG_PREFIX} Deleting the Party Type record`);
    this.partyTypesDataService
      .deletePartyType(this.id)
      .subscribe(
        (response: number) => {

          // The Party Type record was saved successfully
          this.log.trace(`${LOG_PREFIX} Party Type record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Party Type record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Party Type record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
