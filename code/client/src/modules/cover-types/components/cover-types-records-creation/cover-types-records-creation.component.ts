import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  OnInit,
  Output
} from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { CoverTypesDataService } from '../../services/cover-types-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { CoverType } from '../../models';

const LOG_PREFIX: string = "[Cover Types Records Creation Component]";

@Component({
  selector: 'sb-cover-types-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './cover-types-records-creation.component.html',
  styleUrls: ['cover-types-records-creation.component.scss'],
})
export class CoverTypesRecordsCreationComponent implements OnInit {

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful creation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed creation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group for the Cover Types Form.
  // This will allow to define and enforce the validation rules for all the form controls.
  coverTypesForm = new FormGroup({
    code: new FormControl('', [
      Validators.required,
      Validators.maxLength(50),
      this.exists()
    ]),
    description: new FormControl('', [
      Validators.maxLength(250)
    ])
  });

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(private coverTypesDataService: CoverTypesDataService, private log: NGXLogger) { }

  ngOnInit() { }

  ngOnDestroy() {

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  /**
   * Internal validator that checks whether a Cover Type record already exists
   * @returns 
   */
  private exists(): ValidatorFn {

    const values: string[] = <Array<string>>this.coverTypesDataService.records.map(d => d.code);

    return (control: AbstractControl): ValidationErrors | null => {

      if (values != null && values.length > 0) {

        if (control.value) {

          const s: string = control.value;

          if (values.map(v => v.toLowerCase()).includes(s.toLowerCase())) {
            return { 'exists': true }
          }
        }
      }

      return null;
    }
  }

  /**
   * Validates and saves a new Cover Type record.
   * Emits a succeeded or failed event in response to whether or not the creation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.coverTypesForm.valid) {

      // Read in the provided code
      this.log.trace(`${LOG_PREFIX} Reading in the provided code`);
      const code: string | null = this.coverTypesForm.get('code') == null ? null : this.coverTypesForm.get('code')?.value;
      this.log.debug(`${LOG_PREFIX} Cover Type Code = ${code}`);

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.coverTypesForm.get('description') == null ? null : this.coverTypesForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Cover Type Description = ${description}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the Cover Type record`);
      this.coverTypesDataService
        .createCoverType(new CoverType({ code, description }))
        .subscribe(
          (response: CoverType) => {

            // The Cover Type record was saved successfully
            this.log.trace(`${LOG_PREFIX} Cover Type record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.coverTypesForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The Cover Type record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Cover Type record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
      this.validateAllFormFields(this.coverTypesForm);

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
