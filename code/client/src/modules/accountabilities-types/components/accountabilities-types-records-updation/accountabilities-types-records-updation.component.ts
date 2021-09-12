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
import { AccountabilityType } from '@modules/accountabilities-types/models/accountability-type.model';
import { AccountabilitiesTypesDataService } from '@modules/accountabilities-types/services/accountabilities-types-data.service';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Accountabilities Types Records Updation Component]";

@Component({
  selector: 'sb-accountabilities-types-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './accountabilities-types-records-updation.component.html',
  styleUrls: ['accountabilities-types-records-updation.component.scss'],
})
export class AccountabilitiesTypesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the AccountabilityType record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  accountabilityType: AccountabilityType | undefined = new AccountabilityType();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the AccountabilitiesTypes Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  accountabilitiesTypesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
      this.exists("name")
    ])
  });

  constructor(
    private accountabilitiesTypesDataService: AccountabilitiesTypesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the AccountabilityType record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the AccountabilityType record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} AccountabilityType record Id = ${this.id}`);

    const temp: AccountabilityType | undefined = (this.id == null || this.id == undefined)? undefined : this.accountabilitiesTypesDataService.records.find(d => d.id == this.id);
    this.accountabilityType = (temp == undefined)? new AccountabilityType() : temp;

  }

  ngAfterContentInit() {

    // If the AccountabilityType record has been successfully initialize, use it to initialize the form fields
    if (this.accountabilityType != undefined) {

      // The AccountabilityType record was successfully initialized
      this.log.debug(`${LOG_PREFIX} AccountabilityType record = ${JSON.stringify(this.accountabilityType)}`);

      // Initialize the AccountabilityType records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the AccountabilityType records form fields`);
      this.accountabilitiesTypesForm.setValue({
        name: (this.accountabilityType.name)? this.accountabilityType.name : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} AccountabilityType record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another AccountabilityType record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.accountabilitiesTypesDataService.records != null && this.accountabilitiesTypesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const accountabilityType: AccountabilityType | undefined | null =
            attribute == "name" ? this.accountabilitiesTypesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
              null;

          if (accountabilityType != undefined && accountabilityType.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated AccountabilityType record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.accountabilitiesTypesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.accountabilitiesTypesForm.get('name') == null ? null : this.accountabilitiesTypesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} AccountabilityType Name = ${name}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the AccountabilityType record`);
      this.accountabilitiesTypesDataService
        .updateAccountabilityType(Object.assign(this.accountabilityType, { name: name}))
        .subscribe(
          (response: AccountabilityType) => {

            // The AccountabilityType record was saved successfully
            this.log.trace(`${LOG_PREFIX} The AccountabilityType record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.accountabilitiesTypesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The AccountabilityType record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The AccountabilityType record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.accountabilitiesTypesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.accountabilitiesTypesForm);

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
