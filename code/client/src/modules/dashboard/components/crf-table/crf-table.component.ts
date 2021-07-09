import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    HostListener,
    Input,
    OnInit,
    ViewChild
} from '@angular/core';
import { LoadingAnimationComponent, PaginationComponent } from '@common/components';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { ConnectivityStatusService } from '@common/services/connectivity-status.service';
import { QuantityObservationsRecordsFilterService } from '@modules/quantity-observations/services/quantity-observations-records-filter.service';
import { QuantityObservation } from '@modules/quantity-observations/models/quantity-observation.model';
import { ConfigService } from '@common/services/config.service';
import { DatabaseFilterService } from '@common/services/database-filter.service';
import { DatabaseFilter } from '@common/models/database-filter.model';

const LOG_PREFIX: string = "[CRF-Table-Component]";

@Component({
    selector: 'sb-crf-table',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './crf-table.component.html',
    styleUrls: ['crf-table.component.scss'],
})
export class CrfTableComponent implements OnInit, AfterViewInit {

    // Inject a reference to the loading animation component. 
    // This will provide a way of informing it of the status of 
    // the processing events happening in the background.
    @ViewChild(LoadingAnimationComponent) animation!: LoadingAnimationComponent;

    // Allows the year selection drop downs to keep tabs of the current status of years
    private _yearsSubject$: BehaviorSubject<Array<number>> = new BehaviorSubject<Array<number>>([]);
    readonly years$: Observable<Array<number>> = this._yearsSubject$.asObservable();

    // Allows the parent component to specify the sector for which the table should be rendered
    @Input() sector!: number;

