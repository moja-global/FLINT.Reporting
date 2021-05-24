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
import { EmissionTypesDataService } from '../../services/emission-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { EmissionType } from '../../../emission-types/models';

const LOG_PREFIX: string = "[Emission Types Records Updation Component]";

@Component({
  selector: 'sb-emission-types-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './emission-types-records-updation.component.html',
  styleUrls: ['emission-types-records-updation.component.scss'],
})
export class EmissionTypesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Emission Type record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  emissionType: EmissionType | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Emission Types Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  emissionTypesForm: FormGroup = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists("name")
    ]),
    abbreviation: new FormControl('', [
      Validators.required,
      Validators.maxLength(50),
      this.exists("abbreviation")
    ]),
    description: new FormControl('', [
      Validators.maxLength(250)
    ])
  });

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private emissionTypesDataService: EmissionTypesDataService, private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Emission Type record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Emission Type record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Emission Type record Id = ${this.id}`);
    this.emissionType = this.emissionTypesDataService.records.find(d => d.id == this.id);
  }

  ngAfterContentInit() {

    // If the Emission Type record has been successfully initialize, use it to initialize the form fields
    if (this.emissionType != undefined) {

      // The Emission Type record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Emission Type record = ${JSON.stringify(this.emissionType)}`);

      // Initialize the Emission Type records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Emission Type records form fields`);
      this.emissionTypesForm.setValue({
        name: this.emissionType.name,
        abbreviation: this.emissionType.abbreviation,
        description: this.emissionType.description
      });

    } else {
      this.log.error(`${LOG_PREFIX} Emission Type record ${this.id} was not found in the data store`);
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
   * Internal validator that checks whether another Emission Type record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.emissionTypesDataService.records != null && this.emissionTypesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const emissionType: EmissionType | undefined | null =
            attribute == "name" ? this.emissionTypesDataService.records.find(d => d.name?.toLowerCase() == s.toLowerCase()) :
              attribute == "abbreviation" ? this.emissionTypesDataService.records.find(d => d.abbreviation?.toLowerCase() == s.toLowerCase()) :
                null;

          if (emissionType != null && emissionType != undefined && emissionType.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Emission Type record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.emissionTypesForm.valid) {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);

      // Read in the provided name 
      this.log.trace(`${LOG_PREFIX} Reading in the provided name `);
      const name: string | null = this.emissionTypesForm.get('name') == null ? null : this.emissionTypesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Emission Type Name  = ${name}`);

      // Read in the provided abbreviation
      this.log.trace(`${LOG_PREFIX} Reading in the provided abbreviation`);
      const abbreviation: string | null = this.emissionTypesForm.get('abbreviation') == null ? null : this.emissionTypesForm.get('abbreviation')?.value;
      this.log.debug(`${LOG_PREFIX} Emission Type Abbreviation = ${abbreviation}`);

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.emissionTypesForm.get('description') == null ? null : this.emissionTypesForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Emission Type Description = ${description}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Emission Type record`);
      this.emissionTypesDataService
        .updateEmissionType(new EmissionType(Object.assign(this.emissionType, { name, abbreviation, description })))
        .subscribe(
          (response: EmissionType) => {

            // The Emission Type record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Emission Type record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.emissionTypesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Emission Type record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Emission Type record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.emissionTypesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.emissionTypesForm);

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
