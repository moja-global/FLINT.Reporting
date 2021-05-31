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
import { LandUseCategoriesDataService } from '../../services/land-use-categories-data.service';
import { NGXLogger } from 'ngx-logger';
import { LandUseCategory } from '@modules/land-use-categories/models/land-use-category.model';
import { Subject } from 'rxjs';
import { ReportingFramework } from '@modules/reporting-frameworks/models';
import { CoverTypesDataService } from '@modules/cover-types/services/cover-types-data.service';

const LOG_PREFIX: string = "[Land Use Categories Records Creation Component]";

@Component({
  selector: 'sb-land-use-categories-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './land-use-categories-records-creation.component.html',
  styleUrls: ['land-use-categories-records-creation.component.scss'],
})
export class LandUseCategoriesRecordsCreationComponent implements OnInit, OnDestroy {

  // Instantiate an 'initialized' state notification Emitter.
  // This will allow us to notify the parent component that the component was successfully initialized
  @Output() initialized: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast notifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantiate and avail the target landUseCategory category variable to the parent component.
  // This will allow the parent component to inject the details of the current target  
  // landUseCategory category  
  @Input() targetReportingFramework: ReportingFramework = new ReportingFramework();  

  // Instantiate a notifier.
  // This is simply an observable that we will use to opt 
  // out of initialization subscriptions once we are done with them
  private _done$ = new Subject<boolean>();

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
      Validators.required,
    ]),    
  });


  constructor(
    public coverTypesDataService: CoverTypesDataService,
    private landUseCategoriesDataService: LandUseCategoriesDataService,
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
   * Internal validator that checks whether a Land Use Category record already exists
   * @returns 
   */
  private exists(attribute: string): ValidatorFn {

    this.log.trace(`${LOG_PREFIX} Checking whether ${attribute} already exists`);

    const values: string[] =
      attribute == "name" ? <Array<string>>this.landUseCategoriesDataService.records.map(d => d.name) :
        attribute == "number" ? <Array<string>>this.landUseCategoriesDataService.records.map(d => d.number) :
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
   * Validates and saves a new Land Use Category record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
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
        .createLandUseCategory(new LandUseCategory({ reportingFrameworkId: this.targetReportingFramework?.id, name: name, coverTypeId: coverTypeId }))
        .subscribe(
          (response: LandUseCategory) => {

            // The Land Use Category record was saved successfully
            this.log.trace(`${LOG_PREFIX} Land Use Category record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.landUseCategoriesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Land Use Category record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Land Use Category record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
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
