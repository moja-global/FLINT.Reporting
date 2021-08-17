import { Component, ChangeDetectionStrategy, AfterViewInit, Output, EventEmitter, HostListener, OnInit, OnDestroy, ChangeDetectorRef } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { PARTIES, DEFAULT_PARTY_ID } from "@common/data";
import { ConfigService } from "@common/services/config.service";
import { DatabaseFilterService } from "@common/services/database-filter.service";
import { Database } from "@modules/databases/models";
import { DatabasesDataService } from "@modules/databases/services";
import { Party } from "@modules/parties/models/party.model";
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

  // Flag
  hasProcessedDatabase: boolean = false;

  // Allows the database selection drop down to keep tabs of the current status of databases
  private _databasesSubject$: BehaviorSubject<Array<Database>> = new BehaviorSubject<Array<Database>>([]);
  readonly databases$: Observable<Array<Database>> = this._databasesSubject$.asObservable();

  // Allows the database selection drop down to keep tabs of the current status of parties
  private _partiesSubject$: BehaviorSubject<Array<Party>> = new BehaviorSubject<Array<Party>>([]);
  readonly parties$: Observable<Array<Party>> = this._partiesSubject$.asObservable();     

  // Allows the year selection drop downs to keep tabs of the current status of years
  private _yearsSubject$: BehaviorSubject<Array<number>> = new BehaviorSubject<Array<number>>([]);
  readonly years$: Observable<Array<number>> = this._yearsSubject$.asObservable();

  // Allows the land use selection drop downs to keep tabs of the land uses in the selected database
  private _landUsesSubject$: BehaviorSubject<Array<number>> = new BehaviorSubject<Array<number>>([]);
  readonly landUses$: Observable<Array<number>> = this._landUsesSubject$.asObservable();

  // Selected Database
  selectedDatabase!: Database;

  // Selected Database Id
  selectedDatabaseId: number = -1;

  // Selected Party Id
  selectedPartyId: number = DEFAULT_PARTY_ID;

  // selected Land Use Id
  selectedLandUseId: number = -1;

  // Selected Start Year
  selectedStartYear: number = -1;

  // Selected End Year
  selectedEndYear: number = -1;

  // Allows the handling of the filter's select options as a reactive form
  filterForm!: FormGroup;

  // A common gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];


  constructor(
    private databasesDataService: DatabasesDataService,
    public configService: ConfigService,
    private databaseFilterService: DatabaseFilterService,
    private cd: ChangeDetectorRef,
    private fb: FormBuilder,
    private log: NGXLogger) {
  }


  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

        // Parties
        this._partiesSubject$.next(PARTIES);    


    this.filterForm = this.fb.group({
      databaseId: [-1],
      landUseCategoryId: [-1],
      startYear: [-1],
      endYear: [-1],
      partyId: DEFAULT_PARTY_ID
    });

    // Databases
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

            if(sorted.length > 0) {

            // Set the selected database to the latest database
            this.selectedDatabase = sorted[0];

            // Set the selected database's id to the latest database
            this.selectedDatabaseId = sorted[0].id;
            
            // Set the selected start year to the latest database's start year
            this.selectedStartYear = sorted[0].startYear;

            // Set the selected end year to the latest database's end year
            this.selectedEndYear = sorted[0].endYear;
            


            // Create the new year range
            let years: number[] = [];
            for (let i = this.selectedStartYear; i <= this.selectedEndYear; i++) {
              years.push(i);
            }
            this._yearsSubject$.next(years);

            // Update the default selected values
            this.filterForm.get("databaseId")?.patchValue(sorted[0].id);
            this.filterForm.get("startYear")?.patchValue(sorted[0].startYear);
            this.filterForm.get("endYear")?.patchValue(sorted[0].endYear);


          }

          this.onChange();

          },
          error => {

            // Assume the worst and set processed databases to false
            this.log.error(`${LOG_PREFIX} 'Could not load databases`);
            this.hasProcessedDatabase = false;
            this._databasesSubject$.next([]);
            this._yearsSubject$.next([]);

          }
        ));



        
  }

  ngAfterViewInit() {

    this.log.trace(`${LOG_PREFIX} Post View initialization`);

    // Call the global change management method
    this.log.trace(`${LOG_PREFIX} Calling the global change management method`);
    
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  onDatabaseChange(event: any) {

    this.log.trace(`${LOG_PREFIX} Database change event received`);

    // Get the id of the selected database
    this.log.trace(`${LOG_PREFIX} Getting the id of the selected database`);
    const id: number = this.filterForm.get("databaseId")?.value;
    this.log.debug(`${LOG_PREFIX} Database = ${id}`);

    // Check if the selected database id is indeed different from the locally saved database id
    this.log.trace(`${LOG_PREFIX} Checking if the selected database id is indeed different from the locally saved database id`);

    if (id != this.selectedDatabaseId) {

      // The selected database id is different from the locally saved database id
      this.log.trace(`${LOG_PREFIX} The selected database id is different from the locally saved database id`);

      // Update the locally saved database id
      this.log.trace(`${LOG_PREFIX} Updating the locally saved database id`);
      this.selectedDatabaseId = id;

      // Get the record of the database with the given id
      this.log.trace("Getting the record of the database with the given id");
      let database: Database | undefined = this._databasesSubject$.value.find(d => d.id == this.selectedDatabaseId);

      // Check if the record exists
      this.log.trace("Checking if the database record exists");

      if (database != undefined) {

        // The record exists
        this.log.trace("The database record exists");

        // Set the form's / local variable's start year to the database's start year
        this.log.trace(`${LOG_PREFIX} Setting the form's / local variable's start year to the database's start year`);
        this.filterForm.get("startYear")?.patchValue(database.startYear);
        this.selectedStartYear = database.startYear;


        // Set the form's / local variable's end year to the database's end year
        this.log.trace(`${LOG_PREFIX} Setting the form's / local variable's end year to the database's end year`);
        this.filterForm.get("endYear")?.patchValue(database.endYear);
        this.selectedEndYear = database.endYear;

        // Create a new year range
        this.log.trace(`${LOG_PREFIX} Creating a new year range`);
        let years: number[] = [];
        for (let i = database.startYear; i <= database.endYear; i++) {
          years.push(i);
        }

        // Broadcast the new year range
        this.log.trace(`${LOG_PREFIX} Broadcast the new year range`);
        this._yearsSubject$.next(years);

        // Call the global change management method
        this.log.trace(`${LOG_PREFIX} Calling the global change management method`);
        this.onChange();


      } else {

        // The record does not exist
        this.log.warn(`${LOG_PREFIX} The database record does not exist`);

        // Assume the worst and set 'has processed databases' to false
        this.log.warn(`${LOG_PREFIX}Assuming the worst and setting 'has processed databases' to false`);
        this.hasProcessedDatabase = false;

        // Assume the worst and clear the databases array
        this.log.warn(`${LOG_PREFIX}Assuming the worst and clearing the databases data`);
        this._databasesSubject$.next([]);

        // Assume the worst and clear the years array
        this.log.warn(`${LOG_PREFIX}Assuming the worst and clearing the years data`);
        this._yearsSubject$.next([]);
      }

    } else {

      // The selected database id is not different from the locally saved database id
      this.log.trace(`${LOG_PREFIX} The selected database id is not different from the locally saved database id`);

      // Ignore the value
      this.log.trace(`${LOG_PREFIX} Ignoring the change event`);
    }

    this.cd.detectChanges();

  }


  onStartYearChange(event: any) {

    this.log.trace(`${LOG_PREFIX} Start year change event received`);

    // Get the the selected start year
    this.log.trace(`${LOG_PREFIX} Getting the selected start year`);
    const year = this.filterForm.get("startYear")?.value;
    this.log.debug(`${LOG_PREFIX} Start Year = ${year}`);

    // Check if the selected start year is indeed different from the locally saved start year
    this.log.trace(`${LOG_PREFIX} Checking if the selected start year is indeed different from the locally saved start year`);

    if (year != this.selectedStartYear) {


      // Selected start year is different from the locally saved start year
      this.log.trace(`${LOG_PREFIX} Selected start year is indeed different from the locally saved start year`);

      // Update the locally saved start year
      this.log.trace(`${LOG_PREFIX} Updating the locally saved start year`);
      this.selectedStartYear = year;


      // Check if the selected start year is greater than the locally saved end year
      this.log.trace(`${LOG_PREFIX} Checking if the selected start year is greater than the locally saved end year`);

      if (year > this.selectedEndYear) {

        // The selected start year is greater than the locally saved end year
        this.log.trace(`${LOG_PREFIX} The selected start year is greater than the locally saved end year`);

        // Adjust the end year - to be equivalent to the start year
        this.log.trace(`${LOG_PREFIX} Adjusting the end year - to be equivalent to the start year`);
        this.selectedEndYear = this.selectedStartYear;
        this.filterForm.get("endYear")?.patchValue(year);

      } else {
        // The selected start year is not greater than the locally saved end year
        this.log.trace(`${LOG_PREFIX} The selected start year is not greater than the locally saved end year`);
      }

      // Call the global change management method
      this.log.trace(`${LOG_PREFIX} Calling the global change management method`);
      this.onChange();

    }

  }


  onEndYearChange(event: any) {

    this.log.trace(`${LOG_PREFIX} End year change event received`);

    // Get the the selected end year
    this.log.trace(`${LOG_PREFIX} Getting the selected end year`);
    const year = this.filterForm.get("endYear")?.value;
    this.log.debug(`${LOG_PREFIX} End Year = ${year}`);

    // Check if the selected end year is indeed different from the locally saved end year
    this.log.trace(`${LOG_PREFIX} Checking if the selected end year is indeed different from the locally saved end year`);

    if (year != this.selectedEndYear) {


      // Selected end year is different from the locally saved end year
      this.log.trace(`${LOG_PREFIX} Selected end year is indeed different from the locally saved end year`);

      // Update the locally saved end year
      this.log.trace(`${LOG_PREFIX} Updating the locally saved end year`);
      this.selectedEndYear = year;


      // Check if the selected end year is lesser than the locally saved end year
      this.log.trace(`${LOG_PREFIX} Checking if the selected end year is lesser than the locally saved end year`);

      if (year < this.selectedEndYear) {

        // The selected end year is lesser than the locally saved end year
        this.log.trace(`${LOG_PREFIX} The selected end year is lesser than the locally saved end year`);

        // Adjust the start year - to be equivalent to the end year
        this.log.trace(`${LOG_PREFIX} Adjusting the start year - to be equivalent to the end year`);
        this.selectedEndYear = this.selectedEndYear;
        this.filterForm.get("endYear")?.patchValue(year);

      } else {
        // The selected end year is not greater than the locally saved end year
        this.log.trace(`${LOG_PREFIX} The selected end year is not greater than the locally saved end year`);
      }

      // Call the global change management method
      this.log.trace(`${LOG_PREFIX} Calling the global change management method`);
      this.onChange();

    }

  }


  onLandUseChange(event: any) {

    this.log.trace(`${LOG_PREFIX} Land use change event received`);

    // Get the the selected land use
    this.log.trace(`${LOG_PREFIX} Getting the selected land use category id`);
    const id = this.filterForm.get("landUseCategoryId")?.value;
    this.log.debug(`${LOG_PREFIX} Land Use Category Id = ${id}`);

    // Check if the selected land use category id is indeed different from the locally saved land use
    this.log.trace(`${LOG_PREFIX} Checking if the selected land use category id is indeed different from the locally saved land use category id`);

    if (id != this.selectedLandUseId) {

      // The selected land use category id is different from the locally saved land use
      this.log.trace(`${LOG_PREFIX} The selected land use category id is different from the locally saved land use`);

      // Update the locally saved land use
      this.log.trace(`${LOG_PREFIX} Updating the locally saved land use`);
      this.selectedLandUseId = id;

      // Call the global change management method
      this.log.trace(`${LOG_PREFIX} Calling the global change management method`);
      this.onChange();

    } else {
      // The selected land use category id is not different from the locally saved land use
      this.log.trace(`${LOG_PREFIX} The selected land use category id is not different from the locally saved land use`);
    }

  }


  onPartyChange(event: any) {

    this.log.trace(`${LOG_PREFIX} Party change event received`);

    // Get the id of the selected party
    this.log.trace(`${LOG_PREFIX} Getting the id of the selected party`);
    const id: number = this.filterForm.get("partyId")?.value;
    this.log.debug(`${LOG_PREFIX} Party = ${id}`);

    // Check if the selected party id is indeed different from the locally saved party id
    this.log.trace(`${LOG_PREFIX} Checking if the selected party id is indeed different from the locally saved party id`);

    if (id != this.selectedPartyId) {

      // The selected party id is different from the locally saved party id
      this.log.trace(`${LOG_PREFIX} The selected party id is different from the locally saved party id`);

      // Update the locally saved party id
      this.log.trace(`${LOG_PREFIX} Updating the locally saved party id`);
      this.selectedPartyId = id;

      // Get the record of the party with the given id
      this.log.trace("Getting the record of the party with the given id");
      let party: Party | undefined = this._partiesSubject$.value.find(d => d.id == this.selectedPartyId);

      // Check if the record exists
      this.log.trace("Checking if the party record exists");

      if (party != undefined) {

        // The record exists
        this.log.trace("The party record exists");

        // Call the global change management method
        this.log.trace(`${LOG_PREFIX} Calling the global change management method`);
        this.onChange();


      } else {

        // The record does not exist
        this.log.warn(`${LOG_PREFIX} The party record does not exist`);

        // Assume the worst and clear the parties array
        this.log.warn(`${LOG_PREFIX}Assuming the worst and clearing the parties data`);
        this._partiesSubject$.next([]);
      }

    } else {

      // The selected party id is not different from the locally saved party id
      this.log.trace(`${LOG_PREFIX} The selected party id is not different from the locally saved party id`);

      // Ignore the value
      this.log.trace(`${LOG_PREFIX} Ignoring the change event`);
    }


  }


  onChange() {
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
