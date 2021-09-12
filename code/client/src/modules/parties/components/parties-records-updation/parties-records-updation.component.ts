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
import { Party } from '@modules/parties/models/party.model';
import { PartiesDataService } from '@modules/parties/services/parties-data.service';
import { NGXLogger } from 'ngx-logger';

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

  // Instantiate an object to hold the details of the Party Record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  party: Party | undefined = new Party();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Parties Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  partiesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
      this.exists("name")
    ])
  });

  constructor(
    private partiesDataService: PartiesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Party Record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Party Record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Party Record Id = ${this.id}`);

    const temp: Party | undefined = (this.id == null || this.id == undefined)? undefined : this.partiesDataService.records.find(d => d.id == this.id);
    this.party = (temp == undefined)? new Party() : temp;

  }

  ngAfterContentInit() {

    // If the Party Record has been successfully initialize, use it to initialize the form fields
    if (this.party != undefined) {

      // The Party Record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Party Record = ${JSON.stringify(this.party)}`);

      // Initialize the Party Records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Party Records form fields`);
      this.partiesForm.setValue({
        name: (this.party.name)? this.party.name : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} Party Record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Party Record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.partiesDataService.records != null && this.partiesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const party: Party | undefined | null =
            attribute == "name" ? this.partiesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
              null;

          if (party != undefined && party.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Party Record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.partiesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.partiesForm.get('name') == null ? null : this.partiesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Party Name = ${name}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Party Record`);
      this.partiesDataService
        .updateParty(Object.assign(this.party, { name: name}))
        .subscribe(
          (response: Party) => {

            // The Party Record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Party Record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.partiesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Party Record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Party Record was not successfuly updated`);

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
