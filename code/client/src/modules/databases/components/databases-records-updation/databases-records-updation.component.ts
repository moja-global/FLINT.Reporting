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
import { DatabasesDataService } from '../../services/databases-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { Database } from '../../../databases/models';

const LOG_PREFIX: string = "[Databases Records Updation Component]";

@Component({
  selector: 'sb-databases-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './databases-records-updation.component.html',
  styleUrls: ['databases-records-updation.component.scss'],
})
export class DatabasesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Database record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  database: Database | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Databases Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  databasesForm: FormGroup = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists()
    ])
  });

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private databasesDataService: DatabasesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Database record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Database record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Database record Id = ${this.id}`);
    this.database = this.databasesDataService.records.find(d => d.id == this.id);

    const temp: Database | undefined = (this.id == null || this.id == undefined)? undefined : this.databasesDataService.records.find(d => d.id == this.id);
    this.database = (temp == undefined)? new Database() : temp;    
  }

  ngAfterContentInit() {

    // If the Database record has been successfully initialize, use it to initialize the form fields
    if (this.database != undefined) {

      // The Database record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Database record = ${JSON.stringify(this.database)}`);

      // Initialize the Database records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Database records form fields`);
      this.databasesForm.setValue({
        name: (this.database.label)? this.database.label : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} Database record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  /**
   * Internal validator that checks whether another Database record with the same attribute already exists
   * @returns 
   */
  private exists(): ValidatorFn {

    const values: string[] = <Array<string>>this.databasesDataService.records.map(d => d.label);

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.databasesDataService.records != null && this.databasesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const database: Database | undefined = this.databasesDataService.records.find(d => d.label?.toLowerCase() == s.toLowerCase());

          if (database != undefined && database.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Database record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.databasesForm.valid) {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);

      // Read in the provided name 
      this.log.trace(`${LOG_PREFIX} Reading in the provided name `);
      const name: string | null = this.databasesForm.get('name') == null ? null : this.databasesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Database Name  = ${name}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Database record`);
      this.databasesDataService
        .updateDatabase(Object.assign(this.database, { name }))
        .subscribe(
          (response: Database) => {

            // The Database record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Database record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.databasesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Database record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Database record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.databasesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
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
