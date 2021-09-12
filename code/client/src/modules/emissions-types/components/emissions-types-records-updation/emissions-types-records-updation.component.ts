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
import { EmissionType } from '@modules/emissions-types/models/emission-type.model';
import { EmissionsTypesDataService } from '@modules/emissions-types/services/emissions-types-data.service';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Emissions Types Records Updation Component]";

@Component({
  selector: 'sb-emissions-types-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './emissions-types-records-updation.component.html',
  styleUrls: ['emissions-types-records-updation.component.scss'],
})
export class EmissionsTypesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Emission Type record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  emissionType: EmissionType | undefined = new EmissionType();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the EmissionsTypes Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  emissionsTypesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
      this.exists("name")
    ]),
    abbreviation: new FormControl('', [
      Validators.required,
      Validators.maxLength(250),
      this.exists("abbreviation")
    ]),    
    description: new FormControl('', [
      Validators.maxLength(250)
    ])    
  });

  constructor(
    private emissionsTypesDataService: EmissionsTypesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Emission Type record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Emission Type record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Emission Type record Id = ${this.id}`);

    const temp: EmissionType | undefined = (this.id == null || this.id == undefined) ? undefined : this.emissionsTypesDataService.records.find(d => d.id == this.id);
    this.emissionType = (temp == undefined) ? new EmissionType() : temp;

  }

  ngAfterContentInit() {

    // If the Emission Type record has been successfully initialize, use it to initialize the form fields
    if (this.emissionType != undefined) {

      // The Emission Type record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Emission Type record = ${JSON.stringify(this.emissionType)}`);

      // Initialize the Emission Type records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Emission Type records form fields`);
      this.emissionsTypesForm.setValue({
        name: (this.emissionType.name) ? this.emissionType.name : "",
        abbreviation: (this.emissionType.abbreviation) ? this.emissionType.abbreviation : "",
        description: (this.emissionType.description) ? this.emissionType.description : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} Emission Type record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Emission Type record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.emissionsTypesDataService.records != null && this.emissionsTypesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          let emissionType: EmissionType | undefined | null =
            attribute == "name" ? this.emissionsTypesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
              attribute == "abbreviation" ? this.emissionsTypesDataService.records.find(v => v.abbreviation?.toLowerCase() == s.toLowerCase()) : null;

          if (emissionType != undefined && emissionType.id != this.id) {
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

    if (this.emissionsTypesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.emissionsTypesForm.get('name') == null ? null : this.emissionsTypesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Emission Type Name = ${name}`);

      // Read in the provided abbreviation
      this.log.trace(`${LOG_PREFIX} Reading in the provided abbreviation`);
      const abbreviation: string | null = this.emissionsTypesForm.get('abbreviation') == null ? null : this.emissionsTypesForm.get('abbreviation')?.value;
      this.log.debug(`${LOG_PREFIX} Emission Type Abbreviation = ${abbreviation}`);      

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.emissionsTypesForm.get('description') == null ? null : this.emissionsTypesForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Emission Type Name = ${description}`);      

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Emission Type record`);
      this.emissionsTypesDataService
        .updateEmissionType(Object.assign(this.emissionType, { name: name, abbreviation: abbreviation, description: description }))
        .subscribe(
          (response: EmissionType) => {

            // The Emission Type record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Emission Type record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.emissionsTypesForm.reset();

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
      console.log(JSON.stringify(this.emissionsTypesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.emissionsTypesForm);

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
