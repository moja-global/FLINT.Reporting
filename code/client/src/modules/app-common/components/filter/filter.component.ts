import { Component, ChangeDetectionStrategy, AfterViewInit, Output, EventEmitter, HostListener, OnInit, OnDestroy } from "@angular/core";
import { ConfigService } from "@common/services/config.service";
import { DatabaseFilterService } from "@common/services/database-filter.service";
import { Database } from "@modules/databases/models";
import { DatabasesDataService } from "@modules/databases/services";
import { NGXLogger } from "ngx-logger";
import { BehaviorSubject, Observable, Subscription } from "rxjs";

const LOG_PREFIX: string = "[Filter Component]";

@Component({
  selector: 'sb-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './filter.component.html',
  styleUrls: ['filter.component.scss'],
})
export class FilterComponent implements OnInit, OnDestroy, AfterViewInit {

  // Database Filter Changed Emitter
  // @Output() databaseFilterChanged: EventEmitter<DatabaseFilter> = new EventEmitter<DatabaseFilter>();

  // Flag
  hasProcessedDatabase: boolean = false;

  // Allows the database selection drop down to keep tabs of the current status of databases
  private _databasesSubject$: BehaviorSubject<Array<Database>> = new BehaviorSubject<Array<Database>>([]);
  readonly databases$: Observable<Array<Database>> = this._databasesSubject$.asObservable();

  // Allows the year selection drop downs to keep tabs of the current status of years
  private _yearsSubject$: BehaviorSubject<Array<number>> = new BehaviorSubject<Array<number>>([]);
  readonly years$: Observable<Array<number>> = this._yearsSubject$.asObservable();

  // Allows the land use selection drop downs to keep tabs of the land uses in the selected database
  private _landUsesSubject$: BehaviorSubject<Array<number>> = new BehaviorSubject<Array<number>>([]);
  readonly landUses$: Observable<Array<number>> = this._landUsesSubject$.asObservable();

  // Selected Database
  selectedDatabase!: Database;

  // Selected Database Id
  selectedDatabaseId!: number | null | undefined;

  // Selected Party Id
  selectedPartyId: number | null | undefined = 48;

  // selected Land Use Id
  selectedLandUseId: number | null | undefined = -1;

  // Selected Start Year
  selectedStartYear!: number | null | undefined;

  // Selected End Year
  selectedEndYear!: number | null | undefined;


  // A common gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];


  constructor(
    private databasesDataService: DatabasesDataService,
    public configService: ConfigService,
    private databaseFilterService: DatabaseFilterService,
    private log: NGXLogger) {
  }


  ngOnInit() {
    this.log.trace(`${LOG_PREFIX} Initializing Component`);
    this._subscriptions.push(
      this.databasesDataService.databases$
        .subscribe(
          databases => {

            // The update could have been an an addition or a removal
            // So start by assuming that there are no proceesed databases
            this.hasProcessedDatabase = false;


            // Loop through the databases and check for a least one processed database
            // If found, set the processed databases flag to true
            for (let database of databases) {
              if (database.processed) {
                this.hasProcessedDatabase = true;
                break;
              }
            }

            // Sort the database in descending order
            let sorted: Database[] = databases.sort((n1, n2) => {

              if (n1.id != undefined && n1.id != null && n2.id != undefined && n2.id != null) {
                if (n1.id < n2.id) {
                  return 1;
                }

                if (n1.id > n2.id) {
                  return -1;
                }
              }

              return 0;
            });

            // Push the next databases update
            this._databasesSubject$.next(sorted);

            // Set the selected database to the latest database
            this.selectedDatabase = sorted[0];

            // Set the selected database's id to the latest database
            this.selectedDatabaseId = sorted[0].id;

            // Set the selected start year to the latest database's start year
            this.selectedStartYear = sorted[0].startYear;

            // Set the selected end year to the latest database's end year
            this.selectedEndYear = sorted[0].endYear;

            // Create the new year range
            if (this.selectedStartYear != undefined &&
              this.selectedStartYear != null &&
              this.selectedEndYear != undefined &&
              this.selectedEndYear != null) {

              let years: number[] = [];

              for (let i = this.selectedStartYear; i <= this.selectedEndYear; i++) {
                years.push(i);
              }

              this._yearsSubject$.next(years);

            } else {
              this._yearsSubject$.next([]);
            }

          },
          error => {

            // Assume the worst and set processed databases to false
            this.log.error('Could not load databases');
            this.hasProcessedDatabase = false;
            this._databasesSubject$.next([]);
            this._yearsSubject$.next([]);

          }
        ));
  }

