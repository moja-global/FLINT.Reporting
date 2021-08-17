import {
  ChangeDetectionStrategy,
  Component,
  HostListener,
  OnInit
} from '@angular/core';
import {
  VisualizationsDataService
} from '@modules/visualization/services';
import { NGXLogger } from 'ngx-logger';
import { Observable, Subscription } from 'rxjs';
import {
  Group,
  Visualization
} from '@modules/visualization/models';

const LOG_PREFIX: string = "[Issue Counts Visualization Component]";

@Component({
  selector: 'sb-issue-graphs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './issue-graphs.component.html',
  styleUrls: ['issue-graphs.component.scss'],
})
export class IssueGraphsComponent implements OnInit {

  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private visualizationsDataService: VisualizationsDataService,
    private log: NGXLogger) { }

  ngOnInit() {

  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  getProgressionChartsVisualizations(): Observable<Visualization[]>{
    return this.visualizationsDataService.getVisualizationsByGroup$(Group.SITUATION_PROGRESSION_CHARTS)
  }

  getOtherChartsVisualizations(): Observable<Visualization[]>{
    return this.visualizationsDataService.getVisualizationsByGroup$(Group.SITUATION_OTHER_CHARTS)
  }  
  
}
