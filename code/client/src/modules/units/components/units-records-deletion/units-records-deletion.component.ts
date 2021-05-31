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
import { UnitsDataService } from '../../services/units-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { Unit } from '../../../units/models/unit.model';
import { UnitCategory } from '@modules/unit-categories/models/unit-category.model';

const LOG_PREFIX: string = "[Units Records Deletion Component]";

@Component({
  selector: 'sb-units-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './units-records-deletion.component.html',
  styleUrls: ['units-records-deletion.component.scss'],
})
export class UnitsRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate and avail the target unit category variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // unit category  
  @Input() targetUnitCategory: UnitCategory = new UnitCategory(); 

  // Instantiate an object to hold the details of the Unit record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  unit: Unit | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();


  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private unitsDataService: UnitsDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Unit with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Unit with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Unit record Id = ${this.id}`);
    this.unit = this.unitsDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Unit record = ${JSON.stringify(this.unit)}`);

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
   * Deletes Unit record.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Save the record
    this.log.trace(`${LOG_PREFIX} Deleting the Unit record`);
    this.unitsDataService
      .deleteUnit(this.id)
      .subscribe(
        (response: number) => {

          // The Unit record was saved successfully
          this.log.trace(`${LOG_PREFIX} Unit record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Unit record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Unit record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
