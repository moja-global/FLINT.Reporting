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
import { LandUseCategory } from '@modules/land-use-categories/models/land-use-category.model';
import { LandUseCategoriesDataService } from '@modules/land-use-categories/services';
import { CoversTypesDataService } from '@modules/covers-types/services/covers-types-data.service';

const LOG_PREFIX: string = "[Land Use Categories Records Updation Component]";

@Component({
  selector: 'sb-land-use-categories-records-updation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './land-use-categories-records-updation.component.html',
  styleUrls: ['land-use-categories-records-updation.component.scss'],
})
export class LandUseCategoriesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate and avail the target landUseCategory category variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // landUseCategory category  
  @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();  

  // Instantiate an object to hold the details of the Land Use Category record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  landUseCategory: LandUseCategory | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Land Use Categories Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  landUseCategoriesForm = new FormGroup({
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
    public coverTypesDataService: CoversTypesDataService,
    private landUseCategoriesDataService: LandUseCategoriesDataService,
    private log: NGXLogger) {

  }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    // Retrieve the Land Use Category record with the given id from the data store 
    this.log.trace(`${LOG_PREFIX} Retrieving the Land Use Category record with the given id from the data store`);
    this.log.debug(`${LOG_PREFIX} Land Use Category record Id = ${this.id}`);
    this.landUseCategory = this.landUseCategoriesDataService.records.find(d => d.id == this.id);
  }

  ngAfterContentInit() {

    // If the Land Use Category record has been successfully initialize, use it to initialize the form fields
    if (this.landUseCategory != undefined) {

      // The Land Use Category record was successfully initialized
      this.log.debug(`${LOG_PREFIX} Land Use Category record = ${JSON.stringify(this.landUseCategory)}`);

      // Initialize the Land Use Category records form fields
      this.log.trace(`${LOG_PREFIX} Initializing the Land Use Category records form fields`);
      this.landUseCategoriesForm.setValue({
        name: this.landUseCategory.name,
        coverTypeId: this.landUseCategory.coverTypeId
      });

    } else {
      this.log.error(`${LOG_PREFIX} Land Use Category record ${this.id} was not found in the data store`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  /**
   * Internal validator that checks whether another Land Use Category record with the same attribute already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    return (control: AbstractControl): ValidationErrors | null => {

      if (this.landUseCategoriesDataService.records != null && this.landUseCategoriesDataService.records.length > 0) {

        if (control.value) {

          const s: string = control.value;

          const landUseCategory: LandUseCategory | undefined | null =
            attribute == "name" ? this.landUseCategoriesDataService.records.find(v => v.name?.toLowerCase() == s.toLowerCase()) :
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
   * Validates and saves the updated Land Use Category record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.landUseCategoriesForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.landUseCategoriesForm.get('name') == null ? null : this.landUseCategoriesForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Land Use Category Name = ${name}`);

      // Read in the provided Cover Type Id
      this.log.trace(`${LOG_PREFIX} Reading in the provided Cover Type Id`);
      const coverTypeId: number | null = this.landUseCategoriesForm.get('coverTypeId') == null ? null : this.landUseCategoriesForm.get('coverTypeId')?.value;
      this.log.debug(`${LOG_PREFIX} Land Use Category Cover Type Id = ${coverTypeId}`);
      
      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Land Use Category record`);
      this.landUseCategoriesDataService
        .updateLandUseCategory(Object.assign(this.landUseCategory, { reportingFrameworkId: this.targetReportingFramework?.id, name: name, coverTypeId: coverTypeId}))
        .subscribe(
          (response: LandUseCategory) => {

            // The Land Use Category record was saved successfully
            this.log.trace(`${LOG_PREFIX} The Land Use Category record was successfuly updated`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.landUseCategoriesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Land Use Category record was not saved successfully
            this.log.trace(`${LOG_PREFIX} The Land Use Category record was not successfuly updated`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Form is invalid
      this.log.trace(`${LOG_PREFIX} Form is invalid`);
      console.log(JSON.stringify(this.landUseCategoriesForm.get('name')?.errors));

      // Check which fields have issues
      this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
      this.validateAllFormFields(this.landUseCategoriesForm);

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
