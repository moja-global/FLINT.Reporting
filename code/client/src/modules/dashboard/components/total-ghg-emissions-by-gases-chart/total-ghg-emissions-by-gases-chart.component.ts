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

const LOG_PREFIX: string = "[Total-GHG-Emissions-By-Gases-Chart-Component]";

@Component({
    selector: 'sb-total-ghg-emissions-by-gases-chart',
    templateUrl: './total-ghg-emissions-by-gases-chart.component.html',
    styleUrls: ['./total-ghg-emissions-by-gases-chart.component.scss']
})
export class TotalGHGEmissionsByGasesChartComponent implements OnInit, AfterViewInit {

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
            text: 'Total GHG Emissions - By Gases'
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
                '<td style="padding:0"><b>{point.y:.1f}kt CO2</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
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
                    this.cd.detectChanges();
                }));

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
            


            let a = ycategories.find(s => s.name === "CO2");

            if (typeof a == 'undefined') {
                a = {
                    showInLegend: true, name: "CO2",
                    data: [this.getTotalReportingVariableValue(this.configService.netCarbonDioxideEmissionsRemovalsReportingVariableId, year)]
                };
                ycategories.push(a);
            } else {
                a.data.push(this.getTotalReportingVariableValue(this.configService.netCarbonDioxideEmissionsRemovalsReportingVariableId, year));
            }



            let b = ycategories.find(s => s.name === "CH4");

            if (typeof b == 'undefined') {
                b = {
                    showInLegend: true, name: "CH4",
                    data: [this.getTotalReportingVariableValue(this.configService.methaneReportingVariableId, year)]
                };
                ycategories.push(b);
            } else {
                b.data.push(this.getTotalReportingVariableValue(this.configService.methaneReportingVariableId, year));
            }


            let c = ycategories.find(s => s.name === "N2O");

            if (typeof c == 'undefined') {
                c = {
                    showInLegend: true, name: "N2O",
                    data: [this.getTotalReportingVariableValue(this.configService.nitrousOxideReportingVariableId, year)]
                };
                ycategories.push(c);
            } else {
                c.data.push(this.getTotalReportingVariableValue(this.configService.nitrousOxideReportingVariableId, year));
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

        
        this.cd.detectChanges();


    }


    getTotalReportingVariableValue(reportingVariable: number, year: number): number {
        return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .reduce((accumulator, observation) => accumulator + this.getCO2Equivalent(reportingVariable, observation.amount), 0);
    }



    getCO2Equivalent(reportingVariableId: number, emission: number): number {
        if (reportingVariableId == this.configService.methaneReportingVariableId) {
            return +(emission) * +(25);
        } else if (reportingVariableId == this.configService.nitrousOxideReportingVariableId) {
            return +(emission) * +(298);
        }else if(
            reportingVariableId == this.configService.netCarbonStockChangeInLivingBiomassReportingVariableId || 
            reportingVariableId == this.configService.netCarbonStockChangeInDeadOrganicMatterReportingVariableId || 
            reportingVariableId == this.configService.netCarbonStockChangeInMineralSoilsReportingVariableId || 
            reportingVariableId == this.configService.netCarbonStockChangeInOrganicSoilsReportingVariableId) {
            return +(emission) * -(3.666667);
        } else {
            return emission;
        }
    }    


}