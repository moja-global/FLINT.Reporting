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
  selector: 'sb-issue-counts',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './issue-counts.component.html',
  styleUrls: ['issue-counts.component.scss'],
})
export class IssuesCountsComponent implements OnInit {

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

  getIssueCountsVisualizations(): Observable<Visualization[]>{
    return this.visualizationsDataService.getVisualizationsByGroup$(Group.SITUATION_ISSUE_COUNTS)
  }
  
}
