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
import { PartiesDataService } from '../../services/parties-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { Party } from '../../../parties/models';

const LOG_PREFIX: string = "[Parties Records Updation Component]";

@Component({
  selector: 'sb-parties-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './parties-records-updation.component.html',
  styleUrls: ['parties-records-updation.component.scss'],
})
export class PartiesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Party record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  party: Party | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Parties Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  partiesForm: FormGroup = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists()
    ])
  });

    // Keep tabs on the target part type
    @Input() targetPartyType!: Party | undefined;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

  constructor(
    private partiesDataService: PartiesDataService, 
    private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`); 

    // Retrieve the Party record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Party record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Party record Id = ${this.id}`);
    this.party = this.partiesDataService.records.find(d => d.id == this.id);
  }

  ngAfterContentInit() {

    // If the Party record has been successfully initialize, use it to initialize the form fields
    if (this.party != undefined) {

      // The Party record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Party record = ${JSON.stringify(this.party)}`);

      // Initialize the Party records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Party records form fields`);
      this.partiesForm.setValue({
        name: this.party.name
      });

    } else {
      this.log.error(`${LOG_PREFIX} Party record ${this.id} was not found in the data store`);
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
   * Internal validator that checks whether another Party record with the same attribute already exists
   * @returns 
   */
  private exists(): ValidatorFn {

    const values: string[] = <Array<string>>this.partiesDataService.records.map(d => d.name);

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.partiesDataService.records != null && this.partiesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const party: Party | undefined = this.partiesDataService.records.find(d => d.name?.toLowerCase() == s.toLowerCase());

          if (party != undefined && party.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Party record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.partiesForm.valid) {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);

      // Read in the provided name 
      this.log.trace(`${LOG_PREFIX} Reading in the provided name `);
      const name: string | null = this.partiesForm.get('name') == null ? null : this.partiesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Party Name  = ${name}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Party record`);
      this.partiesDataService
        .updateParty(Object.assign(this.party, {  name: name, partyTypeId: this.targetPartyType?.id}))
        .subscribe(
          (response: Party) => {

            // The Party record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Party record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.partiesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Party record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Party record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.partiesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.partiesForm);

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
