import { Component, OnInit, ChangeDetectorRef, AfterViewInit, HostListener, Input } from '@angular/core';
import {
    Axis,
    DataType,
    Format,
    QuantityObservation,
    VisualizationAxis,
    VisualizationVariable,
    Visualization
} from '@modules/visualization/models';
import {
    QuantityObservationsDataService,
    VisualizationAxesDataService,
    VisualizationVariablesDataService
} from '@modules/visualization/services';
import { ResizedEvent } from 'angular-resize-event';
import * as Highcharts from 'highcharts';
import exporting from 'highcharts/modules/exporting';
import offline from 'highcharts/modules/offline-exporting';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Observable, Subject, Subscription } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

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


const LOG_PREFIX: string = "[Chart Component]";

@Component({
    selector: 'sb-chart',
    templateUrl: './chart.component.html',
    styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnInit, AfterViewInit {

    // Allow the parent component to specify what should be visualized
    @Input() visualization: Visualization | null = null;

    // Allow the parent component to specify in what steps it should be visualized
    @Input() steps: number = 1;

    // The total number of Visualization Variables
    totalVariables!: number;

    // Loading Animation Flag
    loading: boolean = true;

    // Series Data Loading Completion Notifier
    notifier = new Subject();

    // Keep tabs on the variables whose data has been successfully retrieved
    private _processedVariables$: BehaviorSubject<number | null> = new BehaviorSubject<number | null>(null);

    //The Chart
    chart: any = null;

    // The Chart Generation Inputs
    categories: Set<string> = new Set();
    series: any[] = [];
    data: any[] = [];
    colors: string[] = [];

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private cd: ChangeDetectorRef,
        private quantityObservationsDataService: QuantityObservationsDataService,
        private visualizationAxesDataService: VisualizationAxesDataService,
        private visualizationVariablesDataService: VisualizationVariablesDataService,
        private log: NGXLogger) { }


    ngOnInit(): void {

        // Check if the Visualization Subject has been specified
        this.log.trace(`${LOG_PREFIX}  Checking if the Visualization Subject has been specified`);

    }


    ngAfterViewInit(): void {

        if (this.visualization) {

            // The Visualization Subject has been specified
            this.log.trace(`${LOG_PREFIX} The Visualization Subject has been specified`);
            this.log.debug(`${LOG_PREFIX} Visualization Subject = ${JSON.stringify(this.visualization)}`);

            //

            // Retrieve the Visualization Subject's variables
            this.log.trace(`${LOG_PREFIX} Retrieving the Visualization Subject's Variables`);
            this.visualizationVariablesDataService.getVisualizationVariablesByVisualization$(this.visualization?.id)
                .subscribe((variables: VisualizationVariable[]) => {

                    // Visualization Subject's Variables successfully retrieved
                    this.log.trace(`${LOG_PREFIX} The Visualization Subject's Variables have been successfully retrieved`);
                    this.log.debug(`${LOG_PREFIX} Variables = ${JSON.stringify(variables)}`);

                    // Take count of the total number of variables
                    this.log.trace(`${LOG_PREFIX} Taking count of the total number of variables`);
                    this.totalVariables = variables.length;
                    this.log.debug(`${LOG_PREFIX} Variable Count = ${this.totalVariables}`);

                    // Register a chart series preparation monitor.
                    // Load the chart when the series preparation is done
                    this.monitorChartSeriesPreparation(() => this.prepareChartOptions((options) => this.loadChart(options)));

                    // Start the chart series preparation
                    this.prepareChartSeries(variables);

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
        this._subscriptions.forEach(s => s.unsubscribe());
    }

    private prepareChartSeries(variables: VisualizationVariable[]): void {

        // Go through each variable and update the chart categories / series
        this.log.trace(`${LOG_PREFIX} Going through each variable and update the chart categories / series`);
        variables.forEach((variable: VisualizationVariable) => {

            switch (this.visualization?.formatId) {

                case Format.BASIC_LINE_CHART:
                case Format.BASIC_COLUMN_CHART:
                case Format.STACKED_COLUMN_CHART:
                case Format.BASIC_BAR_CHART:
                case Format.STACKED_BAR_CHART:

                    if (this.visualization?.dataTypeId == DataType.TIME_SERIES_DATA) {

                        this.quantityObservationsDataService.getQuantityObservationsInStepsByIndicator$(variable.indicatorId, this.steps)
                            .subscribe((quantityObservations: QuantityObservation[]) => {
                                if (quantityObservations.length > 0) {

                                    // Create a temporary array to hold the amounts
                                    const amounts: number[] = [];

                                    // Loop through the quantity observations and update the categories / amounts array
                                    quantityObservations.forEach(quantityObservation => {
                                        this.categories.add(this.formatDateForUI(this.getDateFromTimepointId(quantityObservation.timepointId)));
                                        amounts.push(quantityObservation.amount);
                                    });

                                    // Update the series array
                                    if (variable.color != "") {
                                        this.series.push({ showInLegend: true, name: variable.label, data: amounts, color: variable.color });
                                    } else {
                                        this.series.push({ showInLegend: true, name: variable.label, data: amounts });
                                    }

                                    // Broadcast that the variable has been successfully processed
                                    this._processedVariables$.next(variable.id);

                                }
                            });
                    } else {

                        this.categories.add(variable.category);

                        let currentSeries: any | undefined = this.series.find(s => s.name == variable.label);

                        if (currentSeries == undefined) {

                            currentSeries = {
                                showInLegend: true,
                                name: variable.label,
                                color: variable.color,
                                data: []
                            }

                            this.series.push(currentSeries);
                        }

                        this.quantityObservationsDataService.getQuantityObservationsByIndicator$(variable.indicatorId)
                            .subscribe((quantityObservations: QuantityObservation[]) => {
                                if (quantityObservations.length > 0) {

                                    // Create a temporary array to hold the amounts
                                    const amounts: number[] = [];

                                    // Loop through the quantity observations and update the categories / amounts array
                                    quantityObservations.forEach(quantityObservation => {
                                        amounts.push(quantityObservation.amount);
                                    });

                                    // Update the series data
                                    currentSeries.data.push(amounts);

                                    // Broadcast that the variable has been successfully processed
                                    this._processedVariables$.next(variable.id);

                                }
                            });

                    }

                    break;

                case Format.PIE_CHART:
                case Format.SEMI_CIRCLE_DONUT:

                    this.quantityObservationsDataService.getQuantityObservationsByIndicator$(variable.indicatorId)
                        .subscribe((quantityObservations: QuantityObservation[]) => {

                            // Update the data
                            this.data.push([variable.label, quantityObservations[0].amount]);
                            this.colors.push(variable.color);

                            // Broadcast that the indicator has been successfully processed
                            this._processedVariables$.next(variable.id);
                        });

                    break;
            }

        });

    

    }

    private monitorChartSeriesPreparation(callback: () => void): void {

        this.log.trace(`${LOG_PREFIX} Monitoring Chart Serie Preparation`);

        let processedVariables: number = 0;

        this._processedVariables$
            .pipe(takeUntil(this.notifier))
            .subscribe((id: number | null) => {
                if (id != null) {

                    // Increment the number of processed variables
                    processedVariables += 1;

                    // Check if the processed variables is equal to the total number of variable
                    if (processedVariables == this.totalVariables) {

                        // The processed variables is equal to the total number of variable
                        // Stop monitoring the processing activity
                        this.notifier.next(true);
                        this.notifier.complete();

                        // Call the callback function
                        callback();
                    }
                }
            });
    }


    private prepareChartOptions(callback: (chartOptions: any) => void): void {

        this.log.trace(`${LOG_PREFIX} Preparing Chart Options`);

        switch (this.visualization?.formatId) {

            case Format.BASIC_LINE_CHART:
            case Format.BASIC_COLUMN_CHART:
            case Format.STACKED_COLUMN_CHART:
            case Format.BASIC_BAR_CHART:
            case Format.STACKED_BAR_CHART:

                // Get the labels of the chart axes
                this.visualizationAxesDataService.getVisualizationAxesByVisualization$(this.visualization?.id)
                    .subscribe(axes => {

                        const xAxisLabel: VisualizationAxis | undefined = axes.find(axis => axis.axisId == Axis.X_AXIS);
                        const yAxisLabel: VisualizationAxis | undefined = axes.find(axis => axis.axisId == Axis.Y_AXIS);


                        // Initialize the chart options
                        switch (this.visualization?.formatId) {

                            case Format.BASIC_LINE_CHART:
                                callback(this.getLineChartOptions(this.categories, this.series, xAxisLabel, yAxisLabel));
                                break;

                            case Format.BASIC_COLUMN_CHART:
                            case Format.STACKED_COLUMN_CHART:
                                callback(this.getColumnChartOptions(this.categories, this.series, xAxisLabel, yAxisLabel));
                                break;

                            case Format.BASIC_BAR_CHART:
                            case Format.STACKED_BAR_CHART:
                                callback(this.getBarChartOptions(this.categories, this.series, xAxisLabel, yAxisLabel));
                                break;
                        }

                    });

                break;

            case Format.PIE_CHART:
                callback(this.getPieChartOptions(this.visualization?.name, this.data, this.colors));
                break;

            case Format.SEMI_CIRCLE_DONUT:
                callback(this.getSemiCircleDonutChartOptions(this.visualization?.name, this.data, this.colors));
                break;

            default:
                callback({});
                return;

        }

    }

    private loadChart(chartOptions: any): void {

        this.log.trace(`${LOG_PREFIX} Loading Chart`);
        this.log.debug(`${LOG_PREFIX} Chart Options = ${JSON.stringify(chartOptions)}`);
        this.chart = Highcharts.chart(`chart${this.visualization?.id}`, chartOptions);

        this.cd.detectChanges();

    }


    private getLineChartOptions(
        categories: Set<string>,
        series: any[],
        xAxisLabel: VisualizationAxis | undefined,
        yAxisLabel: VisualizationAxis | undefined): any {

        return {
            chart: {
                type: 'line'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories: Array.from(categories),
                title: {
                    text: xAxisLabel == undefined ? '' : xAxisLabel.label
                },
                crosshair: true
            },
            yAxis: {
                title: {
                    text: yAxisLabel == undefined ? '' : yAxisLabel.label
                }
            },
            plotOptions: {
                series: {
                    marker: {
                        enabled: false
                    }
                }
            },
            series: series,
            credits: {
                enabled: false
            }
        }
    }


    private getColumnChartOptions(
        categories: Set<string>,
        series: any[],
        xAxisLabel: VisualizationAxis | undefined,
        yAxisLabel: VisualizationAxis | undefined): any {

        return {
            chart: {
                type: 'column'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories: Array.from(categories),
                title: {
                    text: xAxisLabel == undefined ? '' : xAxisLabel.label
                },
                crosshair: true
            },
            yAxis: {
                title: {
                    text: yAxisLabel == undefined ? '' : yAxisLabel.label
                }
            },
            series: series,
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: false
                    }
                }
            },
            credits: {
                enabled: false
            }
        }
    }



    private getBarChartOptions(
        categories: Set<string>,
        series: any[],
        xAxisLabel: VisualizationAxis | undefined,
        yAxisLabel: VisualizationAxis | undefined): any {

        return {
            chart: {
                type: 'bar'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories: Array.from(categories),
                title: {
                    text: xAxisLabel == undefined ? '' : xAxisLabel.label
                },
                crosshair: true
            },
            yAxis: {
                title: {
                    text: yAxisLabel == undefined ? '' : yAxisLabel.label
                }
            },
            series: series,
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: false
                    }
                }
            },
            credits: {
                enabled: false
            }
        }
    }



    private getPieChartOptions(
        title: string,
        data: any[],
        colors: string[]): any {

        return {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: 0,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            accessibility: {
                point: {
                    valueSuffix: '%'
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                    }
                }
            },
            series: [{
                name: title,
                colorByPoint: true,
                data: data
            }],
            credits: {
                enabled: false
            }
        }
    }



    private getSemiCircleDonutChartOptions(
        title: string,
        data: any[],
        colors: string[]): any {

        return {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: 0,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            accessibility: {
                point: {
                    valueSuffix: '%'
                }
            },
            plotOptions: {
                pie: {
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        distance: -50,
                        style: {
                            fontWeight: 'normal',
                            color: 'black'
                        }
                    },
                    startAngle: -90,
                    endAngle: 90,
                    center: ['50%', '75%'],
                    size: '110%',
                    colors: colors
                }
            },
            series: [{
                type: 'pie',
                name: title,
                innerSize: '50%',
                data: data
            }],
            credits: {
                enabled: false
            }
        }
    }

    getChartId(): string {
        return 'chart-' + this.visualization?.id;
    }


    onResized(event: ResizedEvent) {
        if (this.chart != null) {
            this.chart.reflow();
        }
    }

    private formatDateForUI(dateString: string): string {

        let date: Date = new Date(dateString);
        let month = new Intl.DateTimeFormat('en', { month: 'short' }).format(date);
        let day = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(date);

        return `${month} ${day}`;
    }

    private getDateFromTimepointId(timepointId: number): string {

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

}