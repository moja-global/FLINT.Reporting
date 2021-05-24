import {
  AfterContentInit,
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnDestroy,
  OnInit,
  Output
} from '@angular/core';
import { EmissionTypesDataService } from '../../services/emission-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { EmissionType } from '../../../emission-types/models/emission-type.model';

const LOG_PREFIX: string = "[Emission Types Records Deletion Component]";

@Component({
  selector: 'sb-emission-types-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './emission-types-records-deletion.component.html',
  styleUrls: ['emission-types-records-deletion.component.scss'],
})
export class EmissionTypesRecordsDeletionComponent implements OnInit, OnDestroy {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Emission Type record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  emissionType: EmissionType | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private emissionTypesDataService: EmissionTypesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Emission Type with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Emission Type with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Emission Type record Id = ${this.id}`);
    this.emissionType = this.emissionTypesDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Emission Type record = ${JSON.stringify(this.emissionType)}`);

  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
    
    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }


  /**
   * Deletes Emission Type record.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Save the record
    this.log.trace(`${LOG_PREFIX} Deleting the Emission Type record`);
    this.emissionTypesDataService
      .deleteEmissionType(this.id)
      .subscribe(
        (response: number) => {

          // The Emission Type record was saved successfully
          this.log.trace(`${LOG_PREFIX} Emission Type record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Emission Type record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Emission Type record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
