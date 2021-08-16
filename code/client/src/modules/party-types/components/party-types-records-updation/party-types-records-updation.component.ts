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
import { PartyTypesDataService } from '../../services/party-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { PartyType } from '../../../party-types/models';

const LOG_PREFIX: string = "[Party Types Records Updation Component]";

@Component({
  selector: 'sb-party-types-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './party-types-records-updation.component.html',
  styleUrls: ['party-types-records-updation.component.scss'],
})
export class PartyTypesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Party Type record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  partyType: PartyType | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Party Types Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  partyTypesForm: FormGroup = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists()
    ])
  });

    // Keep tabs on the target part type
    @Input() targetPartyType!: PartyType | undefined;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

  constructor(
    private partyTypesDataService: PartyTypesDataService, 
    private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`); 

    // Retrieve the Party Type record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Party Type record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Party Type record Id = ${this.id}`);
    this.partyType = this.partyTypesDataService.records.find(d => d.id == this.id);
  }

  ngAfterContentInit() {

    // If the Party Type record has been successfully initialize, use it to initialize the form fields
    if (this.partyType != undefined) {

      // The Party Type record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Party Type record = ${JSON.stringify(this.partyType)}`);

      // Initialize the Party Type records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Party Type records form fields`);
      this.partyTypesForm.setValue({
        name: this.partyType.name
      });

    } else {
      this.log.error(`${LOG_PREFIX} Party Type record ${this.id} was not found in the data store`);
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
   * Internal validator that checks whether another Party Type record with the same attribute already exists
   * @returns 
   */
  private exists(): ValidatorFn {

    const values: string[] = <Array<string>>this.partyTypesDataService.records.map(d => d.name);

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.partyTypesDataService.records != null && this.partyTypesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const partyType: PartyType | undefined = this.partyTypesDataService.records.find(d => d.name?.toLowerCase() == s.toLowerCase());

          if (partyType != undefined && partyType.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Party Type record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.partyTypesForm.valid) {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);

      // Read in the provided name 
      this.log.trace(`${LOG_PREFIX} Reading in the provided name `);
      const name: string | null = this.partyTypesForm.get('name') == null ? null : this.partyTypesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Party Type Name  = ${name}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Party Type record`);
      this.partyTypesDataService
        .updatePartyType(Object.assign(this.partyType, {  name: name, parentAdministrativeLevelId: this.targetPartyType?.id}))
        .subscribe(
          (response: PartyType) => {

            // The Party Type record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Party Type record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.partyTypesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Party Type record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Party Type record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.partyTypesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.partyTypesForm);

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
