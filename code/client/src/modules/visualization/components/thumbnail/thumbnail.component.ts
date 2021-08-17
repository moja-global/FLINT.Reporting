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

const LOG_PREFIX: string = "[Thumbnail Component]";

@Component({
  selector: 'sb-thumbnail',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './thumbnail.component.html',
  styleUrls: ['thumbnail.component.scss'],
})
export class ThumbnailComponent implements OnInit {

  // Allow the parent component to specify the thumbnail details
  @Input() details: {img: string, title: string, subtitle: string, link: string} | null = null;


  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private log: NGXLogger) { }

  ngOnInit() {

      // Carry out component initialization
      this.log.trace(`${LOG_PREFIX} Carrying out component initialization`);
      this.log.debug(`${LOG_PREFIX} Details = ${JSON.stringify(this.details)}`);


  }


  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

}
