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
import { Pool } from '@modules/pools/models/pool.model';
import { PoolsDataService } from '@modules/pools/services/pools-data.service';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Pools Records Updation Component]";

@Component({
  selector: 'sb-pools-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './pools-records-updation.component.html',
  styleUrls: ['pools-records-updation.component.scss'],
})
export class PoolsRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Pool record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  pool: Pool | undefined = new Pool();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Pools Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  poolsForm = new FormGroup({
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
    private poolsDataService: PoolsDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Pool record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Pool record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Pool record Id = ${this.id}`);

    const temp: Pool | undefined = (this.id == null || this.id == undefined) ? undefined : this.poolsDataService.records.find(d => d.id == this.id);
    this.pool = (temp == undefined) ? new Pool() : temp;

  }

  ngAfterContentInit() {

    // If the Pool record has been successfully initialize, use it to initialize the form fields
    if (this.pool != undefined) {

      // The Pool record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Pool record = ${JSON.stringify(this.pool)}`);

      // Initialize the Pool records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Pool records form fields`);
      this.poolsForm.setValue({
        name: (this.pool.name) ? this.pool.name : "",
        description: (this.pool.description) ? this.pool.description : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} Pool record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Pool record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.poolsDataService.records != null && this.poolsDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          let pool: Pool | undefined | null =
            attribute == "name" ? this.poolsDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) : null;

          if (pool != undefined && pool.id != this.id) {
            return { 'exists': true }
          }

        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Pool record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.poolsForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.poolsForm.get('name') == null ? null : this.poolsForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Pool Name = ${name}`);   

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.poolsForm.get('description') == null ? null : this.poolsForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Pool Name = ${description}`);      

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Pool record`);
      this.poolsDataService
        .updatePool(Object.assign(this.pool, { name: name, description: description }))
        .subscribe(
          (response: Pool) => {

            // The Pool record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Pool record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.poolsForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Pool record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Pool record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.poolsForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.poolsForm);

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
