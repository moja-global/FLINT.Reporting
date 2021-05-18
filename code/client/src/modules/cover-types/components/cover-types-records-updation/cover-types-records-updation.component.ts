import {
    AfterContentInit,
    ChangeDetectionStrategy,
    Component,
    EventEmitter,
    Input,
    OnInit,
    Output
  } from '@angular/core';
  import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
  import { CoverTypesDataService } from '../../services/cover-types-data.service';
  import { NGXLogger } from 'ngx-logger';
  import { Subscription } from 'rxjs';
  import { CoverType } from '../../../cover-types/models';
  
  const LOG_PREFIX: string = "[Cover Types Records Updation Component]";
  
  @Component({
    selector: 'sb-cover-types-records-updation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './cover-types-records-updation.component.html',
    styleUrls: ['cover-types-records-updation.component.scss'],
  })
  export class CoverTypesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Cover Type record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  coverType: CoverType | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();
  
  // Instantitate a new reactive Form Group for the Cover Types Form.
  // This will allow to define and enforce the validation rules for all the form controls.
    coverTypesForm: FormGroup = new FormGroup({
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
  
    ngOnInit() { 

        // Retrieve the Cover Type record with the given id from the data store 
        this.log.trace(`${LOG_PREFIX} Retrieving the Cover Type record with the given id from the data store`);
        this.log.debug(`${LOG_PREFIX} Cover Type record Id = ${this.id}`);
        this.coverType = this.coverTypesDataService.records.find(d => d.id == this.id);
    }

    ngAfterContentInit(){

        // If the Cover Type record has been successfully initialize, use it to initialize the form fields
        if (this.coverType != undefined) {

            // The Cover Type record was successfully initialized
            this.log.debug(`${LOG_PREFIX} Cover Type record = ${JSON.stringify(this.coverType)}`);

            // Initialize the Cover Type records form fields
            this.log.trace(`${LOG_PREFIX} Initializing the Cover Type records form fields`);
            this.coverTypesForm.setValue({
                code: this.coverType.code,
                description: this.coverType.description
            });

        } else {
            this.log.error(`${LOG_PREFIX} Cover Type record ${this.id} was not found in the data store`);
        }
    }
  
    ngOnDestroy() {

      // Clear all subscriptions
      this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
      this._subscriptions.forEach((s) => s.unsubscribe());
  }
  
    /**
     * Internal validator that checks whether another Cover Type record with the same attribute already exists
     * @returns 
     */
    private exists(): ValidatorFn {
  
      const values: string[] = <Array<string>>this.coverTypesDataService.records.map(d => d.code );
  
      return (control: AbstractControl): ValidationErrors | null => {
  
        if (this.coverTypesDataService.records != null && this.coverTypesDataService.records.length > 0) {
  
          if (control.value) {
  
            const s: string = control.value;

            const coverType: CoverType | undefined = this.coverTypesDataService.records.find(d => d.code ?.toLowerCase().includes(s.toLocaleLowerCase()));

            if(coverType != undefined && coverType.id != this.id){
              return { 'exists': true }
            }
          }
        }
  
        return null;
      }
    }
  
  /**
   * Validates and saves the updated Cover Type record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
    public save(): void {
  
      if (this.coverTypesForm.valid) {

        // Form is invalid
        this.log.trace(`${LOG_PREFIX} Form is invalid`);         
  
        // Read in the provided code 
        this.log.trace(`${LOG_PREFIX} Reading in the provided code `);
        const code : string | null = this.coverTypesForm.get('code') == null ? null : this.coverTypesForm.get('code')?.value;
        this.log.debug(`${LOG_PREFIX} Cover Type Code  = ${code }`);



        // Read in the provided description
        this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
        const description: string | null = this.coverTypesForm.get('description') == null ? null : this.coverTypesForm.get('description')?.value;
        this.log.debug(`${LOG_PREFIX} Cover Type Description = ${description}`);
  
        // Save the record
        this.log.trace(`${LOG_PREFIX} Saving the Cover Type record`);
        this.coverTypesDataService
          .updateCoverType(new CoverType(Object.assign(this.coverType, { code , description })))
          .subscribe(
            (response: CoverType) => {
  
              // The Cover Type record was saved successfully
              this.log.trace(`${LOG_PREFIX} The Cover Type record was successfuly updated`);
  
              // Reset the form
              this.log.trace(`${LOG_PREFIX} Resetting the form`);
              this.coverTypesForm.reset();
  
              // Emit a 'succeeded' event
              this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
              this.succeeded.emit();
            },
            (error: any) => {
  
              // The Cover Type record was not saved successfully
              this.log.trace(`${LOG_PREFIX} The Cover Type record was not successfuly updated`);
  
              // Emit a 'failed' event
              this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
              this.failed.emit(500);
            });
      } else {

        // Form is invalid
        this.log.trace(`${LOG_PREFIX} Form is invalid`); 
        console.log(JSON.stringify(this.coverTypesForm.get('code')?.errors));
  
        // Check which fields have issues
        this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
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
  