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
import { UnitsDataService } from '../../services/units-data.service';
import { NGXLogger } from 'ngx-logger';
import { Unit } from '../../models';
import { UnitCategoriesDataService } from '@modules/unit-categories/services';
import { Subject } from 'rxjs';
import { UnitCategory } from '@modules/unit-categories/models';

const LOG_PREFIX: string = "[Units Records Creation Component]";

@Component({
  selector: 'sb-units-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './units-records-creation.component.html',
  styleUrls: ['units-records-creation.component.scss'],
})
export class UnitsRecordsCreationComponent implements OnInit, OnDestroy {

  // Instantiate an 'initialized' state notification Emitter.
  // This will allow us to notify the parent component that the component was successfully initialized
  @Output() initialized: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate and avail the target unit category variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // unit category  
  @Input() targetUnitCategory: UnitCategory = new UnitCategory(); 

  // Instantiate a notifier.
  // This is simply an observable that we will use to opt 
  // out of initialization subscriptions once we are done with them
  private _done$ = new Subject<boolean>();

  // Instantitate a new reactive Form Group for the Units Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  unitsForm = new FormGroup({
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
  }


  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }


  /**
   * Internal validator that checks whether a Unit record already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    this.log.trace(`${LOG_PREFIX} Checking whether ${attribute} already exists`);

    const values: string[] =
      attribute == "name" ? <Array<string>>this.unitsDataService.records.map(d => d.name) :
        attribute == "plural" ? <Array<string>>this.unitsDataService.records.map(d => d.plural) :
          attribute == "symbol" ? <Array<string>>this.unitsDataService.records.map(d => d.symbol) :
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
   * Validates and saves a new Unit record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.unitsForm.valid) {

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
        .createUnit(new Unit({ unitCategoryId: this.targetUnitCategory?.id, name: name, plural: plural, symbol: symbol, scaleFactor: scaleFactor }))
        .subscribe(
          (response: Unit) => {

            // The Unit record was saved successfully
            this.log.trace(`${LOG_PREFIX} Unit record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.unitsForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Unit record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Unit record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
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
