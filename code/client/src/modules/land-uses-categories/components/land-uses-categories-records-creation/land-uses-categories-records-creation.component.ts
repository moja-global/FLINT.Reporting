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
import { CoversTypesDataService } from '@modules/covers-types/services/covers-types-data.service';
import { LandUseCategory } from '@modules/land-uses-categories/models/land-use-category.model';
import { LandUsesCategoriesDataService } from '@modules/land-uses-categories/services/land-uses-categories-data.service';
import { NGXLogger } from 'ngx-logger';
import { first } from 'rxjs/internal/operators/first';

const LOG_PREFIX: string = "[Land Uses Categories Records Creation Component]";

@Component({
  selector: 'sb-land-uses-categories-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './land-uses-categories-records-creation.component.html',
  styleUrls: ['land-uses-categories-records-creation.component.scss'],
})
export class LandUsesCategoriesRecordsCreationComponent implements OnInit, OnDestroy {

  // Instantiate an 'initialized' state notification Emitter.
  // This will allow us to notify the parent component that the component was successfully initialized
  @Output() initialized: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate and avail the target parent id variable to the parent component.
  // This will allow the parent component to inject the details of the target parent id  
  @Input() reportingFrameworkId: number | null = null;


  // Keep tabs on the Parent Land Use Category Id
  // When selected, select a corresponding Cover Type and disable the Cover Type Selection Field
  parentLandUseCategoryId: number | null = null;

  // Keep tabs on the Cover Type Id
  // When the Parent Land Use Category is selected, set it to the same Cover Type as the parent; 
  // else use the user selected value
  coverTypeId: number | null = null;

  // Instantitate a new reactive Form Group for the LandUsesCategories Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  landUsesCategoriesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
      this.exists("name")
    ]),
    parentLandUseCategoryId: new FormControl('', [
    ]),  
    coverTypeId: new FormControl('', [
      this.conditionallyRequired("coverTypeId")
    ])    
  });


  constructor(
    public landUsesCategoriesDataService: LandUsesCategoriesDataService,
    public coversTypesDataService: CoversTypesDataService,
    private log: NGXLogger) {

  }


  ngOnInit() {
    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    this.initCoverTypes(
      () => { });
  }


  initCoverTypes(callback: () => void) {

    // Check if there's a need for Cover Types initialization
    this.log.trace(`${LOG_PREFIX} Checking if there's a need for Cover Types initialization`);
    if (this.coversTypesDataService.records.length != 0) {

      // The Cover Types have already been initialized
      this.log.trace(`${LOG_PREFIX} The Cover Types have already been initialized`);

      // Transfer control to the callback() method
      callback();

    } else {

      // There's a need for Cover Types initialization
      this.log.trace(`${LOG_PREFIX} There's a need for Cover Types initialization`);

      // Initializing the Cover Types
      this.log.trace(`${LOG_PREFIX} Initializing the Cover Types`);
      this.coversTypesDataService
        .getAllCoversTypes()
        .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
        .subscribe((response => {

          // Cover Types initialization successfully completed
          this.log.trace(`${LOG_PREFIX} Cover Types initialization successfully completed`);

          // Transfer control to the callback() method
          callback();

        }));

    }

  }


  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  onSelectParentLandUseCategoryId(id: number | null) {
    this.parentLandUseCategoryId = id;
    if (this.parentLandUseCategoryId) {
      const parent: LandUseCategory | undefined = this.landUsesCategoriesDataService.records.find(l => l.id == id);
      if (parent) {

      }
    }
  }

  onSelectCoverTypeId(id: number | null) {
    this.coverTypeId = id;
  }  


  /**
   * Internal validator that checks whether a Land Use Category Record already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    this.log.trace(`${LOG_PREFIX} Checking whether ${attribute} already exists`);

    const values: string[] =
      attribute == "name" ? <Array<string>>this.landUsesCategoriesDataService.records.map(d => d.name) :
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
   * Internal validator that checks whether a Land Use Category Record Cover Type is required
   * @returns 
   */
   private conditionallyRequired(attribute: string): ValidatorFn {

    this.log.trace(`${LOG_PREFIX} Checking whether ${attribute} is conditionally required`);

    return (control: AbstractControl): ValidationErrors | null => {

      if(attribute == "coverTypeId") {
        if(this.parentLandUseCategoryId == null && this.coverTypeId == null ) {
          return { 'conditionallyRequired': true }
        }
      }

      return null;
    }
  }  



  /**
   * Validates and saves a new Land Use Category Record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
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
        .createLandUseCategory(new LandUseCategory({ 
          reportingFrameworkId: this.reportingFrameworkId, 
          parentLandUseCategoryId: this.parentLandUseCategoryId,
          coverTypeId: this.coverTypeId,
          name: name }))
        .subscribe(
          (response: LandUseCategory) => {

            // The Land Use Category Record was saved successfully
            this.log.trace(`${LOG_PREFIX} Land Use Category Record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.landUsesCategoriesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Land Use Category Record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Land Use Category Record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
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
