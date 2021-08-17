import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output
} from '@angular/core';
import { ActivitiesDataService, PhenomenonTypesDataService, QuantityObservationsDataService, UnitsDataService } from '../../services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { Activity, PhenomenonType, QuantityObservation, Unit } from '../../models';

const LOG_PREFIX: string = "[Activity Result Component]";

@Component({
  selector: 'sb-activity-result',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './activity-result.component.html',
  styleUrls: ['activity-result.component.scss'],
})
export class ActivityResultComponent implements OnInit {

  // Allow the parent component to specify the target activity
  @Input() activity!: Activity;

  // Allow the parent component to specify the color of the target activity
  @Input() color!: string; 
  
  // Allow the parent component to specify the color of the target activity
  @Output() open: EventEmitter<Activity> = new EventEmitter();    

  // Primary Phenomenon Type Details
  primaryPhenomenonType!: PhenomenonType;
  primaryPhenomenonTypeQuantityObservation!: QuantityObservation;
  primaryPhenomenonTypeQuantityObservationUnit!: Unit;

  // Secondary Phenomenon Type Details
  secondaryPhenomenonType!: PhenomenonType;
  secondaryPhenomenonTypeQuantityObservation!: QuantityObservation;
  secondaryPhenomenonTypeQuantityObservationUnit!: Unit;

  // Classes that are adjusted on the fly based on the activity's color code
  customClasses: string[] = [];

  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = []; 

  onClick() {
    this.open.emit(this.activity);
  }

  constructor(
    private activitiesDataService: ActivitiesDataService,
    private phenomenonTypesDataService: PhenomenonTypesDataService,
    private quantityObservationsDataService: QuantityObservationsDataService,
    private unitsDataService: UnitsDataService,
    private log: NGXLogger) { }

  ngOnInit() {


    // Check if the activity id has been specified
    this.log.trace(`${LOG_PREFIX}  Checking if the activity has been specified`);

    if (this.activity != undefined) {

      

      // The activity has been specified
      this.log.trace(`${LOG_PREFIX} The activity has been specified`);
      this.log.debug(`${LOG_PREFIX} Activity = ${JSON.stringify(this.activity)}`);

      // Carry out component initialization
      this.log.trace(`${LOG_PREFIX} Carrying out component initialization`);
      this.initPrimaryObservation(this.activity);
      this.initSecondaryObservation(this.activity);

    } else {

      // The activity id has not been specified
      this.log.trace(`${LOG_PREFIX} The activity id has not been specified`);

      // Cannot initialize the component
      this.log.warn(`${LOG_PREFIX} Cannot initialize the component`);
    }

  }


  private initPrimaryObservation(result: Activity): void {

    // Check if the Primary Phenomenon Type Id has been specified
    this.log.trace(`${LOG_PREFIX} Checking if the Primary Phenomenon Type Id has been specified`);
    if (result.primaryPhenomenonTypeId != -1) {

      // The Primary Phenomenon Type Id has been specified
      this.log.trace(`${LOG_PREFIX} The Primary Phenomenon Type Id has been specified`);
      this.log.debug(`${LOG_PREFIX} Primary Phenomenon Type Id = ${result.primaryPhenomenonTypeId}`);

      // Initialize the Primary Phenomenon Type
      this.phenomenonTypesDataService.getPhenomenonType$(result.primaryPhenomenonTypeId)
        .subscribe((result: PhenomenonType) => {

          // Initialize the Primary Phenomenon Type
          this.log.trace(`${LOG_PREFIX} Initializing the Primary Phenomenon Type`);
          this.log.debug(`${LOG_PREFIX} Primary Phenomenon Type = ${JSON.stringify(result)}`);
          this.primaryPhenomenonType = result;

          // Initialize the Primary Phenomenon Type's Quantity Observation
          this.quantityObservationsDataService.getActivitiesQuantityObservations$(result.id)
            .subscribe((result: QuantityObservation) => {

              // Initialize the Primary Phenomenon Type's Quantity Observation
              this.log.trace(`${LOG_PREFIX} Initializing the Primary Phenomenon Type's Quantity Observation`);
              this.log.debug(`${LOG_PREFIX} Primary Phenomenon Type's Quantity Observation = ${JSON.stringify(result)}`);
              this.primaryPhenomenonTypeQuantityObservation = result;

              // Initialize the Primary Phenomenon Type's Quantity Observation's Unit
              this.unitsDataService.getUnit$(result.unitId)
                .subscribe((result: Unit) => {

                  // Initialize the Primary Phenomenon Type's Quantity Observation's Unit
                  this.log.trace(`${LOG_PREFIX} Initializing the Primary Phenomenon Type's Quantity Observation's Unit`);
                  this.log.debug(`${LOG_PREFIX} Primary Phenomenon Type's Quantity Observation's Unit = ${JSON.stringify(result)}`);
                  this.primaryPhenomenonTypeQuantityObservationUnit = result;

                });


            });
        });

    } else {

      // The Primary Phenomenon Type Id has not been specified
      this.log.trace(`${LOG_PREFIX} The Primary Phenomenon Type Id has not been specified`);
    }
  }


  private initSecondaryObservation(result: Activity): void {

    // Check if the Secondary Phenomenon Type Id has been specified
    this.log.trace(`${LOG_PREFIX} Checking if the Secondary Phenomenon Type Id has been specified`);
    if (result.secondaryPhenomenonTypeId != -1) {

      // The Secondary Phenomenon Type Id has been specified
      this.log.trace(`${LOG_PREFIX} The Secondary Phenomenon Type Id has been specified`);
      this.log.debug(`${LOG_PREFIX} Secondary Phenomenon Type Id = ${result.secondaryPhenomenonTypeId}`);

      // Initialize the Secondary Phenomenon Type
      this.phenomenonTypesDataService.getPhenomenonType$(result.secondaryPhenomenonTypeId)
        .subscribe((result: PhenomenonType) => {

          // Initialize the Secondary Phenomenon Type
          this.log.trace(`${LOG_PREFIX} Initializing the Secondary Phenomenon Type`);
          this.log.debug(`${LOG_PREFIX} Secondary Phenomenon Type = ${JSON.stringify(result)}`);
          this.secondaryPhenomenonType = result;

          // Initialize the Secondary Phenomenon Type's Quantity Observation
          this.quantityObservationsDataService.getActivitiesQuantityObservations$(result.id)
            .subscribe((result: QuantityObservation) => {

              // Initialize the Secondary Phenomenon Type's Quantity Observation
              this.log.trace(`${LOG_PREFIX} Initializing the Secondary Phenomenon Type's Quantity Observation`);
              this.log.debug(`${LOG_PREFIX} Secondary Phenomenon Type's Quantity Observation = ${JSON.stringify(result)}`);
              this.secondaryPhenomenonTypeQuantityObservation = result;

              // Initialize the Secondary Phenomenon Type's Quantity Observation's Unit
              this.unitsDataService.getUnit$(result.unitId)
                .subscribe((result: Unit) => {

                  // Initialize the Secondary Phenomenon Type's Quantity Observation's Unit
                  this.log.trace(`${LOG_PREFIX} Initializing the Secondary Phenomenon Type's Quantity Observation's Unit`);
                  this.log.debug(`${LOG_PREFIX} Secondary Phenomenon Type's Quantity Observation's Unit = ${JSON.stringify(result)}`);
                  this.secondaryPhenomenonTypeQuantityObservationUnit = result;

                });


            });
        });

    } else {

      // The Secondary Phenomenon Type Id has not been specified
      this.log.trace(`${LOG_PREFIX} The Secondary Phenomenon Type Id has not been specified`);
    }
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }


}
