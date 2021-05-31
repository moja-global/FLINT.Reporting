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
import { ReportingFramework } from '@modules/reporting-frameworks/models';
import { LandUseCategory } from '@modules/land-use-categories/models';
import { LandUseCategoriesDataService } from '@modules/land-use-categories/services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Land Use Categories Records Deletion Component]";

@Component({
  selector: 'sb-land-use-categories-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './land-use-categories-records-deletion.component.html',
  styleUrls: ['land-use-categories-records-deletion.component.scss'],
})
export class LandUseCategoriesRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate and avail the target landUseCategory category variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // landUseCategory category  
  @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();  

  // Instantiate an object to hold the details of the Land Use Category record being deleted.
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

  constructor(private landUseCategoriesDataService: LandUseCategoriesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Land Use Category with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Land Use Category with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Land Use Category record Id = ${this.id}`);
    this.landUseCategory = this.landUseCategoriesDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Land Use Category record = ${JSON.stringify(this.landUseCategory)}`);

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
   * Deletes Land Use Category record.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Save the record
    this.log.trace(`${LOG_PREFIX} Deleting the Land Use Category record`);
    this.landUseCategoriesDataService
      .deleteLandUseCategory(this.id)
      .subscribe(
        (response: number) => {

          // The Land Use Category record was saved successfully
          this.log.trace(`${LOG_PREFIX} Land Use Category record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Land Use Category record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Land Use Category record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
