import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { LoadingAnimationComponent, PaginationComponent } from '@common/components';
import { SortEvent } from '@common/directives/sortable.directive';
import { AccountabilitiesTypesDataService } from '@modules/accountabilities-types/services/accountabilities-types-data.service';
import { Filter } from '@modules/filters/models/filter.model';
import { AdministrativeUnitsFilterTabulationService } from '@modules/filters/services/administrative-units-filter-tabulation.service';
import { FilterService } from '@modules/filters/services/filter.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Administrative Units Filter Component]";

export interface AdministrativeUnitsSelectionPayload {
  index: number | null,
  accountabilityTypeId: number | null,
  accountabilityId: number
}

@Component({
  selector: 'sb-administrative-units-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './administrative-units-filter.component.html',
  styleUrls: ['administrative-units-filter.component.scss'],
})
export class AdministrativeUnitsFilterComponent implements OnInit {

  // Inject a reference to the loading animation component. 
  // This will provide a way of informing it of the status of 
  // the processing events happening in the background.
  @ViewChild(LoadingAnimationComponent) animation!: LoadingAnimationComponent;

  // Inject a reference to the pagination component.
  // This will provide a way for setting the initial page / page size settings, 
  // and thereafter provide a way for updating the changes in record totals following record filtering.
  @ViewChild(PaginationComponent) pagination!: PaginationComponent;

  // Instantiate and avail a page size variable to the parent component.
  // This will allow the parent component to set the desired page size i.e. maximum number of records per page.
  // This could differ depending on where the table is displayed:
  // For example, a few of the table records can be displayed on the dashboard and 
  // a full set of the table records can be displayed on the table's home page.
  @Input() pageSize: number = 4;

  // Instantiate and avail a selection mode flag to the parent component.
  // This will allow the parent component to specify whether single-selection or multi-selection mode is required.
  // Single and Multi Selection Mode will then be executed through Radio Buttons and Checkboxes respectively.
  @Input() selectionMode: string = "single";

  // Instantiate and avail an accountabilityTypeId field to the parent component.
  // This will allow the parent component to initialize previously selected accountability type id e.g. in the case of an update.
  @Input() accountabilityTypeId: number | null = -1;

  // Instantiate and avail a selectedAccountabilitiesIds arrays to the parent component.
  // This will allow the parent component to initialize previously selected accountabilities ids e.g. in the case of an update.
  // This array will contain a single item during single-selection mode and multiple items during multi-select mode
  @Input() selectedAccountabilitiesIds: number[] = [];

  // Propogates Reset Events to the Parent Component when all selections get cleared
  @Output() reset: EventEmitter<void> = new EventEmitter<void>();

  // Propogates Radio Buttons Selection Events to the Parent Component
  @Output() select: EventEmitter<AdministrativeUnitsSelectionPayload> = new EventEmitter<AdministrativeUnitsSelectionPayload>();

  // Propogates Checkboxes Check Events to the Parent Component
  @Output() check: EventEmitter<AdministrativeUnitsSelectionPayload> = new EventEmitter<AdministrativeUnitsSelectionPayload>();

  // Propogates Checkboxes Uncheck Events to the Parent Component
  @Output() uncheck: EventEmitter<AdministrativeUnitsSelectionPayload> = new EventEmitter<AdministrativeUnitsSelectionPayload>();

  // Keep tabs on the column that the records are currently sorted by.
  sortedColumn!: string;

  // Keep tabs on the direction that the records are currently sorted by: ascending or descending.
  sortedDirection!: string;

  // Instantitate a new Reactive Form Group for the Filter Form.
  // This will allow us to define and enforce the validation rules for all the form controls.
  filtersForm = new FormGroup({
    accountabilityTypeId: new FormControl('')
  });

  public error: string | null = null;

