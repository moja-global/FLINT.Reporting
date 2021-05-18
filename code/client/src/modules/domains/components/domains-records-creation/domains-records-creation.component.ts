import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  OnInit,
  Output
} from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { DomainsDataService } from '../../services/domains-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { Domain } from '@modules/domains/models';

const LOG_PREFIX: string = "[Domains Records Creation Component]";

@Component({
  selector: 'sb-domains-records-creation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './domains-records-creation.component.html',
  styleUrls: ['domains-records-creation.component.scss'],
})
export class DomainsRecordsCreationComponent implements OnInit {

  // Instantiate a succeeded notification Emitter.
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a failed notification Emitter.
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();

  // Instantitate a new reactive Form Group.
  // This Form Group should mirror our Domain Form and provide each field with the appropriate validation tools
  domainsForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50),
      this.exists()
    ]),
    description: new FormControl('', [
      Validators.maxLength(250)
    ])
  });

  // Instantiate a gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all of them when the component is destroyed.   
  private _subscription: Subscription = new Subscription();

  constructor(private domainsDataService: DomainsDataService, private log: NGXLogger) { }

  ngOnInit() { }

  ngOnDestroy() {

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscription.unsubscribe();
  }

  /**
   * Internal validator that checks whether a domain record already exists
   * @returns 
   */
  private exists(): ValidatorFn {

    const values: string[] = <Array<string>>this.domainsDataService.records.map(d => d.name);

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
   * Validates and saves a new domain record.
   * Emits an succeeded or failed event indicating whether or not the saving exercise was successful.
   * Error 400 = Indicates an invalid or incomplete Form Entry was encountered.
   * Error 500 = Indicates something unexpected happened at the server side
   */
  public save(): void {

    if (this.domainsForm.valid) {

      // Read in the provided name
      this.log.trace(`${LOG_PREFIX} Reading in the provided name`);
      const name: string | null = this.domainsForm.get('name') == null ? null : this.domainsForm.get('name')?.value;
      this.log.debug(`${LOG_PREFIX} Domain Name = ${name}`);

      // Read in the provided description
      this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
      const description: string | null = this.domainsForm.get('description') == null ? null : this.domainsForm.get('description')?.value;
      this.log.debug(`${LOG_PREFIX} Domain Description = ${description}`);

      // Save the record
      this.log.trace(`${LOG_PREFIX} Saving the domain record`);
      this.domainsDataService
        .createDomain(new Domain({ name, description }))
        .subscribe(
          (response: Domain) => {

            // The domain record was saved successfully
            this.log.trace(`${LOG_PREFIX} Domain record was saved successfuly`);

            // Reset the form
            this.log.trace(`${LOG_PREFIX} Resetting the form`);
            this.domainsForm.reset();

            // Emit a 'succeeded' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
            this.succeeded.emit();
          },
          (error: any) => {

            // The domain record was not saved successfully
            this.log.trace(`${LOG_PREFIX} Domain record was not saved successfuly`);

            // Emit a 'failed' event
            this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
            this.failed.emit(500);
          });
    } else {

      // Validate the form fields
      this.log.trace(`${LOG_PREFIX} Validating the form fields`);
      this.validateAllFormFields(this.domainsForm);

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
