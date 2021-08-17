import { Component, OnInit, ChangeDetectorRef, AfterViewInit, HostListener } from '@angular/core';
import * as Highcharts from 'highcharts';
import { NGXLogger } from 'ngx-logger';
import exporting from 'highcharts/modules/exporting';
import offline from 'highcharts/modules/offline-exporting';
import { QuantityObservation } from '@modules/quantity-observations/models/quantity-observation.model';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { ConfigService } from '@common/services/config.service';
import { Serie } from '@common/models/serie';
import { QuantityObservationsRecordsFilterService } from '@modules/quantity-observations/services/quantity-observations-records-filter.service';
import { DatabaseFilterService } from '@common/services/database-filter.service';
import { DatabaseFilter } from '@common/models/database-filter.model';

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

const LOG_PREFIX: string = "[Total-Emissions-Chart-Component]";

@Component({
    selector: 'sb-total-ghg-emissions-chart',
    templateUrl: './total-ghg-emissions-chart.component.html',
    styleUrls: ['./total-ghg-emissions-chart.component.scss']
})
export class TotalGHGEmissionsChartComponent implements OnInit, AfterViewInit {

    // Loading Animation Flag
    loading: boolean = true;

    // Allows the year selection drop downs to keep tabs of the current status of years
    private _yearsSubject$: BehaviorSubject<Array<number>> = new BehaviorSubject<Array<number>>([]);
    readonly years$: Observable<Array<number>> = this._yearsSubject$.asObservable();

    // A container the Quantity Observations to be rendered in the chart
    observations: QuantityObservation[] = [];

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    //Chart Configurations
    private basicColumnChartOptions: any = {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Total GHG Emissions'
        },
        subtitle: {
            text: 'source: moja.global'
        },
        xAxis: {
            categories: [],
            title: {
                text: 'Years'
            },            
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Emissions (kt CO2-eq)'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f}kt CO2-eq</b></td></tr>',
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


    constructor(
        private configService: ConfigService,
        public quantityObservationsTableService: QuantityObservationsRecordsFilterService,
        private databaseFilterService: DatabaseFilterService,
        private cd: ChangeDetectorRef,
        private log: NGXLogger) {

    }

    ngOnInit(): void {
    }

    ngAfterViewInit(): void {

        // Subscribe to quantity observations data notifications.
        this.log.trace(`${LOG_PREFIX} Subscribing to quantity observations data notifications`);
        this._subscriptions.push(
            this.quantityObservationsTableService.quantityObservations$.subscribe(
                data => {

                    // Update the local quantity observations
                    this.log.trace(`${LOG_PREFIX} Updating the local Quantity Observations`);
                    this.observations = data;
                    this.log.debug(`${LOG_PREFIX} Quantity Observations = ${JSON.stringify(data)}`);

                    // Get the current database filter
                    this.log.trace(`${LOG_PREFIX} Getting the current Database Filter`);
                    let databaseFilter: DatabaseFilter = this.databaseFilterService.filter;
                    this.log.debug(`${LOG_PREFIX} Database Filter = ${JSON.stringify(databaseFilter)}`);

                    // Get the start year from the database filter
                    this.log.trace(`${LOG_PREFIX} Getting the Start Year from the current database filter`);
                    let startYear: number = databaseFilter.startYear == undefined || databaseFilter.startYear == null ? -1 : databaseFilter.startYear;
                    this.log.debug(`${LOG_PREFIX} Start Year = ${JSON.stringify(startYear)}`);

                    // Get the end year from the database filter
                    this.log.trace(`${LOG_PREFIX} Getting the End Year from the current database filter`);
                    let endYear: number = databaseFilter.endYear == undefined || databaseFilter.endYear == null ? -1 : databaseFilter.endYear;
                    this.log.debug(`${LOG_PREFIX} End Year = ${JSON.stringify(endYear)}`);

                    // Update the year range
                    this.log.trace(`${LOG_PREFIX} Updating the year range`);
                    let years: number[] = [];
                    if (startYear != -1 && endYear != -1) {
                        for (let i = startYear; i <= endYear; i++) {
                            years.push(i);
                        }
                        this._yearsSubject$.next(years);
                    } else {
                        this._yearsSubject$.next(years);
                    }
                    this.log.debug(`${LOG_PREFIX} Years = ${JSON.stringify(years)}`);

                    // Load or reload the chart
                    this.log.trace(`${LOG_PREFIX}  Loading or reloading the  chart`);
                    this.loadChart();

                    this.cd.markForCheck();


                },
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
                }));

                this.cd.detectChanges();

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach(s => s.unsubscribe());
    }


    private loadChart(): void {

        this.log.trace(`${LOG_PREFIX} Setting loading status to true`);
        this.loading = false;        

        let xcategories: number[] = [];
        let ycategories: Serie[] = [];

        this._yearsSubject$.value.forEach(year => {

            // Set the year on the x axis
            xcategories.push(year);

            // Set the emission value on the y axis
            let a = ycategories.find(s => s.name === "Total Emissions (kt CO2-eq)");

            if (typeof a == 'undefined') {
                a = { showInLegend: true, name: "Total Emissions (kt CO2-eq)", data: [this.getTotalCO2EquivalentValue(year)], color: "#A0E7E5" };
                ycategories.push(a);
            } else {
                a.data.push(this.getTotalCO2EquivalentValue(year));
            }
        });

        this.log.debug(`${LOG_PREFIX} x axis: ${JSON.stringify(xcategories)}`);
        this.log.debug(`${LOG_PREFIX} y axis: ${JSON.stringify(ycategories)}`);



        // Prepare and load the required graphs
        this.basicColumnChartOptions.xAxis.categories = xcategories;
        this.basicColumnChartOptions.series = ycategories;

        // Set the loading status to false
        this.log.trace(`${LOG_PREFIX} Setting loading status to false`);
        this.loading = false;

        // Load the chart
        Highcharts.chart("container", this.basicColumnChartOptions);

        
        


    }


    getTotalCO2EquivalentValue(year: number): number {
        return this.observations
            .filter(o => o.year == year)
            .filter(o =>
                o.reportingVariableId == this.configService.netCarbonDioxideEmissionsRemovalsReportingVariableId ||
                o.reportingVariableId == this.configService.methaneReportingVariableId ||
                o.reportingVariableId == this.configService.nitrousOxideReportingVariableId)
            .map(o => this.getCO2Equivalent(o.reportingVariableId, o.amount))
            .reduce((accumulator, observation) => accumulator + observation, 0);
    }

    getCO2Equivalent(reportingVariableId: number, emission: number): number {

        if (reportingVariableId == this.configService.methaneReportingVariableId) {
            return emission * +25;
        } else if (reportingVariableId == this.configService.nitrousOxideReportingVariableId) {
            return emission * +298;
        } else {
            return emission;
        }
    } 


}