  // Instantiate a central gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    public filterService: FilterService,
    public accountabilitiesTypesDataService: AccountabilitiesTypesDataService,
    public administrativeUnitsFilterTabulationService: AdministrativeUnitsFilterTabulationService,
    private cd: ChangeDetectorRef,
    private log: NGXLogger) { }

  ngOnInit() {

    this.log.trace(`${LOG_PREFIX} Initializing Component`);

    this._subscriptions.push(
      this.filterService.filter$.subscribe((filter: Filter) => {

        // Set the selected accountability type value on the UI
        this.log.trace(`${LOG_PREFIX} Setting the selected accountability type value on the UI`);
        this.accountabilityTypeId = filter.accountabilityTypeId;

      }));


  }

  ngAfterViewInit() {

    // Set the initial page and page size values on the pagination component.
    this.log.trace(`${LOG_PREFIX} Set the initial Page and Page Size values on the pagination component`);
    this.pagination.initialize(this.administrativeUnitsFilterTabulationService.page, this.pageSize);

    // Subscribe to the total value changes and propagate them to the pagination component.
    // These values typically change in response to the user filtering the records.
    this.log.trace(`${LOG_PREFIX} Subscribing to total value changes`);
    this._subscriptions.push(
      this.administrativeUnitsFilterTabulationService.total$.subscribe(
        (total) => {
          this.pagination.total = total;
          this.cd.detectChanges();
        }));

    // Subscribe to loading events and propagate them to the loading component.
    // Loading events occur when the user searches, sorts or moves from one record page to another.
    this.log.trace(`${LOG_PREFIX} Subscribing to loading status changes`);
    this._subscriptions.push(
      this.administrativeUnitsFilterTabulationService.loading$.subscribe(
        (loading) => {
          this.animation.loading = loading;
          this.cd.detectChanges();
        }));

  }


  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear the current search
    this.administrativeUnitsFilterTabulationService.searchTerm = "";

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  /**
   * Propagates search events to the table service
   * @param event The term to search by
   */
  onSearch(event: any) {
    this.log.trace(`${LOG_PREFIX} Searching for ${event}`);
    this.administrativeUnitsFilterTabulationService.searchTerm = event;
    this.cd.detectChanges();
  }

  /**
   * Propagates sort events to the table service
   * @param param0 The column / direction to sort by
   */
  onSort({ column, direction }: SortEvent) {
    this.log.trace(`${LOG_PREFIX} Sorting ${column} in ${direction} order`);
    this.sortedColumn = column;
    this.sortedDirection = direction;
    this.administrativeUnitsFilterTabulationService.sortColumn = column;
    this.administrativeUnitsFilterTabulationService.sortDirection = direction;
    this.cd.detectChanges();
  }

  /**
   * Propagates page change events to the table service
   * @param event The page to load
   */
  onPageChange(event: any) {
    this.log.trace(`${LOG_PREFIX} Changing Page to ${event}`);
    this.administrativeUnitsFilterTabulationService.page = event;
    this.cd.detectChanges();
  }

  /**
   * Propagates page size change events to the table service
   * @param event The newly desired page size
   */
  onPageSizeChange(event: any) {
    this.log.trace(`${LOG_PREFIX} Changing Page Size to ${event}`);
    this.administrativeUnitsFilterTabulationService.pageSize = event;
    this.cd.detectChanges();
  }


  onAccountabilityTypeIdChange(e: any) {

    this.log.trace(`${LOG_PREFIX} Entering onAccountabilityTypeIdChange()`);
    this.log.debug(`${LOG_PREFIX} Accountability Type Id = ${e.target.value}`);


    // Clear any previously selected records
    if (this.selectedAccountabilitiesIds.length > 0) {
      this.selectedAccountabilitiesIds = [];
      this.reset.emit();
    }

    // Clear the current search
    this.administrativeUnitsFilterTabulationService.searchTerm = "";

    // Update the accountability type
    this.filterService.onAccountabilityTypeIdChange(e.target.value);

    this.cd.detectChanges();
  }

  /** 
  * Propagates Accountabilities Selection Event to the Parent Component
  * @param id The id of the Selected Accountability
  */
  onSelect(id: number) {
    this.log.trace(`${LOG_PREFIX} Selected Accountability Id: ${id}`);

    // Update the Selected Accountabilities Ids array
    this.selectedAccountabilitiesIds = [id];

    // Push the Selected Accountability Id to the Parent Component
    this.select.emit({ index: 0, accountabilityTypeId: this.accountabilityTypeId, accountabilityId: id});
  }


  /** 
  * Propagates Accountabilities Checkboxes Check Events to the Parent Component
  * @param id The id of the Checked Accountability
  */
  onCheck(id: number) {

    this.log.trace(`${LOG_PREFIX} Checked Accountability Id: ${id}`);

    // Get the index of the checked Accountability
    // If has already been inserted into the Selected Accountabilities Ids array, the index will be -1
    let index: number = this.selectedAccountabilitiesIds.indexOf(id);

    // If the Accountability hasn't been inserted into the Accountabilities array, insert it and update the index value.
    // Please note that the index value should be a single step up from the last index value.
    // This will avoid reuse of the same index values when middle-placed values are deleted from the array
    if (index == -1) {
      index = this.selectedAccountabilitiesIds.length > 0 ? ((this.selectedAccountabilitiesIds[this.selectedAccountabilitiesIds.length - 1]) + 1) : 1;
      this.selectedAccountabilitiesIds.push(id);
    }

    // Notify the Parent Component
    this.check.emit({ index: index, accountabilityTypeId: this.accountabilityTypeId, accountabilityId: id });
  }


  /** 
  * Propagates Accountabilities Checkboxes Uncheck Events to the Parent Component
  * @param id The id of the Unchecked Accountability
  */
  onUncheck(id: number) {

    this.log.trace(`${LOG_PREFIX} Unchecked Accountability Id: ${id}`);

    // Get the index of the unchecked Accountability in the Selected Accountabilities Ids array
    // If its non-existent, the index will be -1
    let index: number = this.selectedAccountabilitiesIds.indexOf(id);

    // Remove the Accountability from the array
    if (index != -1) {
      this.selectedAccountabilitiesIds.splice(index, 1);
    }

    // Notify the Parent Component
    this.check.emit({ index: index, accountabilityTypeId: this.accountabilityTypeId, accountabilityId: id });

  }

  isSelected(accountabilityId: number) {
    return this.selectedAccountabilitiesIds.some(id => id == accountabilityId);
  }

  isChecked(accountabilityId: number) {
    return this.selectedAccountabilitiesIds.some(id => id == accountabilityId);
  }

  onDrillDown(e: any) {

    // Drill down
    this.filterService.onDrillDown(e);

    // Clear the current search
    this.administrativeUnitsFilterTabulationService.searchTerm = "";
  }

  onDrillUp() {

    // Drill up
    this.filterService.onDrillUp();

    // Clear the current search
    this.administrativeUnitsFilterTabulationService.searchTerm = "";
  }

}


