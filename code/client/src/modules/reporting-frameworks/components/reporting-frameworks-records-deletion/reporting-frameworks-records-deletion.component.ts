import {
  AfterContentInit,
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output
} from '@angular/core';
import { ReportingFrameworksDataService } from '../../services/reporting-frameworks-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { ReportingFramework } from '../../../reporting-frameworks/models/reporting-framework.model';

const LOG_PREFIX: string = "[Reporting Frameworks Records Deletion Component]";

@Component({
  selector: 'sb-reporting-frameworks-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reporting-frameworks-records-deletion.component.html',
  styleUrls: ['reporting-frameworks-records-deletion.component.scss'],
})
export class ReportingFrameworksRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Reporting Framework record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  reportingFramework: ReportingFramework | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private reportingFrameworksDataService: ReportingFrameworksDataService, private log: NGXLogger) { }

  ngOnInit() { 

    // Retrieve the Reporting Framework with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Reporting Framework with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Reporting Framework record Id = ${this.id}`);
    this.reportingFramework = this.reportingFrameworksDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Reporting Framework record = ${JSON.stringify(this.reportingFramework)}`);

  }

  ngAfterContentInit() {

  }

  ngOnDestroy() {

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }


  /**
   * Deletes Reporting Framework record.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Save the record
    this.log.trace(`${LOG_PREFIX} Deleting the Reporting Framework record`);
    this.reportingFrameworksDataService
      .deleteReportingFramework(this.id)
      .subscribe(
        (response: number) => {

          // The Reporting Framework record was saved successfully
          this.log.trace(`${LOG_PREFIX} Reporting Framework record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Reporting Framework record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Reporting Framework record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
