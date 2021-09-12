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
import { PartyType } from '@modules/parties-types/models/party-type.model';
import { PartiesTypesDataService } from '@modules/parties-types/services/parties-types-data.service';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Parties Types Records Updation Component]";

@Component({
  selector: 'sb-parties-types-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './parties-types-records-updation.component.html',
  styleUrls: ['parties-types-records-updation.component.scss'],
})
export class PartiesTypesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the PartyType record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  partyType: PartyType | undefined = new PartyType();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the PartiesTypes Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  partiesTypesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
      this.exists("name")
    ])
  });

  constructor(
    private partiesTypesDataService: PartiesTypesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the PartyType record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the PartyType record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} PartyType record Id = ${this.id}`);

    const temp: PartyType | undefined = (this.id == null || this.id == undefined)? undefined : this.partiesTypesDataService.records.find(d => d.id == this.id);
    this.partyType = (temp == undefined)? new PartyType() : temp;

  }

  ngAfterContentInit() {

    // If the PartyType record has been successfully initialize, use it to initialize the form fields
    if (this.partyType != undefined) {

      // The PartyType record was successfully initialized
      this.log.debug(`${LOG_PREFIX} PartyType record = ${JSON.stringify(this.partyType)}`);

      // Initialize the PartyType records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the PartyType records form fields`);
      this.partiesTypesForm.setValue({
        name: (this.partyType.name)? this.partyType.name : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} PartyType record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another PartyType record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.partiesTypesDataService.records != null && this.partiesTypesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const partyType: PartyType | undefined | null =
            attribute == "name" ? this.partiesTypesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
              null;

          if (partyType != undefined && partyType.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated PartyType record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.partiesTypesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.partiesTypesForm.get('name') == null ? null : this.partiesTypesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} PartyType Name = ${name}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the PartyType record`);
      this.partiesTypesDataService
        .updatePartyType(Object.assign(this.partyType, { name: name}))
        .subscribe(
          (response: PartyType) => {

            // The PartyType record was saved successfully
            this.log.trace(`${LOG_PREFIX} The PartyType record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.partiesTypesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The PartyType record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The PartyType record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.partiesTypesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.partiesTypesForm);

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
