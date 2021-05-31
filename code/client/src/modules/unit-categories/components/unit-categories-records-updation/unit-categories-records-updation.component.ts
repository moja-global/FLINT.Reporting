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
import { UnitCategoriesDataService } from '../../services/unit-categories-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { UnitCategory } from '../../../unit-categories/models';

const LOG_PREFIX: string = "[Unit Categories Records Updation Component]";

@Component({
  selector: 'sb-unit-categories-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './unit-categories-records-updation.component.html',
  styleUrls: ['unit-categories-records-updation.component.scss'],
})
export class UnitCategoriesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Unit Category record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  unitCategory: UnitCategory | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Unit Categories Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  unitCategoriesForm: FormGroup = new FormGroup({
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

  constructor(private unitCategoriesDataService: UnitCategoriesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Unit Category record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Unit Category record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Unit Category record Id = ${this.id}`);
    this.unitCategory = this.unitCategoriesDataService.records.find(d => d.id == this.id);

    const temp: UnitCategory | undefined = (this.id == null || this.id == undefined)? undefined : this.unitCategoriesDataService.records.find(d => d.id == this.id);
    this.unitCategory = (temp == undefined)? new UnitCategory() : temp;    
  }

  ngAfterContentInit() {

    // If the Unit Category record has been successfully initialize, use it to initialize the form fields
    if (this.unitCategory != undefined) {

      // The Unit Category record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Unit Category record = ${JSON.stringify(this.unitCategory)}`);

      // Initialize the Unit Category records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Unit Category records form fields`);
      this.unitCategoriesForm.setValue({
        name: (this.unitCategory.name)? this.unitCategory.name : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} Unit Category record ${this.id} was not found in the data store`);
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
   * Internal validator that checks whether another Unit Category record with the same attribute already exists
   * @returns 
   */
  private exists(): ValidatorFn {

    const values: string[] = <Array<string>>this.unitCategoriesDataService.records.map(d => d.name);

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.unitCategoriesDataService.records != null && this.unitCategoriesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const unitCategory: UnitCategory | undefined = this.unitCategoriesDataService.records.find(d => d.name?.toLowerCase() == s.toLowerCase());

          if (unitCategory != undefined && unitCategory.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Unit Category record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.unitCategoriesForm.valid) {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);

      // Read in the provided name 
      this.log.trace(`${LOG_PREFIX} Reading in the provided name `);
      const name: string | null = this.unitCategoriesForm.get('name') == null ? null : this.unitCategoriesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Unit Category Name  = ${name}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Unit Category record`);
      this.unitCategoriesDataService
        .updateUnitCategory(Object.assign(this.unitCategory, { name }))
        .subscribe(
          (response: UnitCategory) => {

            // The Unit Category record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Unit Category record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.unitCategoriesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Unit Category record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Unit Category record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.unitCategoriesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.unitCategoriesForm);

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
