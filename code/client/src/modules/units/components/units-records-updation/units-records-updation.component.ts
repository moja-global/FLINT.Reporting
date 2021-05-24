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
import { UnitsDataService } from '../../services/units-data.service';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { Unit } from '../../../units/models';
import { ConnectivityStatusService } from '@common/services';
import { UnitCategoriesDataService } from '@modules/unit-categories/services';
import { takeUntil, first } from 'rxjs/operators';

const LOG_PREFIX: string = "[Units Records Updation Component]";

@Component({
  selector: 'sb-units-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './units-records-updation.component.html',
  styleUrls: ['units-records-updation.component.scss'],
})
export class UnitsRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Unit record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  unit: Unit | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Units Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  unitsForm = new FormGroup({
    unitCategoryId: new FormControl('', [
      Validators.required
    ]),
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists("name")
    ]),
    plural: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists("plural")
    ]),
    symbol: new FormControl('', [
      this.exists("symbol")
    ]),
    scaleFactor: new FormControl('', [
      Validators.required,
      Validators.pattern("^[0-9]*$")
    ])
  });

  constructor(
    public unitCategoriesDataService: UnitCategoriesDataService,
    private unitsDataService: UnitsDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Unit record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Unit record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Unit record Id = ${this.id}`);
    this.unit = this.unitsDataService.records.find(d => d.id == this.id);
  }

  ngAfterContentInit() {

    // If the Unit record has been successfully initialize, use it to initialize the form fields
    if (this.unit != undefined) {

      // The Unit record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Unit record = ${JSON.stringify(this.unit)}`);

      // Initialize the Unit records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Unit records form fields`);
      this.unitsForm.setValue({
        unitCategoryId: this.unit.unitCategoryId,
        name: this.unit.name,
        plural: this.unit.plural,
        symbol: this.unit.symbol,
        scaleFactor: this.unit.scaleFactor
      });

    } else {
      this.log.error(`${LOG_PREFIX} Unit record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Unit record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.unitsDataService.records != null && this.unitsDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const unit: Unit | undefined | null =
            attribute == "name" ? this.unitsDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
              attribute == "plural" ? this.unitsDataService.records.find(v => v.plural?.toLowerCase() == s.toLowerCase()) :
                attribute == "symbol" ? this.unitsDataService.records.find(v => v.symbol?.toLowerCase() == s.toLowerCase()) :
                  null;

          if (unit != undefined && unit.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Unit record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.unitsForm.valid) {

      // Read in the provided unit category id
      this.log.trace(`${LOG_PREFIX} Reading in the provided unit category id`);
      const unitCategoryId: number | null = this.unitsForm.get('unitCategoryId') == null ? null : this.unitsForm.get('unitCategoryId')?.value;
      this.log.debug(`${LOG_PREFIX} Unit Category Id = ${unitCategoryId}`);

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.unitsForm.get('name') == null ? null : this.unitsForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Unit Name = ${name}`);

      // Read in the provided plural
      this.log.trace(`${LOG_PREFIX} Reading in the provided plural`);
      const plural: string | null = this.unitsForm.get('plural') == null ? null : this.unitsForm.get('plural')?.value;
      this.log.debug(`${LOG_PREFIX} Unit Plural = ${plural}`);

      // Read in the provided symbol
      this.log.trace(`${LOG_PREFIX} Reading in the provided symbol`);
      const symbol: string | null = this.unitsForm.get('symbol') == null ? null : this.unitsForm.get('symbol')?.value;
      this.log.debug(`${LOG_PREFIX} Unit Symbol = ${symbol}`);

      // Read in the provided scale factor
      this.log.trace(`${LOG_PREFIX} Reading in the provided scale factor`);
      const scaleFactor: number | null = this.unitsForm.get('scaleFactor') == null ? null : this.unitsForm.get('scaleFactor')?.value;
      this.log.debug(`${LOG_PREFIX} Unit Scale Factor = ${scaleFactor}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Unit record`);
      this.unitsDataService
        .updateUnit(new Unit(Object.assign(this.unit, { unitCategoryId, name, plural, symbol, scaleFactor })))
        .subscribe(
          (response: Unit) => {

            // The Unit record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Unit record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.unitsForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Unit record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Unit record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.unitsForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.unitsForm);

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
