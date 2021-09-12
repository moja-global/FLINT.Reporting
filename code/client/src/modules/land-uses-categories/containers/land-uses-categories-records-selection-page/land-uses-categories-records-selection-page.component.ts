import { ChangeDetectionStrategy, Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Land Uses Categories Records Tabulation Page]";

@Component({
    selector: 'sb-land-uses-categories-records-selection-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './land-uses-categories-records-selection-page.component.html',
    styleUrls: ['land-uses-categories-records-selection-page.component.scss'],
})
export class LandUsesCategoriesRecordsSelectionPageComponent implements OnInit {

    // Instantiate and avail a reportingFrameworkId field to the parent component.
    // This will allow the parent component to initialize previously selected domain id e.g. in the case of an update.
    @Input() reportingFrameworkId: number | null = null;    

    // Instantiate and avail a selectedDomainActivitiesIds arrays to the parent component.
    // This will allow the parent component to initialize previously selected domain activities ids e.g. in the case of an update.
    // This array will contain a single item during single-selection mode and multiple items during multi-select mode
    @Input() selectedDomainActivitiesIds: number[] = [];
    
    // Instantiate and avail a selection mode flag to the parent component.
    // This will allow the parent component to specify whether single-selection or multi-selection mode is required.
    // Single and Multi Selection Mode will then be executed through Radio Buttons and Checkboxes respectively.
    @Input() selectionMode: string = "single";     

    // Propogates Radio Buttons Selection Events to the Parent Component
    @Output() select: EventEmitter<number> = new EventEmitter<number>();

    // Propogates Checkboxes Check Events to the Parent Component
    @Output() check: EventEmitter<number> = new EventEmitter<number>();    

    // Propogates Checkboxes Uncheck Events to the Parent Component
    @Output() uncheck: EventEmitter<number> = new EventEmitter<number>();       

    constructor(public connectivityStatusService: ConnectivityStatusService, private log: NGXLogger) { }

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }

    /** 
    * Propagates LandUseCategory Selection Event to the Parent Component
    * @param id The id of the Selected LandUseCategory
    */
     onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected LandUseCategory Id: ${id}`);

        // Push the Selected LandUseCategory Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates LandUsesCategories Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked LandUseCategory
    */
     onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked LandUseCategory Id: ${id}`);

        // Notify the Parent Component
        this.check.emit(id);
    }  
    
    
    /** 
    * Propagates LandUsesCategories Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked LandUseCategory
    */
     onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked LandUseCategory Id: ${id}`);

        // Notify the Parent Component
        this.uncheck.emit(id);
    }     

}
