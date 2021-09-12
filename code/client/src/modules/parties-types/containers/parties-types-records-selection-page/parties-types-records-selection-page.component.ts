import { ChangeDetectionStrategy, Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Parties Types Records Tabulation Page]";

@Component({
    selector: 'sb-parties-types-records-selection-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './parties-types-records-selection-page.component.html',
    styleUrls: ['parties-types-records-selection-page.component.scss'],
})
export class PartiesTypesRecordsSelectionPageComponent implements OnInit {

    // Instantiate and avail a previously selected Party Types Ids arrays to the parent component.
    // This will allow the parent component to specify the previously selected Party Types Ids.
    // This might then be used to disable the previously selected Party Types Ids if the next configuration is set to true
    @Input() previouslySelectedPartiesTypesIds: number[] = [];       

    // Instantiate and avail a 'skip previously selected Party Types Ids' flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be skipped. 
    @Input() skipPreviouslySelectedPartiesTypesIds:boolean = true;      
    
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
    * Propagates PartyType Selection Event to the Parent Component
    * @param id The id of the Selected PartyType
    */
     onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected PartyType Id: ${id}`);

        // Push the Selected PartyType Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates PartiesTypes Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked PartyType
    */
     onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked PartyType Id: ${id}`);

        // Notify the Parent Component
        this.check.emit(id);
    }  
    
    
    /** 
    * Propagates PartiesTypes Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked PartyType
    */
     onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked PartyType Id: ${id}`);

        // Notify the Parent Component
        this.uncheck.emit(id);
    }     

}
