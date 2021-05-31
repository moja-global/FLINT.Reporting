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
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';
import { ReportingFrameworksDataService } from '@modules/reporting-frameworks/services';
import { ReportingFramework } from '@modules/reporting-frameworks/models';
import { ReportingTable } from '@modules/reporting-tables/models/reporting-table.model';
import { ReportingTablesDataService } from '@modules/reporting-tables/services';

const LOG_PREFIX: string = "[Reporting Tables Records Updation Component]";

@Component({
  selector: 'sb-reporting-tables-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reporting-tables-records-updation.component.html',
  styleUrls: ['reporting-tables-records-updation.component.scss'],
})
export class ReportingTablesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate and avail the target reportingTable category variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // reportingTable category  
  @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();  

  // Instantiate an object to hold the details of the Reporting Table record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  reportingTable: ReportingTable | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Reporting Tables Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  reportingTablesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists("name")
    ]),
    number: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists("number")
    ]),    
    description: new FormControl('', [
      Validators.maxLength(250),
      this.exists("description")
    ]),
  });

  constructor(
    public reportingFrameworksDataService: ReportingFrameworksDataService,
    private reportingTablesDataService: ReportingTablesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Reporting Table record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Reporting Table record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Reporting Table record Id = ${this.id}`);
    this.reportingTable = this.reportingTablesDataService.records.find(d => d.id == this.id);
  }

  ngAfterContentInit() {

    // If the Reporting Table record has been successfully initialize, use it to initialize the form fields
    if (this.reportingTable != undefined) {

      // The Reporting Table record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Reporting Table record = ${JSON.stringify(this.reportingTable)}`);

      // Initialize the Reporting Table records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Reporting Table records form fields`);
      this.reportingTablesForm.setValue({
        name: this.reportingTable.name,
        number: this.reportingTable.number,
        description: this.reportingTable.description
      });

    } else {
      this.log.error(`${LOG_PREFIX} Reporting Table record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Reporting Table record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.reportingTablesDataService.records != null && this.reportingTablesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const reportingTable: ReportingTable | undefined | null =
            attribute == "name" ? this.reportingTablesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
              attribute == "number" ? this.reportingTablesDataService.records.find(v => v.number?.toLowerCase() == s.toLowerCase()) :
                  null;

          if (reportingTable != undefined && reportingTable.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Reporting Table record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.reportingTablesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.reportingTablesForm.get('name') == null ? null : this.reportingTablesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Reporting Table Name = ${name}`);

      // Read in the provided number
      this.log.trace(`${LOG_PREFIX} Reading in the provided number`);
      const number: string | null = this.reportingTablesForm.get('number') == null ? null : this.reportingTablesForm.get('number')?.value;
      this.log.debug(`${LOG_PREFIX} Reporting Table Number = ${number}`);      

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.reportingTablesForm.get('description') == null ? null : this.reportingTablesForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Reporting Table Description = ${description}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Reporting Table record`);
      this.reportingTablesDataService
        .updateReportingTable(Object.assign(this.reportingTable, { reportingFrameworkId: this.targetReportingFramework?.id, name: name, number: number, description: description}))
        .subscribe(
          (response: ReportingTable) => {

            // The Reporting Table record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Reporting Table record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.reportingTablesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Reporting Table record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Reporting Table record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.reportingTablesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.reportingTablesForm);

      // Emit an 'invalid' event
      this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
      this.failed.emit(400);
    }
  }

  /**
   * See: https://loiane.com/2017/08/angular-reactive-forms-trigger-validation-on-submit
   * @param formGroup 
   */
  private validateAllFormFields(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsTouched({ onlySelf: true });
      } else if (control instanceof FormGroup) {
        this.validateAllFormFields(control);
      }
    });
  }


}
