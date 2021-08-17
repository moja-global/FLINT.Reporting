import {
  ChangeDetectionStrategy,
  Component,
  HostListener,
  Input,
  OnInit
} from '@angular/core';
import {
  QuantityObservationsDataService,
  UnitsDataService,
  VisualizationVariablesDataService} from '@modules/visualization/services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import {
  QuantityObservation,
  Role,
  Unit,
  VisualizationVariable,
  Visualization
} from '@modules/visualization/models';

const LOG_PREFIX: string = "[Count Component]";

@Component({
  selector: 'sb-count',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './count.component.html',
  styleUrls: ['count.component.scss'],
})
export class CountComponent implements OnInit {

  // Allow the parent component to specify what should be visualized
  @Input() visualization: Visualization | null = null;

  // The total number of Visualization Variables
  variableCount!: number;

  // Primary Visualization Variable Details
  primaryVisualizationVariable: VisualizationVariable | null = null;
  primaryVisualizationVariableQuantityObservation: QuantityObservation | null = null;
  primaryVisualizationVariableQuantityObservationUnit: Unit | null = null;

  // Secondary Visualization Variable Details
  secondaryVisualizationVariable: VisualizationVariable | null = null;
  secondaryVisualizationVariableQuantityObservation: QuantityObservation | null = null;
  secondaryVisualizationVariableQuantityObservationUnit: Unit | null = null;

  // Classes that are adjusted on the fly based on the visualization's color code
  customClasses: string[] = [];

  visualizationVariables: VisualizationVariable[] = [];
  quantityObservations: QuantityObservation[] = [];
  units: Unit[] = [];

  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];


  constructor(
    private quantityObservationsDataService: QuantityObservationsDataService,
    private unitsDataService: UnitsDataService,
    private visualizationVariablesDataService: VisualizationVariablesDataService,
    private log: NGXLogger) { }

  ngOnInit() {

    // Check if the Visualization Subject has been specified
    this.log.trace(`${LOG_PREFIX}  Checking if the Visualization Subject has been specified`);

    if (this.visualization) {

      // The Visualization Subject has been specified
      this.log.trace(`${LOG_PREFIX} The Visualization Subject has been specified`);
      this.log.debug(`${LOG_PREFIX} Visualization Subject = ${JSON.stringify(this.visualization)}`);

      // Retrieve the Visualization Subject's variables
      this.log.trace(`${LOG_PREFIX} Retrieving the Visualization Subject's Variables`);
      this.visualizationVariablesDataService.getVisualizationVariablesByVisualization$(this.visualization.id)
        .subscribe((results: VisualizationVariable[]) => {

          // Visualization Subject's Variables successfully retrieved
          this.log.trace(`${LOG_PREFIX} The Visualization Subject's Variables have been successfully retrieved`);
          this.log.debug(`${LOG_PREFIX} Visualization Variables = ${JSON.stringify(results)}`);

          // Take count of the total number of visualization variables
          this.log.trace(`${LOG_PREFIX} Taking count of the total number of visualization variables`);
          this.variableCount = results.length;

          // Collect the visualization variables together with their observations / observation units
          this.log.trace(`${LOG_PREFIX} Collecting the visualization variables together with their observations & observation units`);
          results.forEach((v: VisualizationVariable) => {


            // Keep a local reference to the visualization variable / styling class
            switch (v.roleId) {

              case Role.PRIMARY:
                this.primaryVisualizationVariable = v;
                break;

              case Role.SECONDARY:
                this.secondaryVisualizationVariable = v;
                break;
            }


            // Get the primary visualization variable's quantity observation
            this.quantityObservationsDataService.getQuantityObservationsByIndicator$(v.indicatorId)
              .subscribe((observations: QuantityObservation[]) => {
                if (observations.length > 0) {

                  // Keep a local reference to the visualization variable's observation
                  switch (v.roleId) {

                    case Role.PRIMARY:
                      this.primaryVisualizationVariableQuantityObservation = observations[0];
                      break;

                    case Role.SECONDARY:
                      this.secondaryVisualizationVariableQuantityObservation = observations[0];
                      break;
                  }


                  // Get the quantity observation's unit
                  if(this.primaryVisualizationVariableQuantityObservation){
                    this.unitsDataService.getUnit$(this.primaryVisualizationVariableQuantityObservation.id)
                    .subscribe((unit: Unit) => {

                      // Keep a local reference to the observation's unit
                      switch (v.roleId) {

                        case Role.PRIMARY:
                          this.primaryVisualizationVariableQuantityObservationUnit = unit;
                          break;

                        case Role.SECONDARY:
                          this.secondaryVisualizationVariableQuantityObservationUnit = unit;
                          break;
                      }

                    });
                  }

                }
              });


          });

        });

      // Carry out component initialization
      this.log.trace(`${LOG_PREFIX} Carrying out component initialization`);


    } else {

      // The Visualization Subject has been specified
      this.log.trace(`${LOG_PREFIX}  The Visualization Subject has been specified`);

      // Cannot initialize the component
      this.log.warn(`${LOG_PREFIX} Cannot initialize the component`);
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
