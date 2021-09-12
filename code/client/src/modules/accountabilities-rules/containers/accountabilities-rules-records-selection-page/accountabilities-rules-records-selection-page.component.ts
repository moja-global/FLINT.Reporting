import { ChangeDetectionStrategy, Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Accountabilities Rules Records Tabulation Page]";

@Component({
    selector: 'sb-accountabilities-rules-records-selection-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './accountabilities-rules-records-selection-page.component.html',
    styleUrls: ['accountabilities-rules-records-selection-page.component.scss'],
})
export class AccountabilitiesRulesRecordsSelectionPageComponent implements OnInit {

    // Instantiate and avail a previously selected Accountabilities Types Ids arrays to the parent component.
    // This will allow the parent component to specify the previously selected Accountabilities Types Ids.
    // This might then be used to disable the previously selected Accountabilities Types Ids if the next configuration is set to true
    @Input() previouslySelectedAccountabilitiesRulesIds: number[] = [];       

    // Instantiate and avail a 'skip previously selected Accountabilities Types Ids' flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be skipped. 
    @Input() skipPreviouslySelectedAccountabilitiesRulesIds:boolean = true;     
    
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
    * Propagates AccountabilityType Selection Event to the Parent Component
    * @param id The id of the Selected AccountabilityType
    */
     onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected AccountabilityType Id: ${id}`);

        // Push the Selected AccountabilityType Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates AccountabilitiesRules Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked AccountabilityType
    */
     onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked AccountabilityType Id: ${id}`);

        // Notify the Parent Component
        this.check.emit(id);
    }  
    
    
    /** 
    * Propagates AccountabilitiesRules Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked AccountabilityType
    */
     onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked AccountabilityType Id: ${id}`);

        // Notify the Parent Component
        this.uncheck.emit(id);
    }     

}
