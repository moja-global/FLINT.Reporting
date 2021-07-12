import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostListener,
  OnInit,
  Output
} from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { DatabasesDataService } from '../../services/databases-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { Database } from '../../models';

const LOG_PREFIX: string = "[Databases Records Creation Component]";

@Component({
  selector: 'sb-databases-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './databases-records-creation.component.html',
  styleUrls: ['databases-records-creation.component.scss'],
})
export class DatabasesRecordsCreationComponent implements OnInit {

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Databases Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  databasesForm = new FormGroup({
    url: new FormControl('', [
      Validators.required
    ]),   
    label: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists()
    ]),
    description: new FormControl('', [
      Validators.maxLength(250)
    ]),
    startYear: new FormControl('', [
      Validators.required,
    ]), 
    endYear: new FormControl('', [
      Validators.required,
    ])            
  });

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private databasesDataService: DatabasesDataService, private log: NGXLogger) { }

  ngOnInit() {
    this.log.trace(`${LOG_PREFIX} Initializing Component`);
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  /**
   * Internal validator that checks whether a Database record already exists
   * @returns 
   */
  private exists(): ValidatorFn {

    const values: string[] = <Array<string>>this.databasesDataService.records.map(d => d.label);

    return (control: AbstractControl): ValidationErrors | null => {

      if (values != null && values.length > 0) {

        if (control.value) {

          const s: string = control.value;

          if (values.map(v => v.toLowerCase()).includes(s.toLowerCase())) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves a new Database record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.databasesForm.valid) {

      // Read in the provided url
      this.log.trace(`${LOG_PREFIX} Reading in the provided url`);
      const url: string = this.databasesForm.get('url')?.value;
      this.log.debug(`${LOG_PREFIX} Database URL = ${url}`);      

      // Read in the provided label
      this.log.trace(`${LOG_PREFIX} Reading in the provided label`);
      const label: string = this.databasesForm.get('label')?.value;
      this.log.debug(`${LOG_PREFIX} Database Label = ${label}`);

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string = this.databasesForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Database Description = ${description}`);

      // Read in the provided start year
      this.log.trace(`${LOG_PREFIX} Reading in the provided start year`);
      const startYear: number = this.databasesForm.get('startYear')?.value;
      this.log.debug(`${LOG_PREFIX} Database Start Year = ${description}`);    
      
      // Read in the provided end year
      this.log.trace(`${LOG_PREFIX} Reading in the provided end year`);
      const endYear: number = this.databasesForm.get('endYear')?.value;
      this.log.debug(`${LOG_PREFIX} Database End Year = ${description}`);        
      
      
      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Database record`);
      this.databasesDataService
        .createDatabase(new Database({ url,label,description,startYear,endYear }))
        .subscribe(
          (response: Database) => {

            // The Database record was saved successfully
            this.log.trace(`${LOG_PREFIX} Database record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.databasesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Database record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Database record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
      this.validateAllFormFields(this.databasesForm);

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
