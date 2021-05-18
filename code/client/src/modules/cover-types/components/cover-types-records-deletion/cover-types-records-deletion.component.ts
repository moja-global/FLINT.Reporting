import {
  AfterContentInit,
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output
} from '@angular/core';
import { CoverTypesDataService } from '../../services/cover-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { CoverType } from '../../../cover-types/models/cover-type.model';

const LOG_PREFIX: string = "[Cover Types Records Deletion Component]";

@Component({
  selector: 'sb-cover-types-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './cover-types-records-deletion.component.html',
  styleUrls: ['cover-types-records-deletion.component.scss'],
})
export class CoverTypesRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Cover Type record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  coverType: CoverType | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private coverTypesDataService: CoverTypesDataService, private log: NGXLogger) { }

  ngOnInit() { 

    // Retrieve the Cover Type with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Cover Type with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Cover Type record Id = ${this.id}`);
    this.coverType = this.coverTypesDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Cover Type record = ${JSON.stringify(this.coverType)}`);

  }

  ngAfterContentInit() {

  }

  ngOnDestroy() {

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }


  /**
   * Deletes Cover Type record.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Save the record
    this.log.trace(`${LOG_PREFIX} Deleting the Cover Type record`);
    this.coverTypesDataService
      .deleteCoverType(this.id)
      .subscribe(
        (response: number) => {

          // The Cover Type record was saved successfully
          this.log.trace(`${LOG_PREFIX} Cover Type record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Cover Type record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Cover Type record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
