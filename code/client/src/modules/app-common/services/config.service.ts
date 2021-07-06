import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChartConfig } from '@common/models';
import { NGXLogger } from 'ngx-logger';
import { Observable, of, throwError } from 'rxjs';

export const chartsConfigurations: ChartConfig[] = [
  {
    "id": 1,
    "title": "Total GHG Emissions",
    "subtitle": "",
    "uri": "",
    "dataLabelledLineChart": false,
    "percentageAreaChart": false,
    "basicColumnChart": true,
    "stackedPercentageColumnChart": false,
    "selectedChart": "basicColumnChart",
    "chartYAxisLabel": "Emissions (kt)",
    "chartXAxisLabel": "Years",
    "units": "kg",
    "showSeriesLabelsInLegend": true
  },
  {
    "id": 2,
    "title": "Total GHG Emissions - By Carbon Pools",
    "subtitle": "",
    "uri": "",
    "dataLabelledLineChart": false,
    "percentageAreaChart": false,
    "basicColumnChart": true,
    "stackedPercentageColumnChart": false,
    "selectedChart": "basicColumnChart",
    "chartYAxisLabel": "Emissions (kt)",
    "chartXAxisLabel": "Years",
    "units": "kg",
    "showSeriesLabelsInLegend": true
  },
  {
    "id": 3,
    "title": "Total GHG Emissions - By Gases",
    "subtitle": "",
    "uri": "",
    "dataLabelledLineChart": false,
    "percentageAreaChart": false,
    "basicColumnChart": true,
    "stackedPercentageColumnChart": false,
    "selectedChart": "basicColumnChart",
    "chartYAxisLabel": "Emissions (kt)",
    "chartXAxisLabel": "Years",
    "units": "kg",
    "showSeriesLabelsInLegend": true
  }
];

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  constructor(
    private log: NGXLogger,
    private http: HttpClient) {
  }

  public getChartsConfigurations(): Observable<ChartConfig[]> {

    this.log.trace("Entering getChartsConfigurations()");
    return of(chartsConfigurations);
  }

}
