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
import { EmissionType } from '@modules/emissions-types/models/emission-type.model';
import { EmissionsTypesDataService } from '@modules/emissions-types/services/emissions-types-data.service';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Emissions Types Records Creation Component]";

@Component({
  selector: 'sb-emissions-types-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './emissions-types-records-creation.component.html',
  styleUrls: ['emissions-types-records-creation.component.scss'],
})
export class EmissionsTypesRecordsCreationComponent implements OnInit, OnDestroy {

  // Instantiate an 'initialized' state notification Emitter.
  // This will allow us to notify the parent component that the component was successfully initialized
  @Output() initialized: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed creation events
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
  }


  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }


  /**
   * Internal validator that checks whether a Emission Type Record already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    this.log.trace(`${LOG_PREFIX} Checking whether ${attribute} already exists`);

    const values: string[] =
      attribute == "name" ? <Array<string>>this.emissionsTypesDataService.records.map(d => d.name) :
        attribute == "abbreviation" ? <Array<string>>this.emissionsTypesDataService.records.map(d => d.abbreviation) :
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
   * Validates and saves a new Emission Type Record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
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
      this.log.debug(`${LOG_PREFIX} Emission Type Description = ${description}`);      

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Emission Type Record`);
      this.emissionsTypesDataService
        .createEmissionType(new EmissionType({ name: name, abbreviation: abbreviation, description: description }))
        .subscribe(
          (response: EmissionType) => {

            // The Emission Type Record was saved successfully
            this.log.trace(`${LOG_PREFIX} Emission Type Record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.emissionsTypesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Emission Type Record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Emission Type Record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
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
