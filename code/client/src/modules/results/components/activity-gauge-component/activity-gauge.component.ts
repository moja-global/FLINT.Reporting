import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
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

import { ResizedEvent } from 'angular-resize-event';
import * as Highcharts from 'highcharts';
import exporting from 'highcharts/modules/exporting';
import offline from 'highcharts/modules/offline-exporting';


declare var require: any;
let Boost = require('highcharts/modules/boost');
let Bullet = require('highcharts/modules/bullet');
let noData = require('highcharts/modules/no-data-to-display');
let More = require('highcharts/highcharts-more');


Boost(Highcharts);
Bullet(Highcharts);
noData(Highcharts);
More(Highcharts);
noData(Highcharts);
exporting(Highcharts);
offline(Highcharts);

const LOG_PREFIX: string = "[Activity Gauge Component]";

@Component({
  selector: 'sb-activity-gauge',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './activity-gauge.component.html',
  styleUrls: ['activity-gauge.component.scss'],
})
export class ActivityGaugeComponent implements OnInit {


  //The Chart
  chart: any = null;

  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private cd: ChangeDetectorRef,
    private log: NGXLogger) { }

  ngOnInit() {

  }

  ngAfterViewInit() {
    this.drawChart();
  }


  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }


  drawChart() {

    let options: any = {

      chart: {
        inverted: true,
        marginLeft: 135,
        marginTop: 40,
        type: 'bullet'
      },
      title: {
        text: ''
      },
      legend: {
        enabled: false
      },
      xAxis: {
        categories: ['Drums']
      },
      yAxis: {
        gridLineWidth: 0,
        plotBands: [{
          from: 0,
          to: 153,
          color: '#ffadad'
        }, {
          from: 153,
          to: 306,
          color: '#ffd6a5'
        }, {
          from: 306,
          to: 459,
          color: '#fdffb6'
        }, {
          from: 459,
          to: 9e9,
          color: '#caffbf'
        }],
        title: null
      },
      plotOptions: {
        series: {
          pointPadding: 0.25,
          borderWidth: 0,
          color: '#000',
          targetOptions: {
            width: '200%'
          }
        }
      },
      series: [{
        data: [{
          y: 285,
          target: 612
        }]
      }],
      tooltip: {
        pointFormat: '<b>{point.y}</b> (with target at {point.target})'
      },
      credits: {
        enabled: false
      },
      exporting: {
        enabled: false
      }
    };

    this.chart = Highcharts.chart("progress", options, () => {
      this.cd.detectChanges();
    });




  }

  onResized(event: ResizedEvent) {
    if (this.chart != null) {
      this.chart.reflow();
    }
  }
}
