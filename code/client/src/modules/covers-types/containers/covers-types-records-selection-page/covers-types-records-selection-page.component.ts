import { ChangeDetectionStrategy, Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Covers Types Records Tabulation Page]";

@Component({
    selector: 'sb-covers-types-records-selection-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './covers-types-records-selection-page.component.html',
    styleUrls: ['covers-types-records-selection-page.component.scss'],
})
export class CoversTypesRecordsSelectionPageComponent implements OnInit {

    // Instantiate and avail a selected CoverType Ids arrays to the parent component.
    // This will allow the parent component to initialize previously selected CoverType Ids e.g. in the case of an update.
    // This array will contain a single item during single-selection mode and multiple items during multi-select mode
    @Input() selectedCoverTypeIds: number[] = [];

    // Instantiate and avail a selected coversTypes treatment flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be disabled. 
    @Input() disableSelectedCoverTypeIdsTreatment = "false"       
    
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
    * Propagates CoverType Selection Event to the Parent Component
    * @param id The id of the Selected CoverType
    */
     onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected CoverType Id: ${id}`);

        // Push the Selected CoverType Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates CoversTypes Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked CoverType
    */
     onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked CoverType Id: ${id}`);

        // Notify the Parent Component
        this.check.emit(id);
    }  
    
    
    /** 
    * Propagates CoversTypes Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked CoverType
    */
     onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked CoverType Id: ${id}`);

        // Notify the Parent Component
        this.uncheck.emit(id);
    }     

}
