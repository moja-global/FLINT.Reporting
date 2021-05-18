import {
    AfterContentInit,
    AfterViewInit,
    ChangeDetectionStrategy,
    Component,
    EventEmitter,
    Input,
    OnInit,
    Output
  } from '@angular/core';
  import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
  import { DomainsDataService } from '../../services/domains-data.service';
  import { NGXLogger } from 'ngx-logger';
  import { Subscription } from 'rxjs';
  import { Domain } from '@modules/domains/models';
  
  const LOG_PREFIX: string = "[Domains Records Updation Component]";
  
  @Component({
    selector: 'sb-domains-records-updation',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './domains-records-updation.component.html',
    styleUrls: ['domains-records-updation.component.scss'],
  })
  export class DomainsRecordsUpdationComponent implements OnInit, AfterContentInit {

    // Instantiate and avail the id variable to the parent component
    @Input() id!: number;

    // Instantiate an object to hold the details domain record being edited
    private _domain!: Domain | undefined;
  
    // Instantiate a succeeded notification Emitter.
    @Output() succeeded: EventEmitter<void> = new EventEmitter<void>();
  
    // Instantiate a failed notification Emitter.
    @Output() failed: EventEmitter<number> = new EventEmitter<number>();
  
    // Instantitate a new reactive Form Group.
    // This Form Group should mirror our Domain Form and provide each field with the appropriate validation tools
    domainsForm: FormGroup = new FormGroup({
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

    ngAfterContentInit(){

        // Retrieve the domain with the given id from the data store 
        this.log.trace(`${LOG_PREFIX} Retrieving the domain with the given id from the data store`);
        this.log.debug(`${LOG_PREFIX} Domain Record Id = ${this.id}`);
        this._domain = this.domainsDataService.records.find(d => d.id == this.id);
        
        // If the record was found (index != -1), remove it from the Local Cache
        if (this._domain != undefined) {

            this.log.debug(`${LOG_PREFIX} Domain Record = ${JSON.stringify(this._domain)}`);

            // Initialize the data form
            this.log.trace(`${LOG_PREFIX} Initializing the data form`);
            this.domainsForm.setValue({
                name: this._domain.name,
                description: this._domain.description
            });

        } else {
            this.log.error(`${LOG_PREFIX} Domain Record ${this.id} was not found in the data store`);
        }
    }
  
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
  
        if (this.domainsDataService.records != null && this.domainsDataService.records.length > 0) {
  
          if (control.value) {
  
            const s: string = control.value;

            const domain: Domain | undefined = this.domainsDataService.records.find(d => d.name?.toLowerCase() == s.toLowerCase());

            if(domain != undefined && domain.id != this.id){
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

        // Form is invalid
        this.log.trace(`${LOG_PREFIX} Form is invalid`);         
  
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
          .updateDomain(new Domain(Object.assign(this._domain, { name, description })))
          .subscribe(
            (response: Domain) => {
  
              // The domain record was saved successfully
              this.log.trace(`${LOG_PREFIX} Domain record was updated successfuly`);
  
              // Reset the form
              this.log.trace(`${LOG_PREFIX} Resetting the form`);
              this.domainsForm.reset();
  
              // Emit a 'succeeded' event
              this.log.trace(`${LOG_PREFIX} Emitting a 'succeeded' event`);
              this.succeeded.emit();
            },
            (error: any) => {
  
              // The domain record was not saved successfully
              this.log.trace(`${LOG_PREFIX} Domain record was not updated successfuly`);
  
              // Emit a 'failed' event
              this.log.trace(`${LOG_PREFIX} Emitting a 'failed' event`);
              this.failed.emit(500);
            });
      } else {

        // Form is invalid
        this.log.trace(`${LOG_PREFIX} Form is invalid`); 
        console.log(JSON.stringify(this.domainsForm.get('name')?.errors));
  
        // Check which fields have issues
        this.log.trace(`${LOG_PREFIX} Checking which fields have issues`);
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
  