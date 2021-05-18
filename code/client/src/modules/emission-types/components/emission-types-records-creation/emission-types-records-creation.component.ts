import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  OnInit,
  Output
} from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { EmissionTypesDataService } from '../../services/emission-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { EmissionType } from '../../models';

const LOG_PREFIX: string = "[Emission Types Records Creation Component]";

@Component({
  selector: 'sb-emission-types-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './emission-types-records-creation.component.html',
  styleUrls: ['emission-types-records-creation.component.scss'],
})
export class EmissionTypesRecordsCreationComponent implements OnInit {

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Emission Types Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  emissionTypesForm = new FormGroup({
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

  ngOnInit() { }

  ngOnDestroy() {

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  /**
   * Internal validator that checks whether a Emission Type record already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    const values: string[] =
      attribute == "name" ? <Array<string>>this.emissionTypesDataService.records.map(d => d.name) :
        attribute == "abbreviation" ? <Array<string>>this.emissionTypesDataService.records.map(d => d.abbreviation) :
          [];

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
   * Validates and saves a new Emission Type record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.emissionTypesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.emissionTypesForm.get('name') == null ? null : this.emissionTypesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Emission Type Name = ${name}`);

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
        .createEmissionType(new EmissionType({ name, abbreviation, description }))
        .subscribe(
          (response: EmissionType) => {

            // The Emission Type record was saved successfully
            this.log.trace(`${LOG_PREFIX} Emission Type record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.emissionTypesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Emission Type record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Emission Type record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
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