    // A container for all the Quantity Observations associated with the currently selected sector
    observations: QuantityObservation[] = [];

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];


    constructor(
        public quantityObservationsTableService: QuantityObservationsRecordsFilterService,
        private cd: ChangeDetectorRef,
        public connectivityStatusService: ConnectivityStatusService,
        public configService: ConfigService,
        private databaseFilterService: DatabaseFilterService,
        private log: NGXLogger) {
    }

    ngOnInit() {}


    ngAfterViewInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);
        // Subscribe to loading events and propagate them to the loading component.
        // Loading events occur when the user changes the filter status
        this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
        this._subscriptions.push(
            this.quantityObservationsTableService.loading$.subscribe(
                (loading) => {
                    //this.animation.loading = loading;
                }));


        // Subscribe to quantity observations data notifications.
        this.log.trace(`${LOG_PREFIX} Subscribing to quantity observations data notifications`);
        this._subscriptions.push(
            this.quantityObservationsTableService.quantityObservations$.subscribe(
                data => {

                    // Quantity Observations changed.                  
                    this.observations = data;

                    // Get the current database filter
                    let databaseFilter: DatabaseFilter = this.databaseFilterService.filter;

                    // Get the start year from the database filter
                    let startYear: number = databaseFilter.startYear == undefined || databaseFilter.startYear == null ? -1 : databaseFilter.startYear;

                    // Get the end year from the database filter
                    let endYear: number = databaseFilter.endYear == undefined || databaseFilter.endYear == null ? -1 : databaseFilter.endYear;

                    // Update the year range
                    if (startYear != -1 && endYear != -1) {
                        let years: number[] = [];
                        for (let i = startYear; i <= endYear; i++) {
                            years.push(i);
                        }
                        this._yearsSubject$.next(years);
                    } else {
                        this._yearsSubject$.next([]);
                    }                    
                },
                error => {
                    this.log.trace(`${LOG_PREFIX} Could not load quantity observations`);
                    this.observations = [];
                }));

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {

        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach(s => s.unsubscribe());
    }

    getTotalLandTitle(): string {
        if (this.sector == 1) {
            return "Total Forest land";
        } else if (this.sector == 2) {
            return "Total Cropland";
        } else if (this.sector == 3) {
            return "Total Grassland";
        } else if (this.sector == 4) {
            return "Total Wetland";
        } else {
            return "";
        }
    }

    getRemainingLandTitle(): string {
        if (this.sector == 1) {
            return "Forest land Remaining Forest land";
        } else if (this.sector == 2) {
            return "Cropland Remaining Cropland";
        } else if (this.sector == 3) {
            return "Grassland Remaining Grassland";
        } else if (this.sector == 4) {
            return "Wetland Remaining Wetland";
        } else {
            return "";
        }
    }


    getConvertedLandTitle(): string {
        if (this.sector == 1) {
            return "Land Converted To Forest land";
        } else if (this.sector == 2) {
            return "Land Converted To Cropland";
        } else if (this.sector == 3) {
            return "Land Converted To Grassland";
        } else if (this.sector == 4) {
            return "Land Converted To Wetland";
        } else {
            return "";
        }
    }


    getTotalLandReportingVariableValue(reportingVariable: number, year: number): number {
        if (this.sector == 1) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o =>
                    o.landUseCategoryId == this.configService.forestLandRemainingForestLandId ||
                    o.landUseCategoryId == this.configService.croplandConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.grasslandConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.wetlandsConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToForestLandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 2) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o =>
                    o.landUseCategoryId == this.configService.croplandRemainingCroplandId ||
                    o.landUseCategoryId == this.configService.forestLandConvertedToCroplandId ||
                    o.landUseCategoryId == this.configService.grasslandConvertedToCroplandId ||
                    o.landUseCategoryId == this.configService.wetlandsConvertedToCroplandId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToCroplandId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToCroplandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 3) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o =>
                    o.landUseCategoryId == this.configService.grasslandRemainingGrasslandId ||
                    o.landUseCategoryId == this.configService.croplandConvertedToGrasslandId ||
                    o.landUseCategoryId == this.configService.forestLandConvertedToGrasslandId ||
                    o.landUseCategoryId == this.configService.wetlandsConvertedToGrasslandId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToGrasslandId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToGrasslandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 4) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o =>
                    o.landUseCategoryId == this.configService.wetlandsRemainingWetlandsId ||
                    o.landUseCategoryId == this.configService.croplandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.grasslandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.forestLandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToWetlandsId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else {
            return 0;
        }

    }


    getRemainingLandReportingVariableValue(reportingVariable: number, year: number): number {
        if (this.sector == 1) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o => o.landUseCategoryId == this.configService.forestLandRemainingForestLandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 2) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o => o.landUseCategoryId == this.configService.croplandRemainingCroplandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 3) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o => o.landUseCategoryId == this.configService.grasslandRemainingGrasslandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 4) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o => o.landUseCategoryId == this.configService.wetlandsRemainingWetlandsId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else {
            return 0;
        }

    }


    getConvertedLandReportingVariableValue(reportingVariable: number, year: number): number {
        if (this.sector == 1) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o =>
                    o.landUseCategoryId == this.configService.croplandConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.grasslandConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.wetlandsConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToForestLandId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToForestLandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 2) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o =>
                    o.landUseCategoryId == this.configService.forestLandConvertedToCroplandId ||
                    o.landUseCategoryId == this.configService.grasslandConvertedToCroplandId ||
                    o.landUseCategoryId == this.configService.wetlandsConvertedToCroplandId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToCroplandId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToCroplandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 3) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o =>
                    o.landUseCategoryId == this.configService.croplandConvertedToGrasslandId ||
                    o.landUseCategoryId == this.configService.forestLandConvertedToGrasslandId ||
                    o.landUseCategoryId == this.configService.wetlandsConvertedToGrasslandId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToGrasslandId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToGrasslandId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else if (this.sector == 4) {
            return this.observations
                .filter(o => o.year == year)
                .filter(o => o.reportingVariableId == reportingVariable)
                .filter(o =>
                    o.landUseCategoryId == this.configService.croplandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.grasslandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.forestLandConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.settlementsConvertedToWetlandsId ||
                    o.landUseCategoryId == this.configService.otherLandConvertedToWetlandsId)
                .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        } else {
            return 0;
        }
    }


}
