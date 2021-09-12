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
import { LandUseCategory } from '@modules/land-uses-categories/models/land-use-category.model';
import { LandUsesCategoriesDataService } from '@modules/land-uses-categories/services/land-uses-categories-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Land Uses Categories Records Deletion Component]";

@Component({
  selector: 'sb-land-uses-categories-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './land-uses-categories-records-deletion.component.html',
  styleUrls: ['land-uses-categories-records-deletion.component.scss'],
})
export class LandUsesCategoriesRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Land Use Category Record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  landUseCategory: LandUseCategory | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();


  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private landUsesCategoriesDataService: LandUsesCategoriesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the LandUseCategory with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the LandUseCategory with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Land Use Category Record Id = ${this.id}`);
    this.landUseCategory = this.landUsesCategoriesDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Land Use Category Record = ${JSON.stringify(this.landUseCategory)}`);

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
   * Deletes Land Use Category Records.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Delete the record
    this.log.trace(`${LOG_PREFIX} Deleting the Land Use Category Record`);
    this.landUsesCategoriesDataService
      .deleteLandUseCategory(this.id)
      .subscribe(
        (response: number) => {

          // The Land Use Category Record was deleted successfully
          this.log.trace(`${LOG_PREFIX} Land Use Category Record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Land Use Category Record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Land Use Category Record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
