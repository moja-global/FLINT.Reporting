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
import { FluxType } from '@modules/fluxes-types/models/flux-type.model';
import { FluxesTypesDataService } from '@modules/fluxes-types/services/fluxes-types-data.service';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Fluxes Types Records Updation Component]";

@Component({
  selector: 'sb-fluxes-types-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './fluxes-types-records-updation.component.html',
  styleUrls: ['fluxes-types-records-updation.component.scss'],
})
export class FluxesTypesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Flux Type record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  fluxType: FluxType | undefined = new FluxType();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the FluxesTypes Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  fluxesTypesForm = new FormGroup({
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
    private fluxesTypesDataService: FluxesTypesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Flux Type record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Flux Type record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Flux Type record Id = ${this.id}`);

    const temp: FluxType | undefined = (this.id == null || this.id == undefined) ? undefined : this.fluxesTypesDataService.records.find(d => d.id == this.id);
    this.fluxType = (temp == undefined) ? new FluxType() : temp;

  }

  ngAfterContentInit() {

    // If the Flux Type record has been successfully initialize, use it to initialize the form fields
    if (this.fluxType != undefined) {

      // The Flux Type record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Flux Type record = ${JSON.stringify(this.fluxType)}`);

      // Initialize the Flux Type records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Flux Type records form fields`);
      this.fluxesTypesForm.setValue({
        name: (this.fluxType.name) ? this.fluxType.name : "",
        description: (this.fluxType.description) ? this.fluxType.description : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} Flux Type record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Flux Type record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.fluxesTypesDataService.records != null && this.fluxesTypesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          let fluxType: FluxType | undefined | null =
            attribute == "name" ? this.fluxesTypesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) : null;

          if (fluxType != undefined && fluxType.id != this.id) {
            return { 'exists': true }
          }

        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Flux Type record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.fluxesTypesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.fluxesTypesForm.get('name') == null ? null : this.fluxesTypesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Flux Type Name = ${name}`);   

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.fluxesTypesForm.get('description') == null ? null : this.fluxesTypesForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Flux Type Name = ${description}`);      

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Flux Type record`);
      this.fluxesTypesDataService
        .updateFluxType(Object.assign(this.fluxType, { name: name, description: description }))
        .subscribe(
          (response: FluxType) => {

            // The Flux Type record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Flux Type record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.fluxesTypesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Flux Type record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Flux Type record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.fluxesTypesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.fluxesTypesForm);

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
