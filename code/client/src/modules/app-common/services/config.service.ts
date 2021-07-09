import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChartConfig } from '@common/models/chart-config.model';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';

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

    // Land Use Categories / Subcategories Ids
    forestLandId: number = 1;
    forestLandRemainingForestLandId: number = 2;
    landConvertedToForestLandId: number = 3;
    croplandConvertedToForestLandId: number = 4;
    grasslandConvertedToForestLandId: number = 5;
    wetlandsConvertedToForestLandId: number = 6;
    settlementsConvertedToForestLandId: number = 7;
    otherLandConvertedToForestLandId: number = 8;

    cropland:number = 9;
    croplandRemainingCroplandId:number = 10;
    landConvertedToCroplandId:number = 11;
    forestLandConvertedToCroplandId:number = 12;
    grasslandConvertedToCroplandId:number = 13;
    wetlandsConvertedToCroplandId:number = 14;
    settlementsConvertedToCroplandId:number = 15;
    otherLandConvertedToCroplandId:number = 16;
    
    grasslandId:number = 17;
    grasslandRemainingGrasslandId:number = 18;
    landConvertedToGrasslandId:number = 19;
    croplandConvertedToGrasslandId:number = 20;
    forestLandConvertedToGrasslandId:number = 21;
    wetlandsConvertedToGrasslandId:number = 22;
    settlementsConvertedToGrasslandId:number = 23;
    otherLandConvertedToGrasslandId:number = 24;
    
    wetlandsId:number = 25;
    wetlandsRemainingWetlandsId:number = 26;
    landConvertedToWetlandsId:number = 27;
    croplandConvertedToWetlandsId:number = 28;
    grasslandConvertedToWetlandsId:number = 29;
    forestLandConvertedToWetlandsId:number = 30;
    settlementsConvertedToWetlandsId:number = 31;
    otherLandConvertedToWetlandsId:number = 32;    


    // Reporting Variables Ids    
    areaReportingVariableId: number = 1;
    netCarbonStockChangeInLivingBiomassReportingVariableId: number = 2;
    netCarbonStockChangeInDeadOrganicMatterReportingVariableId: number = 3;
    netCarbonStockChangeInMineralSoilsReportingVariableId: number = 4;
    netCarbonStockChangeInOrganicSoilsReportingVariableId: number = 5;
    netCarbonDioxideEmissionsRemovalsReportingVariableId: number = 6;
    methaneReportingVariableId: number = 7;
    nitrousOxideReportingVariableId: number = 8;  

  constructor(
    private log: NGXLogger,
    private http: HttpClient) {
  }

  // Charts 

  public getChartsConfigurations(): Observable<ChartConfig[]> {

    this.log.trace("Entering getChartsConfigurations()");
    return of(chartsConfigurations);
  }



}
