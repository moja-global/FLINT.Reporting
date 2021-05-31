import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnDestroy,
  OnInit,
  Output
} from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ReportingTablesDataService } from '../../services/reporting-tables-data.service';
import { NGXLogger } from 'ngx-logger';
import { ReportingTable } from '@modules/reporting-tables/models/reporting-table.model';
import { ReportingFrameworksDataService } from '@modules/reporting-frameworks/services';
import { Subject } from 'rxjs';
import { ReportingFramework } from '@modules/reporting-frameworks/models';

const LOG_PREFIX: string = "[Reporting Tables Records Creation Component]";

@Component({
  selector: 'sb-reporting-tables-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reporting-tables-records-creation.component.html',
  styleUrls: ['reporting-tables-records-creation.component.scss'],
})
export class ReportingTablesRecordsCreationComponent implements OnInit, OnDestroy {

  // Instantiate an 'initialized' state notification Emitter.
  // This will allow us to notify the parent component that the component was successfully initialized
  @Output() initialized: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate and avail the target reportingTable category variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // reportingTable category  
  @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();  

  // Instantiate a notifier.
  // This is simply an observable that we will use to opt 
  // out of initialization subscriptions once we are done with them
  private _done$ = new Subject<boolean>();

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
  }


  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }


  /**
   * Internal validator that checks whether a Reporting Table record already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    this.log.trace(`${LOG_PREFIX} Checking whether ${attribute} already exists`);

    const values: string[] =
      attribute == "name" ? <Array<string>>this.reportingTablesDataService.records.map(d => d.name) :
        attribute == "number" ? <Array<string>>this.reportingTablesDataService.records.map(d => d.number) :
            [];

    this.log.debug(`${LOG_PREFIX} Existing ${attribute} values = ${values}`);

    return (control: AbstractControl): ValidationErrors | null => {

      if (values != null && values.length > 0) {

        if (control.value) {

          const s: string = control.value;

          this.log.debug(`${LOG_PREFIX} Checking whether ${s} matches any value in ${values}`);

          if (values.map(v => v.toLowerCase()).includes(s.toLowerCase())) {

            this.log.trace(`${LOG_PREFIX} Matching ${attribute} found`);

            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }



  /**
   * Validates and saves a new Reporting Table record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
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
        .createReportingTable(new ReportingTable({ reportingFrameworkId: this.targetReportingFramework?.id, name: name, number: number, description: description }))
        .subscribe(
          (response: ReportingTable) => {

            // The Reporting Table record was saved successfully
            this.log.trace(`${LOG_PREFIX} Reporting Table record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.reportingTablesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Reporting Table record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Reporting Table record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
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
