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

const LOG_PREFIX: string = "[Forestland-CRF-Table-Component]";

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

    @Input() sector!: number;

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
    otherLandConvertedToWetlandsId:number = 32    


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


    constructor(
        public quantityObservationsTableService: QuantityObservationsRecordsFilterService,
        private cd: ChangeDetectorRef,
        public connectivityStatusService: ConnectivityStatusService,
        private log: NGXLogger) {
    }

    ngOnInit() {




    }


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

                },
                error => {
                    this.log.trace(`${LOG_PREFIX} Could not load quantity observations`);
                    this.observations = [];

                }));

        // Subscribe to quantity observations data notifications.
        this.log.trace(`${LOG_PREFIX} Subscribing to quantity observations data notifications`);
        this._subscriptions.push(
            this.quantityObservationsTableService.quantityObservations$.subscribe(
                data => {
                    this.observations = data;
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

    getTotalLandTitle(): string{
        if(this.sector == 1) {
            return "Total Forest land";
        } else if(this.sector == 2) {
            return "Total Cropland";
        } else if(this.sector == 3) {
            return "Total Grassland";
        } else if(this.sector == 4) {
            return "Total Wetland";
        } else {
            return "";
        }
    }

    getRemainingLandTitle(): string{
        if(this.sector == 1) {
            return "Forest land Remaining Forest land";
        } else if(this.sector == 2) {
            return "Cropland Remaining Cropland";
        } else if(this.sector == 3) {
            return "Grassland Remaining Grassland";
        } else if(this.sector == 4) {
            return "Wetland Remaining Wetland";
        } else {
            return "";
        }
    }  
    
    
    getConvertedLandTitle(): string{
        if(this.sector == 1) {
            return "Land Converted To Forest land";
        } else if(this.sector == 2) {
            return "Land Converted To Cropland";
        } else if(this.sector == 3) {
            return "Land Converted To Grassland";
        } else if(this.sector == 4) {
            return "Land Converted To Wetland";
        } else {
            return "";
        }
    }     


    getTotalLandReportingVariableValue(reportingVariable: number, year: number): number {
        if(this.sector == 1) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o =>
                o.landUseCategoryId == this.forestLandRemainingForestLandId ||
                o.landUseCategoryId == this.croplandConvertedToForestLandId ||
                o.landUseCategoryId == this.grasslandConvertedToForestLandId ||
                o.landUseCategoryId == this.wetlandsConvertedToForestLandId ||
                o.landUseCategoryId == this.settlementsConvertedToForestLandId ||
                o.landUseCategoryId == this.otherLandConvertedToForestLandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else if(this.sector == 2) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o =>
                o.landUseCategoryId == this.croplandRemainingCroplandId ||
                o.landUseCategoryId == this.forestLandConvertedToCroplandId ||
                o.landUseCategoryId == this.grasslandConvertedToCroplandId ||
                o.landUseCategoryId == this.wetlandsConvertedToCroplandId ||
                o.landUseCategoryId == this.settlementsConvertedToCroplandId ||
                o.landUseCategoryId == this.otherLandConvertedToCroplandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0); 
        }else if(this.sector == 3) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o =>
                o.landUseCategoryId == this.grasslandRemainingGrasslandId ||
                o.landUseCategoryId == this.croplandConvertedToGrasslandId ||
                o.landUseCategoryId == this.forestLandConvertedToGrasslandId ||
                o.landUseCategoryId == this.wetlandsConvertedToGrasslandId ||
                o.landUseCategoryId == this.settlementsConvertedToGrasslandId ||
                o.landUseCategoryId == this.otherLandConvertedToGrasslandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else if(this.sector == 4) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o =>
                o.landUseCategoryId == this.wetlandsRemainingWetlandsId ||
                o.landUseCategoryId == this.croplandConvertedToWetlandsId ||
                o.landUseCategoryId == this.grasslandConvertedToWetlandsId ||
                o.landUseCategoryId == this.forestLandConvertedToWetlandsId ||
                o.landUseCategoryId == this.settlementsConvertedToWetlandsId ||
                o.landUseCategoryId == this.otherLandConvertedToWetlandsId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else {
            return 0;
        }

    }


    getRemainingLandReportingVariableValue(reportingVariable: number, year: number): number {
        if(this.sector == 1) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o => o.landUseCategoryId == this.forestLandRemainingForestLandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else if(this.sector == 2) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o => o.landUseCategoryId == this.croplandRemainingCroplandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else if(this.sector == 3) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o => o.landUseCategoryId == this.grasslandRemainingGrasslandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else if(this.sector == 4) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o => o.landUseCategoryId == this.wetlandsRemainingWetlandsId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else {
            return 0;
        }        

    }


    getConvertedLandReportingVariableValue(reportingVariable: number, year: number): number {
        if(this.sector == 1) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o =>
                o.landUseCategoryId == this.croplandConvertedToForestLandId ||
                o.landUseCategoryId == this.grasslandConvertedToForestLandId ||
                o.landUseCategoryId == this.wetlandsConvertedToForestLandId ||
                o.landUseCategoryId == this.settlementsConvertedToForestLandId ||
                o.landUseCategoryId == this.otherLandConvertedToForestLandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else if(this.sector == 2) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o =>
                o.landUseCategoryId == this.forestLandConvertedToCroplandId ||
                o.landUseCategoryId == this.grasslandConvertedToCroplandId ||
                o.landUseCategoryId == this.wetlandsConvertedToCroplandId ||
                o.landUseCategoryId == this.settlementsConvertedToCroplandId ||
                o.landUseCategoryId == this.otherLandConvertedToCroplandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0); 
        }else if(this.sector == 3) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o =>
                o.landUseCategoryId == this.croplandConvertedToGrasslandId ||
                o.landUseCategoryId == this.forestLandConvertedToGrasslandId ||
                o.landUseCategoryId == this.wetlandsConvertedToGrasslandId ||
                o.landUseCategoryId == this.settlementsConvertedToGrasslandId ||
                o.landUseCategoryId == this.otherLandConvertedToGrasslandId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else if(this.sector == 4) {
            return this.observations
            .filter(o => o.year == year)
            .filter(o => o.reportingVariableId == reportingVariable)
            .filter(o =>
                o.landUseCategoryId == this.croplandConvertedToWetlandsId ||
                o.landUseCategoryId == this.grasslandConvertedToWetlandsId ||
                o.landUseCategoryId == this.forestLandConvertedToWetlandsId ||
                o.landUseCategoryId == this.settlementsConvertedToWetlandsId ||
                o.landUseCategoryId == this.otherLandConvertedToWetlandsId)
            .reduce((accumulator, observation) => accumulator + observation.amount, 0);
        }else {
            return 0;
        }
    }


}
