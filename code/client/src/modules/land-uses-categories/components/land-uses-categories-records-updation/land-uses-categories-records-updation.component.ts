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
import { LandUseCategory } from '@modules/land-uses-categories/models/land-use-category.model';
import { LandUsesCategoriesDataService } from '@modules/land-uses-categories/services/land-uses-categories-data.service';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Land Uses Categories Records Updation Component]";

@Component({
  selector: 'sb-land-uses-categories-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './land-uses-categories-records-updation.component.html',
  styleUrls: ['land-uses-categories-records-updation.component.scss'],
})
export class LandUsesCategoriesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Land Use Category Record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  landUseCategory: LandUseCategory | undefined = new LandUseCategory();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the LandUsesCategories Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  landUsesCategoriesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
      this.exists("name")
    ])
  });

  constructor(
    private landUsesCategoriesDataService: LandUsesCategoriesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Land Use Category Record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Land Use Category Record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Land Use Category Record Id = ${this.id}`);

    const temp: LandUseCategory | undefined = (this.id == null || this.id == undefined)? undefined : this.landUsesCategoriesDataService.records.find(d => d.id == this.id);
    this.landUseCategory = (temp == undefined)? new LandUseCategory() : temp;

  }

  ngAfterContentInit() {

    // If the Land Use Category Record has been successfully initialize, use it to initialize the form fields
    if (this.landUseCategory != undefined) {

      // The Land Use Category Record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Land Use Category Record = ${JSON.stringify(this.landUseCategory)}`);

      // Initialize the Land Use Category Records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Land Use Category Records form fields`);
      this.landUsesCategoriesForm.setValue({
        name: (this.landUseCategory.name)? this.landUseCategory.name : ""
      });

    } else {
      this.log.error(`${LOG_PREFIX} Land Use Category Record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Land Use Category Record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.landUsesCategoriesDataService.records != null && this.landUsesCategoriesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const landUseCategory: LandUseCategory | undefined | null =
            attribute == "name" ? this.landUsesCategoriesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
              null;

          if (landUseCategory != undefined && landUseCategory.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Land Use Category Record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.landUsesCategoriesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.landUsesCategoriesForm.get('name') == null ? null : this.landUsesCategoriesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Land Use Category Name = ${name}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Land Use Category Record`);
      this.landUsesCategoriesDataService
        .updateLandUseCategory(Object.assign(this.landUseCategory, { name: name}))
        .subscribe(
          (response: LandUseCategory) => {

            // The Land Use Category Record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Land Use Category Record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.landUsesCategoriesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Land Use Category Record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Land Use Category Record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.landUsesCategoriesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.landUsesCategoriesForm);

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
