import {
    AfterContentInit,
    ChangeDetectionStrategy,
    Component,
    EventEmitter,
    Input,
    OnInit,
    Output
  } from '@angular/core';
  import { DomainsDataService } from '../../services/domains-data.service';
  import { NGXLogger } from 'ngx-logger';
  import { Subscription } from 'rxjs';
  import { Domain } from '@modules/domains/models';
  
  const LOG_PREFIX: string = "[Domains Records Deletion Component]";
  
  @Component({
    selector: 'sb-domains-records-deletion',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './domains-records-deletion.component.html',
    styleUrls: ['domains-records-deletion.component.scss'],
  })
  export class DomainsRecordsDeletionComponent implements OnInit, AfterContentInit {

    // Instantiate and avail the id variable to the parent component
    @Input() id!: number;

    // Instantiate an object to hold the details domain record being deleted
    domain: Domain | undefined;
  
    // Instantiate a succeeded notification Emitter.
    @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();
  
    // Instantiate a failed notification Emitter.
    @Output() failed: EventEmitter<number> = new EventEmitter<number>();
  
    // Instantiate a gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscription: Subscription = new Subscription();
  
    constructor(private domainsDataService: DomainsDataService, private log: NGXLogger) { }
  
    ngOnInit() { }

    ngAfterContentInit(){

        // Retrieve the domain with the given id from the data store 
        this.log.trace(`${LOG_PREFIX} Retrieving the domain with the given id from the data store`);
        this.log.debug(`${LOG_PREFIX} Domain Record Id = ${this.id}`);
        this.domain = this.domainsDataService.records.find(d => d.id == this.id);
        this.log.debug(`${LOG_PREFIX} Domain Record = ${JSON.stringify(this.domain)}`);
    }
  
    ngOnDestroy() {
  
      // Clear all subscriptions
      this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
      this._subscription.unsubscribe();
    }

  
    /**
     * Deletes domain record.
     * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
     * Error 500 = Indicates something unexpected happened at the server side
     */
    public delete(): void {
  
        // Save the record
        this.log.trace(`${LOG_PREFIX} Deleting the domain record`);
        this.domainsDataService
          .deleteDomain(this.id)
          .subscribe(
            (response: number) => {
  
              // The domain record was saved successfully
              this.log.trace(`${LOG_PREFIX} Domain record was deleted successfuly`);

  
              // Emit a 'succeeded' event
              this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
              this.succeeded.emit();
            },
            (error: any) => {
  
              // The domain record was not deleted successfully
              this.log.trace(`${LOG_PREFIX} Domain record was not deleted successfuly`);
  
              // Emit a 'failed' event
              this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
              this.failed.emit(500);
            });

    }
  
  
  }
  