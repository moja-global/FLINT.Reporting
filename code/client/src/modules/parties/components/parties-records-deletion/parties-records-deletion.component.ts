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
import { Party } from '@modules/parties/models/party.model';
import { PartiesDataService } from '@modules/parties/services/parties-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Parties Records Deletion Component]";

@Component({
  selector: 'sb-parties-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './parties-records-deletion.component.html',
  styleUrls: ['parties-records-deletion.component.scss'],
})
export class PartiesRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Party Record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  party: Party | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();


  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private partiesDataService: PartiesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Party with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Party with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Party Record Id = ${this.id}`);
    this.party = this.partiesDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Party Record = ${JSON.stringify(this.party)}`);

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
   * Deletes Party Records.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Delete the record
    this.log.trace(`${LOG_PREFIX} Deleting the Party Record`);
    this.partiesDataService
      .deleteParty(this.id)
      .subscribe(
        (response: number) => {

          // The Party Record was deleted successfully
          this.log.trace(`${LOG_PREFIX} Party Record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Party Record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Party Record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
