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
import { NGXLogger } from 'ngx-logger';
import { ReportingFramework } from '@modules/reporting-frameworks/models';
import { LandUseSubcategory } from '@modules/land-use-subcategories/models/land-use-subcategory.model';
import { LandUseSubcategoriesDataService } from '@modules/land-use-subcategories/services';
import { CoverTypesDataService } from '@modules/cover-types/services/cover-types-data.service';

const LOG_PREFIX: string = "[Land Use Subcategories Records Updation Component]";

@Component({
  selector: 'sb-land-use-subcategories-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './land-use-subcategories-records-updation.component.html',
  styleUrls: ['land-use-subcategories-records-updation.component.scss'],
})
export class LandUseSubcategoriesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate and avail the target Reporting Framework variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // Reporting Framework  
  @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();  

  // Instantiate an object to hold the details of the Land Use Subcategory record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  landUseSubcategory: LandUseSubcategory | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Land Use Subcategories Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  landUseSubcategoriesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists("name")
    ]),
    coverTypeId: new FormControl('', [
      Validators.required
    ]),    
  });

  constructor(
    public coverTypesDataService: CoverTypesDataService,
    private landUseSubcategoriesDataService: LandUseSubcategoriesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Land Use Subcategory record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Land Use Subcategory record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Land Use Subcategory record Id = ${this.id}`);
    this.landUseSubcategory = this.landUseSubcategoriesDataService.records.find(d => d.id == this.id);
  }

  ngAfterContentInit() {

    // If the Land Use Subcategory record has been successfully initialize, use it to initialize the form fields
    if (this.landUseSubcategory != undefined) {

      // The Land Use Subcategory record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Land Use Subcategory record = ${JSON.stringify(this.landUseSubcategory)}`);

      // Initialize the Land Use Subcategory records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Land Use Subcategory records form fields`);
      this.landUseSubcategoriesForm.setValue({
        name: this.landUseSubcategory.name,
        coverTypeId: this.landUseSubcategory.coverTypeId
      });

    } else {
      this.log.error(`${LOG_PREFIX} Land Use Subcategory record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Land Use Subcategory record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.landUseSubcategoriesDataService.records != null && this.landUseSubcategoriesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const landUseSubcategory: LandUseSubcategory | undefined | null =
            attribute == "name" ? this.landUseSubcategoriesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
                  null;

          if (landUseSubcategory != undefined && landUseSubcategory.id != this.id) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves the updated Land Use Subcategory record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.landUseSubcategoriesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.landUseSubcategoriesForm.get('name') == null ? null : this.landUseSubcategoriesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Land Use Subcategory Name = ${name}`);

      // Read in the provided Cover Type Id
      this.log.trace(`${LOG_PREFIX} Reading in the provided Cover Type Id`);
      const coverTypeId: number | null = this.landUseSubcategoriesForm.get('coverTypeId') == null ? null : this.landUseSubcategoriesForm.get('coverTypeId')?.value;
      this.log.debug(`${LOG_PREFIX} Land Use Subcategory Cover Type Id = ${coverTypeId}`);
      
      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Land Use Subcategory record`);
      this.landUseSubcategoriesDataService
        .updateLandUseSubcategory(Object.assign(this.landUseSubcategory, { reportingFrameworkId: this.targetReportingFramework?.id, name: name, coverTypeId: coverTypeId}))
        .subscribe(
          (response: LandUseSubcategory) => {

            // The Land Use Subcategory record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Land Use Subcategory record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.landUseSubcategoriesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Land Use Subcategory record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Land Use Subcategory record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.landUseSubcategoriesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.landUseSubcategoriesForm);

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
