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
  import { FluxTypesDataService } from '../../services/flux-types-data.service';
  import { NGXLogger } from 'ngx-logger';
  import { Subscription } from 'rxjs';
  import { FluxType } from '../../../flux-types/models';
  
  const LOG_PREFIX: string = "[Flux Types Records Updation Component]";
  
  @Component({
    selector: 'sb-flux-types-records-updation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './flux-types-records-updation.component.html',
    styleUrls: ['flux-types-records-updation.component.scss'],
  })
  export class FluxTypesRecordsUpdationComponent implements OnInit, AfterContentInit {

  // Instantiate and avail the id variable to the parent component.
  // This will allow the parent component to inject the id of the 
  // record that has been served up for updation during the component's initialization.
  @Input() id!: number;

  // Instantiate an object to hold the details of the Flux Type record being updated.
  // This will allow the UI to get a hold on and prepolulate the current details of the 
  // record being updated
  fluxType: FluxType | undefined;

  // Instantiate a 'succeeded' state notification Emitter.
  // This will allow us to broadcast notifications of successful updation events
  @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate a 'failed' state notification Emitter.
  // This will allow us to broadcast fnotifications of failed updation events
  @Output() failed: EventEmitter<number> = new EventEmitter<number>();
  
  // Instantitate a new reactive Form Group for the Flux Types Form.
  // This will allow to define and enforce the validation rules for all the form controls.
    fluxTypesForm: FormGroup = new FormGroup({
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
  
  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];
  
    constructor(private fluxTypesDataService: FluxTypesDataService, private log: NGXLogger) { }
  
    ngOnInit() { 

        // Retrieve the Flux Type record with the given id from the data store 
        this.log.trace(`${LOG_PREFIX} Retrieving the Flux Type record with the given id from the data store`);
        this.log.debug(`${LOG_PREFIX} Flux Type record Id = ${this.id}`);
        this.fluxType = this.fluxTypesDataService.records.find(d => d.id == this.id);
    }

    ngAfterContentInit(){

        // If the Flux Type record has been successfully initialize, use it to initialize the form fields
        if (this.fluxType != undefined) {

            // The Flux Type record was successfully initialized
            this.log.debug(`${LOG_PREFIX} Flux Type record = ${JSON.stringify(this.fluxType)}`);

            // Initialize the Flux Type records form fields
            this.log.trace(`${LOG_PREFIX} Initializing the Flux Type records form fields`);
            this.fluxTypesForm.setValue({
                name: this.fluxType.name,
                description: this.fluxType.description
            });

        } else {
            this.log.error(`${LOG_PREFIX} Flux Type record ${this.id} was not found in the data store`);
        }
    }
  
    ngOnDestroy() {

      // Clear all subscriptions
      this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
      this._subscriptions.forEach((s) => s.unsubscribe());
  }
  
    /**
     * Internal validator that checks whether another Flux Type record with the same attribute already exists
     * @returns 
     */
    private exists(): ValidatorFn {
  
      const values: string[] = <Array<string>>this.fluxTypesDataService.records.map(d => d.name );
  
      return (control: AbstractControl): ValidationErrors | null => {
  
        if (this.fluxTypesDataService.records != null && this.fluxTypesDataService.records.length > 0) {
  
          if (control.value) {
  
            const s: string = control.value;

            const fluxType: FluxType | undefined = this.fluxTypesDataService.records.find(d => d.name?.toLowerCase() == s.toLowerCase());

            if(fluxType != undefined && fluxType.id != this.id){
              return { 'exists': true }
            }
          }
        }
  
        return null;
      }
    }
  
  /**
   * Validates and saves the updated Flux Type record.
   * Emits a succeeded or failed event in response to whether or not the updation exercise was successful.
   * Error 400 = Indicates an invalid Form Control Entry was supplied.
   * Error 500 = Indicates something unexpected happened at the server side
   */
    public save(): void {
  
      if (this.fluxTypesForm.valid) {

        // Form is invalid
        this.log.trace(`${LOG_PREFIX} Form is invalid`);         
  
        // Read in the provided name 
        this.log.trace(`${LOG_PREFIX} Reading in the provided name `);
        const name : string | null = this.fluxTypesForm.get('name') == null ? null : this.fluxTypesForm.get('name')?.value;
        this.log.debug(`${LOG_PREFIX} Flux Type Name  = ${name }`);



        // Read in the provided description
        this.log.trace(`${LOG_PREFIX} Reading in the provided description`);
        const description: string | null = this.fluxTypesForm.get('description') == null ? null : this.fluxTypesForm.get('description')?.value;
        this.log.debug(`${LOG_PREFIX} Flux Type Description = ${description}`);
  
        // Save the record
        this.log.trace(`${LOG_PREFIX} Saving the Flux Type record`);
        this.fluxTypesDataService
          .updateFluxType(new FluxType(Object.assign(this.fluxType, { name , description })))
          .subscribe(
            (response: FluxType) => {
  
              // The Flux Type record was saved successfully
              this.log.trace(`${LOG_PREFIX} The Flux Type record was successfuly updated`);
  
              // Reset the form
              this.log.trace(`${LOG_PREFIX} Resetting the form`);
              this.fluxTypesForm.reset();
  
              // Emit a 'succeeded' event
              this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
              this.succeeded.emit();
            },
            (error: any) => {
  
              // The Flux Type record was not saved successfully
              this.log.trace(`${LOG_PREFIX} The Flux Type record was not successfuly updated`);
  
              // Emit a 'failed' event
              this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
              this.failed.emit(500);
            });
      } else {

        // Form is invalid
        this.log.trace(`${LOG_PREFIX} Form is invalid`); 
        console.log(JSON.stringify(this.fluxTypesForm.get('name')?.errors));
  
        // Check which fields have issues
        this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
        this.validateAllFormFields(this.fluxTypesForm);
  
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
  