  ngAfterViewInit() {

    this.log.trace(`${LOG_PREFIX} Post View initialization`);

    // Broadcast
    this.databaseFilterService.filter = {
      databaseId: this.selectedDatabaseId,
      partyId: this.selectedPartyId,
      landUseCategoryId: this.selectedLandUseId,
      startYear: this.selectedStartYear,
      endYear: this.selectedEndYear
    };
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  onDatabaseChange(event: any) {

    this.log.trace(`${LOG_PREFIX} Database changed`);

    // Get the selected database
    let database: Database | undefined = this._databasesSubject$.value.find(d => d.id == this.selectedDatabaseId);
    if (database != undefined) {
      // Set the selected start year to the latest database's start year
      this.selectedStartYear = database.startYear;

      // Set the selected end year to the latest database's end year
      this.selectedEndYear = database.endYear;

      // Create the new year range
      if (this.selectedStartYear != undefined &&
        this.selectedStartYear != null &&
        this.selectedEndYear != undefined &&
        this.selectedEndYear != null) {

        let years: number[] = [];

        for (let i = this.selectedStartYear; i <= this.selectedEndYear; i++) {
          years.push(i);
        }

        this._yearsSubject$.next(years);

      } else {
        this._yearsSubject$.next([]);
      }

      // Broadcast
      this.databaseFilterService.filter = {
        databaseId: this.selectedDatabaseId,
        partyId: this.selectedPartyId,
        landUseCategoryId: this.selectedLandUseId,
        startYear: this.selectedStartYear,
        endYear: this.selectedEndYear
      };

    } else {
      // Assume the worst and set processed databases to false
      this.log.error('Could not load databases');
      this.hasProcessedDatabase = false;
      this._databasesSubject$.next([]);
      this._yearsSubject$.next([]);
    }
  }


  onStartYearChange(event: any) {

    this.log.trace(`${LOG_PREFIX} Start Year changed`);

    if (this.selectedStartYear != undefined &&
      this.selectedStartYear != null &&
      this.selectedEndYear != undefined &&
      this.selectedEndYear != null) {

      if (this.selectedStartYear > this.selectedEndYear) {
        this.selectedEndYear = this.selectedStartYear;
      }

      // Broadcast
      this.databaseFilterService.filter = {
        databaseId: this.selectedDatabaseId,
        partyId: this.selectedPartyId,
        landUseCategoryId: this.selectedLandUseId,
        startYear: this.selectedStartYear,
        endYear: this.selectedEndYear
      };
    }

  }


  onEndYearChange(event: any) {

    this.log.trace(`${LOG_PREFIX} End Year changed`);

    if (this.selectedStartYear != undefined &&
      this.selectedStartYear != null &&
      this.selectedEndYear != undefined &&
      this.selectedEndYear != null) {

      if (this.selectedEndYear < this.selectedStartYear) {
        this.selectedStartYear = this.selectedEndYear;
      }

      // Broadcast
      this.databaseFilterService.filter = {
        databaseId: this.selectedDatabaseId,
        partyId: this.selectedPartyId,
        landUseCategoryId: this.selectedLandUseId,
        startYear: this.selectedStartYear,
        endYear: this.selectedEndYear
      };
    }

  }


  onLandUseChange(event: any) {

    this.log.trace(`${LOG_PREFIX} Land Use changed`);

    // Broadcast
    this.databaseFilterService.filter = {
      databaseId: this.selectedDatabaseId,
      partyId: this.selectedPartyId,
      landUseCategoryId: this.selectedLandUseId,
      startYear: this.selectedStartYear,
      endYear: this.selectedEndYear
    };

  }


  onLocationChange() {


    this.log.trace(`${LOG_PREFIX} Location changed`);

    // Broadcast
    this.databaseFilterService.filter = {
      databaseId: this.selectedDatabaseId,
      partyId: this.selectedPartyId,
      landUseCategoryId: this.selectedLandUseId,
      startYear: this.selectedStartYear,
      endYear: this.selectedEndYear
    };
  }


}
