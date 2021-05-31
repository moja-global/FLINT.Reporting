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
import { ReportingTable } from '@modules/reporting-tables/models';
import { ReportingTablesDataService } from '@modules/reporting-tables/services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Reporting Tables Records Deletion Component]";

@Component({
  selector: 'sb-reporting-tables-records-deletion',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reporting-tables-records-deletion.component.html',
  styleUrls: ['reporting-tables-records-deletion.component.scss'],
})
export class ReportingTablesRecordsDeletionComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for deletion during the component's initialization.
  @Input() id!: number;

  // Instantiate and avail the target reportingTable category variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // reportingTable category  
  @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();  

  // Instantiate an object to hold the details of the Reporting Table record being deleted.
  // This will allow the UI to print readable details of that record that 
  // for the users confirmation
  reportingTable: ReportingTable | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful deletion events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed deletion events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();


  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private reportingTablesDataService: ReportingTablesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Reporting Table with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Reporting Table with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Reporting Table record Id = ${this.id}`);
    this.reportingTable = this.reportingTablesDataService.records.find(d => d.id == this.id);
    this.log.debug(`${LOG_PREFIX} Reporting Table record = ${JSON.stringify(this.reportingTable)}`);

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
   * Deletes Reporting Table record.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public delete(): void {

    // Save the record
    this.log.trace(`${LOG_PREFIX} Deleting the Reporting Table record`);
    this.reportingTablesDataService
      .deleteReportingTable(this.id)
      .subscribe(
        (response: number) => {

          // The Reporting Table record was saved successfully
          this.log.trace(`${LOG_PREFIX} Reporting Table record was deleted successfuly`);

          // Emit a 'succeeded' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
          this.succeeded.emit();
        },
        (error: any) => {

          // The Reporting Table record was not deleted successfully
          this.log.trace(`${LOG_PREFIX} Reporting Table record was not deleted successfuly`);

          // Emit a 'failed' event
          this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
          this.failed.emit(500);
        });

  }


}
