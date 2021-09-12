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
import { ReportingFramework } from '@modules/reporting-frameworks/models/reporting-framework.model';
import { ReportingFrameworksDataService } from '@modules/reporting-frameworks/services/reporting-frameworks-data.service';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Reporting Frameworks Records Updation Component]";

@Component({
  selector: 'sb-reporting-frameworks-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reporting-frameworks-records-updation.component.html',
  styleUrls: ['reporting-frameworks-records-updation.component.scss'],
})
export class ReportingFrameworksRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Reporting Framework record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  reportingFramework: ReportingFramework | undefined = new ReportingFramework();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the ReportingFrameworks Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  reportingFrameworksForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
      this.exists("name")
    ]),
    description: new FormControl('', [
      Validators.maxLength(250)
    ])    
  });

  constructor(
    private reportingFrameworksDataService: ReportingFrameworksDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Reporting Framework record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Reporting Framework record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Reporting Framework record Id = ${this.id}`);

    const temp: ReportingFramework | undefined = (this.id == null || this.id == undefined) ? undefined : this.reportingFrameworksDataService.records.find(d => d.id == this.id);
    this.reportingFramework = (temp == undefined) ? new ReportingFramework() : temp;

  }

  ngAfterContentInit() {

    // If the Reporting Framework record has been successfully initialize, use it to initialize the form fields
    if (this.reportingFramework != undefined) {

      // The Reporting Framework record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Reporting Framework record = ${JSON.stringify(this.reportingFramework)}`);

      // Initialize the Reporting Framework records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Reporting Framework records form fields`);
      this.reportingFrameworksForm.setValue({
        name: (this.reportingFramework.name) ? this.reportingFramework.name : "",
        description: (this.reportingFramework.description) ? this.reportingFramework.description : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} Reporting Framework record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Reporting Framework record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.reportingFrameworksDataService.records != null && this.reportingFrameworksDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          let reportingFramework: ReportingFramework | undefined | null =
            attribute == "name" ? this.reportingFrameworksDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) : null;

          if (reportingFramework != undefined && reportingFramework.id != this.id) {
            return { 'exists': true }
          }

        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Reporting Framework record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.reportingFrameworksForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.reportingFrameworksForm.get('name') == null ? null : this.reportingFrameworksForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Reporting Framework Name = ${name}`);   

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.reportingFrameworksForm.get('description') == null ? null : this.reportingFrameworksForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Reporting Framework Name = ${description}`);      

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Reporting Framework record`);
      this.reportingFrameworksDataService
        .updateReportingFramework(Object.assign(this.reportingFramework, { name: name, description: description }))
        .subscribe(
          (response: ReportingFramework) => {

            // The Reporting Framework record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Reporting Framework record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.reportingFrameworksForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Reporting Framework record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Reporting Framework record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.reportingFrameworksForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.reportingFrameworksForm);

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
