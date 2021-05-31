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
import { ReportingVariable } from '@modules/reporting-variables/models/reporting-variable.model';
import { ReportingVariablesDataService } from '@modules/reporting-variables/services';

const LOG_PREFIX: string = "[Reporting Variables Records Updation Component]";

@Component({
  selector: 'sb-reporting-variables-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reporting-variables-records-updation.component.html',
  styleUrls: ['reporting-variables-records-updation.component.scss'],
})
export class ReportingVariablesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate and avail the target Reporting Framework variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // Reporting Framework  
  @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();  

  // Instantiate an object to hold the details of the Reporting Variable record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  reportingVariable: ReportingVariable | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Reporting Variables Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  reportingVariablesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists("name")
    ]),   
    description: new FormControl('', [
      Validators.maxLength(250),
      this.exists("description")
    ]),
  });

  constructor(
    public reportingFrameworksDataService: ReportingFrameworksDataService,
    private reportingVariablesDataService: ReportingVariablesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Reporting Variable record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Reporting Variable record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Reporting Variable record Id = ${this.id}`);
    this.reportingVariable = this.reportingVariablesDataService.records.find(d => d.id == this.id);
  }

  ngAfterContentInit() {

    // If the Reporting Variable record has been successfully initialize, use it to initialize the form fields
    if (this.reportingVariable != undefined) {

      // The Reporting Variable record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Reporting Variable record = ${JSON.stringify(this.reportingVariable)}`);

      // Initialize the Reporting Variable records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Reporting Variable records form fields`);
      this.reportingVariablesForm.setValue({
        name: (this.reportingVariable.name)? this.reportingVariable.name : '',
        description: (this.reportingVariable.description)? this.reportingVariable.description : ''
      });

    } else {
      this.log.error(`${LOG_PREFIX} Reporting Variable record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Reporting Variable record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.reportingVariablesDataService.records != null && this.reportingVariablesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const reportingVariable: ReportingVariable | undefined | null =
            attribute == "name" ? this.reportingVariablesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
                  null;

          if (reportingVariable != undefined && reportingVariable.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Reporting Variable record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.reportingVariablesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.reportingVariablesForm.get('name') == null ? null : this.reportingVariablesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Reporting Variable Name = ${name}`);    

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.reportingVariablesForm.get('description') == null ? null : this.reportingVariablesForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Reporting Variable Description = ${description}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Reporting Variable record`);
      this.reportingVariablesDataService
        .updateReportingVariable(Object.assign(this.reportingVariable, { reportingFrameworkId: this.targetReportingFramework?.id, name: name, description: description}))
        .subscribe(
          (response: ReportingVariable) => {

            // The Reporting Variable record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Reporting Variable record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.reportingVariablesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Reporting Variable record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Reporting Variable record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.reportingVariablesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.reportingVariablesForm);

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
