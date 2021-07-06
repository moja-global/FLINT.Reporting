import { Component, OnInit, Input, ChangeDetectorRef, AfterViewInit, HostListener } from '@angular/core';
import * as Highcharts from 'highcharts';
import { NGXLogger } from 'ngx-logger';
import exporting from 'highcharts/modules/exporting';
import offline from 'highcharts/modules/offline-exporting';
import { QuantityObservation } from '@modules/quantity-observations/models/quantity-observation.model';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { ConfigService } from '@common/services/config.service';
import { ChartConfig} from '@common/models/chart-config';
import { Serie } from '@common/models/serie';
import { QuantityObservationsRecordsFilterService } from '@modules/quantity-observations/services/quantity-observations-records-filter.service';

declare var require: any;
let Boost = require('highcharts/modules/boost');
let noData = require('highcharts/modules/no-data-to-display');
let More = require('highcharts/highcharts-more');

Boost(Highcharts);
noData(Highcharts);
More(Highcharts);
noData(Highcharts);
exporting(Highcharts);
offline(Highcharts);

const dateOptions = {
    month: "short",
    day: "numeric"
};

const LOG_PREFIX: string = "[Chart-Component]";

@Component({
    selector: 'app-chart',
    templateUrl: './chart.component.html',
    styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnInit, AfterViewInit {

    // Chart configurations
    @Input() chartId: number = 0;

    config: any = null;
    selectedChart: string = "";

    // Loading animation
    loading: boolean = true;

    // chart count - total amount of charts that have been selected for display
    chartCount: number = 0;

    // Allows the year selection drop downs to keep tabs of the current status of years
    private _yearsSubject$: BehaviorSubject<Array<number>> = new BehaviorSubject<Array<number>>([]);
    readonly years$: Observable<Array<number>> = this._yearsSubject$.asObservable();

    observations: QuantityObservation[] = [];

    // Land Use Categories / Subcategories Ids
    forestLandId: number = 1;
    forestLandRemainingForestLandId: number = 2;
    landConvertedToForestLandId: number = 3;
    croplandConvertedToForestLandId: number = 4;
    grasslandConvertedToForestLandId: number = 5;
    wetlandsConvertedToForestLandId: number = 6;
    settlementsConvertedToForestLandId: number = 7;
    otherLandConvertedToForestLandId: number = 8;


    // Reporting Variables Ids    
    areaReportingVariableId: number = 1;
    netCarbonStockChangeInLivingBiomassReportingVariableId: number = 2;
    netCarbonStockChangeInDeadOrganicMatterReportingVariableId: number = 3;
    netCarbonStockChangeInMineralSoilsReportingVariableId: number = 4;
    netCarbonStockChangeInOrganicSoilsReportingVariableId: number = 5;
    netCarbonDioxideEmissionsRemovalsReportingVariableId: number = 6;
    methaneReportingVariableId: number = 7;
    nitrousOxideReportingVariableId: number = 8;


    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];



    // Line charts

    private lineChartWithDataLabelsOptions: any = {
        chart: {
            type: 'line'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: 'source: moja.global'
        },
        xAxis: {
            categories: []
        },
        yAxis: {
            title: {
                text: ''
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        series: [],
        credits: {
            enabled: false
        }
    }


    //Area charts

    private percentageAreaChartOptions: any = {
        chart: {
            type: 'area'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: 'source: moja.global'
        },
        xAxis: {
            categories: [],
            tickmarkPlacement: 'on',
            title: {
                enabled: false
            }
        },
        yAxis: {
            labels: {
                format: '{value}%'
            },
            title: {
                enabled: false
            }
        },
        tooltip: {
            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.percentage:.1f}%</b><br/>',
            split: true
        },
        plotOptions: {
            area: {
                stacking: 'percent',
                lineColor: '#ffffff',
                lineWidth: 1,
                marker: {
                    lineWidth: 1,
                    lineColor: '#ffffff'
                },
                accessibility: {
                    pointDescriptionFormatter: function (point: any) {
                        function round(x: any) {
                            return Math.round(x * 100) / 100;
                        }
                        return (point.index + 1) + ', ' + point.category + ', ' +
                            point.y + ', ' + round(point.percentage) + '%, ' +
                            point.series.name;
                    }
                }
            }
        },
        series: [],
        credits: {
            enabled: false
        }
    }


    //Column charts

    private basicColumnChartOptions: any = {
        chart: {
            type: 'column'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: 'source: moja.global'
        },
        xAxis: {
            categories: [],
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: ''
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [],
        credits: {
            enabled: false
        }
    }


    private stackedPercentageColumnChartOptions: any = {
        chart: {
            type: 'column'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: 'source: moja.global'
        },
        xAxis: {
            categories: []
        },
        yAxis: {
            min: 0,
            title: {
                text: ''
            }
        },
        tooltip: {
            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>',
            shared: true
        },
        plotOptions: {
            column: {
                stacking: 'normal'
            }
        },
        series: [],
        credits: {
            enabled: false
        }
    }


    constructor(
        private configService: ConfigService,
        public quantityObservationsTableService: QuantityObservationsRecordsFilterService,
        private cd: ChangeDetectorRef,
        private log: NGXLogger) {

    }

    ngOnInit(): void {



    }

    ngAfterViewInit(): void {

        this.loadConfiguration(() => {

            // Subscribe to quantity observations data notifications.
            this.log.trace(`${LOG_PREFIX} Subscribing to quantity observations data notifications`);
            this._subscriptions.push(
                this.quantityObservationsTableService.quantityObservations$.subscribe(
                    data => {
                        this.observations = data;                   },
                    error => {
                        this.log.trace(`${LOG_PREFIX} Could not load quantity observations`);
                        this.observations = [];
                    }));            

            // Subscribe to loading events and propagate them to the loading component.
            // Loading events occur when the user changes the filter status
            this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
            this._subscriptions.push(
                this.quantityObservationsTableService.loading$.subscribe(
                    (loading) => {
                        this.loading = loading;
                        this.cd.detectChanges();
                    }));

            // Subscribe to data filters changes notifications.
            this.log.trace(`${LOG_PREFIX} Subscribing to data filter changes notifications`);
            this._subscriptions.push(
                this.quantityObservationsTableService.filter$.subscribe(
                    databaseFilter => {

                        let startYear: number = databaseFilter.startYear == undefined || databaseFilter.startYear == null ? -1 : databaseFilter.startYear;
                        let endYear: number = databaseFilter.endYear == undefined || databaseFilter.endYear == null ? -1 : databaseFilter.endYear;

                        if (startYear != -1 && endYear != -1) {
                            let years: number[] = [];
                            for (let i = startYear; i <= endYear; i++) {
                                years.push(i);
                            }
                            this._yearsSubject$.next(years);
                        } else {
                            this._yearsSubject$.next([]);
                        }

                        this.loadChart();                         
                    },
                    error => {
                        this.log.trace(`${LOG_PREFIX} Could not load quantity observations`);
                        this.observations = [];
                    }));


        });


    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach(s => s.unsubscribe());
    }

    private loadConfiguration(callback: () => void): void {

        this.log.trace(`${LOG_PREFIX} Loading charts configuration`);

        // Reset the chart count
        this.chartCount = 0;

        this.configService
            .getChartsConfigurations()
            .subscribe((configurations: ChartConfig[]) => {

                this.log.debug(`${LOG_PREFIX} Looking for Visual Component ${this.chartId}`);
                let targetConfiguration = configurations.find(c => c.id == this.chartId);
                if (typeof targetConfiguration !== 'undefined') {

                    this.log.trace(`${LOG_PREFIX} Visual Component ${this.chartId} found`);

                    // Update the target configuration details
                    this.log.trace(`${LOG_PREFIX} Updating the target configuration details`);
                    this.config = targetConfiguration;

                    // Set the default selected chart
                    this.selectedChart = targetConfiguration.selectedChart;

                    // Update the charts config / chart count number
                    if (targetConfiguration.dataLabelledLineChart) {

                        // Update the chart count
                        this.chartCount += 1;
                    }

                    if (targetConfiguration.percentageAreaChart) {

                        // Update the chart count
                        this.chartCount += 1;
                    }

                    if (targetConfiguration.basicColumnChart) {

                        // Update the chart tooltip
                        this.log.trace(`${LOG_PREFIX} Setting the tooltip`);

                        this.basicColumnChartOptions.tooltip = {

                            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                '<td style="padding:0"><b>{point.y:.1f} ' + this.config.units + '</b></td></tr>',
                            footerFormat: '</table>',
                            shared: true,
                            useHTML: true
                        };

                        // Update the chart count

                        this.chartCount += 1;
                    }

                    if (targetConfiguration.stackedPercentageColumnChart) {

                        // Update the chart tooltip
                        this.log.trace(`${LOG_PREFIX} Setting the tooltip`);

                        this.stackedPercentageColumnChartOptions.tooltip = {
                            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b>' + this.config.units + '<br/>',
                            shared: true
                        };

                        // Update the chart count                        
                        this.chartCount += 1;
                    }

                    callback();

                } else {
                    this.log.warn(`${LOG_PREFIX} Visual Component ${this.chartId} was not found`);
                }
            }, error => {

                // Print error message
                this.log.error(`${LOG_PREFIX} Configurations retrieval failed`);

            });
    }


    private loadChart(): void {

        let xcategories: number[] = [];
        let ycategories: Serie[] = [];

        this._yearsSubject$.value.forEach(year => {

            // Set the year
            xcategories.push(year);

            // Set the year's value depending on the desired chart
            if (this.chartId == 1) {

                let a = ycategories.find(s => s.name === "Emissions (kt CO2-eq)");

                if (typeof a == 'undefined') {
                    a = { showInLegend: this.config.showSeriesLabelsInLegend, name: "Emissions (kt CO2-eq)", data: [this.getTotalCO2EquivalentValue(year)] };
                    ycategories.push(a);
                } else {
                    a.data.push(this.getTotalCO2EquivalentValue(year));
                }

            } else if (this.chartId == 2) {

                let a = ycategories.find(s => s.name === "Net carbon stock change in living biomass (kt C)");
                
                if (typeof a == 'undefined') {
                    a = { showInLegend: this.config.showSeriesLabelsInLegend, name: "Net carbon stock change in living biomass (kt C)", 
                    data: [this.getTotalReportingVariableValue(this.netCarbonStockChangeInLivingBiomassReportingVariableId, year)] };
                    ycategories.push(a);
                } else {
                    a.data.push(this.getTotalReportingVariableValue(this.netCarbonStockChangeInLivingBiomassReportingVariableId, year));
                } 
                
                
                
                let b = ycategories.find(s => s.name === "Net carbon stock change in dead organic matter (kt C)");
                
                if (typeof b == 'undefined') {
                    b = { showInLegend: this.config.showSeriesLabelsInLegend, name: "Net carbon stock change in dead organic matter (kt C)", 
                    data: [this.getTotalReportingVariableValue(this.netCarbonStockChangeInDeadOrganicMatterReportingVariableId, year)] };
                    ycategories.push(b);
                } else {
                    b.data.push(this.getTotalReportingVariableValue(this.netCarbonStockChangeInDeadOrganicMatterReportingVariableId, year));
                }
                
                
                let c = ycategories.find(s => s.name === "Net carbon stock change in mineral soils (kt C)");
                
                if (typeof c == 'undefined') {
                    c = { showInLegend: this.config.showSeriesLabelsInLegend, name: "Net carbon stock change in mineral soils (kt C)", 
                    data: [this.getTotalReportingVariableValue(this.netCarbonStockChangeInMineralSoilsReportingVariableId, year)] };
                    ycategories.push(c);
                } else {
                    c.data.push(this.getTotalReportingVariableValue(this.netCarbonStockChangeInMineralSoilsReportingVariableId, year));
                }                  
                

                let d = ycategories.find(s => s.name === "Net carbon stock change in organic soils (kt C)");
                
                if (typeof d == 'undefined') {
                    d = { showInLegend: this.config.showSeriesLabelsInLegend, name: "Net carbon stock change in organic soils (kt C)", 
                    data: [this.getTotalReportingVariableValue(this.netCarbonStockChangeInOrganicSoilsReportingVariableId, year)] };
                    ycategories.push(d);
                } else {
                    d.data.push(this.getTotalReportingVariableValue(this.netCarbonStockChangeInOrganicSoilsReportingVariableId, year));
                }  



            } else if (this.chartId == 3) {


                let a = ycategories.find(s => s.name === "CO2 (kt)");
                
                if (typeof a == 'undefined') {
                    a = { showInLegend: this.config.showSeriesLabelsInLegend, name: "CO2 (kt)", 
                    data: [this.getTotalReportingVariableValue(this.netCarbonDioxideEmissionsRemovalsReportingVariableId, year)] };
                    ycategories.push(a);
                } else {
                    a.data.push(this.getTotalReportingVariableValue(this.netCarbonDioxideEmissionsRemovalsReportingVariableId, year));
                } 
                
                
                
                let b = ycategories.find(s => s.name === "CH4 (kt)");
                
                if (typeof b == 'undefined') {
                    b = { showInLegend: this.config.showSeriesLabelsInLegend, name: "CH4 (kt)", 
                    data: [this.getTotalReportingVariableValue(this.methaneReportingVariableId, year)] };
                    ycategories.push(b);
                } else {
                    b.data.push(this.getTotalReportingVariableValue(this.methaneReportingVariableId, year));
                }
                
                
                let c = ycategories.find(s => s.name === "N2O (kt)");
                
                if (typeof c == 'undefined') {
                    c = { showInLegend: this.config.showSeriesLabelsInLegend, name: "N2O (kt)", 
                    data: [this.getTotalReportingVariableValue(this.nitrousOxideReportingVariableId, year)] };
                    ycategories.push(c);
                } else {
                    c.data.push(this.getTotalReportingVariableValue(this.nitrousOxideReportingVariableId, year));
                } 

            }

        });

        this.log.debug(`${LOG_PREFIX} x axis: ${JSON.stringify(xcategories)}`);
        this.log.debug(`${LOG_PREFIX} y axis: ${JSON.stringify(ycategories)}`);

        this.log.trace(`${LOG_PREFIX} Setting loading status to false`);
        this.loading = false;

        // Prepare and load the required graphs

        if (this.config.dataLabelledLineChart) {
            //this.lineChartWithDataLabelsOptions.title = { text: this.config.title };
            //this.lineChartWithDataLabelsOptions.subtitle = { text: this.config.subtitle };
            this.lineChartWithDataLabelsOptions.xAxis = { title: { text: this.config.chartXAxisLabel }, categories: xcategories };
            this.lineChartWithDataLabelsOptions.yAxis = { title: { text: this.config.chartYAxisLabel } };
            this.lineChartWithDataLabelsOptions.series = ycategories;
            Highcharts.chart(`line${this.chartId}Container`, this.lineChartWithDataLabelsOptions);
        }


        if (this.config.percentageAreaChart) {
            //this.percentageAreaChartOptions.title = { text: this.config.title };
            //this.percentageAreaChartOptions.subtitle = { text: this.config.subtitle };
            this.percentageAreaChartOptions.xAxis = { title: { text: this.config.chartXAxisLabel }, categories: xcategories };
            this.percentageAreaChartOptions.yAxis = { title: { text: this.config.chartYAxisLabel } };
            this.percentageAreaChartOptions.series = ycategories;
            Highcharts.chart(`area${this.chartId}Container`, this.percentageAreaChartOptions);
        }


        if (this.config.basicColumnChart) {
            //this.basicColumnChartOptions.title = { text: this.config.title };
            //this.basicColumnChartOptions.subtitle = { text: this.config.subtitle };
            this.basicColumnChartOptions.xAxis = { title: { text: this.config.chartXAxisLabel }, categories: xcategories };
            this.basicColumnChartOptions.yAxis = { title: { text: this.config.chartYAxisLabel } };
            this.basicColumnChartOptions.series = ycategories;
            this.basicColumnChartOptions.tooltip.pointFormat = '<tr><td style="color:{series.color};padding:0">{series.name}: </td> <td style="padding:0"><b>{point.y:.1f}' + this.config.units + '</b></td></tr>';
            Highcharts.chart(`column${this.chartId}Container`, this.basicColumnChartOptions);
        }


        if (this.config.stackedPercentageColumnChart) {
            //this.stackedPercentageColumnChartOptions.title = { text: this.config.title };
            //this.stackedPercentageColumnChartOptions.subtitle = { text: this.config.subtitle };
            this.stackedPercentageColumnChartOptions.xAxis = { title: { text: this.config.chartXAxisLabel }, categories: xcategories };
            this.stackedPercentageColumnChartOptions.yAxis = { title: { text: this.config.chartYAxisLabel } };
            this.stackedPercentageColumnChartOptions.series = ycategories;
            Highcharts.chart(`stackedColumn${this.chartId}Container`, this.stackedPercentageColumnChartOptions);
        }

        this.cd.detectChanges();


    }



    getTotalReportingVariableValue(reportingVariable: number, year: number): number {
        return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
    }


    getTotalCO2EquivalentValue(year: number): number {
        return this.observations
            .filter(o => o.year == year)
            .filter(o =>
                o.reportingVariableId == this.netCarbonDioxideEmissionsRemovalsReportingVariableId ||
                o.reportingVariableId == this.methaneReportingVariableId ||
                o.reportingVariableId == this.nitrousOxideReportingVariableId)
            .reduce((accumulator, observation) => accumulator + this.getCO2Equivalent(observation.reportingVariableId, observation.amount), 0);
    }

    getCO2Equivalent(reportingVariableId: number, emission: number): number {
        if (reportingVariableId == this.methaneReportingVariableId) {
            return +(emission) * +(25);
        } else if (reportingVariableId == this.nitrousOxideReportingVariableId) {
            return +(emission) * +(298);
        } else {
            return emission;
        }
    }


    private formatDateForUI(dateString: string): string {

        let date: Date = new Date(dateString);
        let month = new Intl.DateTimeFormat('en', { month: 'short' }).format(date);
        let day = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(date);

        return `${month} ${day}`;
    }

    private formatDateForServer(dateString: string): string {

        let date: any = new Date(dateString);
        if (isNaN(date)) { return ""; }
        let year = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(date);
        let month = new Intl.DateTimeFormat('en', { month: '2-digit' }).format(date);
        let day = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(date);

        return `${year}-${month}-${day}`;
    }

    public getDateFromTimepointId(timepointId: number): string {

        let yearString = timepointId.toString();
        let output = [];

        for (var i = 0, len = yearString.length; i < len; i++) {
            output.push(yearString.charAt(i));
        }

        return output[0]
            + "" + output[1]
            + "" + output[2]
            + "" + output[3]
            + "-" + output[4]
            + "" + output[5]
            + "-" + output[6]
            + "" + output[7]


    }

    select(chart: string) {
        this.selectedChart = chart;
    }

}