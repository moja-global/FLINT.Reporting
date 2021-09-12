import { ChangeDetectionStrategy, Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Accountabilities Records Tabulation Page]";

@Component({
    selector: 'sb-accountabilities-records-selection-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-records-selection-page.component.html',
    styleUrls: ['accountabilities-records-selection-page.component.scss'],
})
export class AccountabilitiesRecordsSelectionPageComponent implements OnInit {

    // Instantiate and avail a selected Accountability Ids arrays to the parent component.
    // This will allow the parent component to initialize previously selected Accountability Ids e.g. in the case of an update.
    // This array will contain a single item during single-selection mode and multiple items during multi-select mode
    @Input() previouslySelectedAccountabilitiesIds: number[] = [];

    // Instantiate and avail a selected accountabilities treatment flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be disabled. 
    @Input() disableSelectedAccountabilityIdsTreatment = "false"       
    
    // Instantiate and avail a selection mode flag to the parent component.
    // This will allow the parent component to specify whether single-selection or multi-selection mode is required.
    // Single and Multi Selection Mode will then be executed through Radio Buttons and Checkboxes respectively.
    @Input() selectionMode: string = "single";  
    
    // Instantiate and avail a skip id field to the parent component.
    // This will allow the parent component to specify the accountability that should be skipped when listing the selection options
    @Input() skipId: number = -1;      

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
    * Propagates Accountability Selection Event to the Parent Component
    * @param id The id of the Selected Accountability
    */
     onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected Accountability Id: ${id}`);

        // Push the Selected Accountability Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates Accountabilities Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked Accountability
    */
     onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked Accountability Id: ${id}`);

        // Notify the Parent Component
        this.check.emit(id);
    }  
    
    
    /** 
    * Propagates Accountabilities Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked Accountability
    */
     onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked Accountability Id: ${id}`);

        // Notify the Parent Component
        this.uncheck.emit(id);
    }     

}
