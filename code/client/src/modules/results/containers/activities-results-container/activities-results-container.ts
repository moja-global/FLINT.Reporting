import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  HostListener,
  OnInit
} from '@angular/core';
import { ActivitiesDataService, DomainsDataService } from '../../services';
import { NGXLogger } from 'ngx-logger';
import { Observable, Subscription } from 'rxjs';
import { VisualizationsDataService } from '@modules/visualization/services/visualizations-data.service';
import { Visualization } from '@modules/visualization/models/visualization.model';
import { Group } from '@modules/visualization/models/group.enum';
import { Activity } from '@modules/results/models/activity.model';

const LOG_PREFIX: string = "[Progression Container Component]";

@Component({
  selector: 'sb-activities-results',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './activities-results-container.html',
  styleUrls: ['activities-results-container.scss'],
})
export class ActivitiesResultsContainerComponent implements OnInit {

  view: string = "decks";
  activity: Activity = {
    id: -1,
    domainId: -1,
    name: "",
    icon: "",
    primaryPhenomenonTypeId: -1,
    secondaryPhenomenonTypeId: -1
  };

  //The Chart
  chart: any = null;

  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private cd: ChangeDetectorRef,
    public domainsDataService: DomainsDataService,
    public activitiesDataService: ActivitiesDataService,
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

  getActivityResultsChartsVisualizations(): Observable<Visualization[]> {
    return this.visualizationsDataService.getVisualizationsByGroup$(Group.ACTIVITY_RESULTS_CHARTS)
  }

  onOpen(activity: Activity) {
    this.activity = activity;
    this.view = "details";
    
  }

  onClose() {
    this.view = "decks"
  }


    
  


}
