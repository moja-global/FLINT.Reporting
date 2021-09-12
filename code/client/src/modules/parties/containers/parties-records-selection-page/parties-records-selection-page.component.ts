import { ChangeDetectionStrategy, Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Parties Records Tabulation Page]";

@Component({
    selector: 'sb-parties-records-selection-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './parties-records-selection-page.component.html',
    styleUrls: ['parties-records-selection-page.component.scss'],
})
export class PartiesRecordsSelectionPageComponent implements OnInit {

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

    // Instantiate and avail a basis flag to the parent component.
    // This will allow the parent component to specify whether the selection should be based on parent or subsidiary parties or on default settings.
    @Input() basis: string = "default"; // parents, subsidiaries, default

    // Instantiate and avail a partyTypeId flag to the parent component.
    // This will allow the parent component to specify the party type of the records to be listed for selection
    @Input() partyTypeId: number = -1;

    // Instantiate and avail a previously selected Parties arrays to the parent component.
    // This will allow the parent component to specify the previously selected Parties.
    // This might then be used to disable the previously selected Parties if the next configuration is set to true
    @Input() previouslySelectedPartiesIds: number[] = [];

    // Instantiate and avail a 'skip previously selected Parties' flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be skipped. 
    @Input() skipPreviouslySelectedPartiesIds: boolean = true;

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
    * Propagates Party Selection Event to the Parent Component
    * @param id The id of the Selected Party
    */
     onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected Party Id: ${id}`);

        // Push the Selected Party Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates Parties Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked Party
    */
     onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked Party Id: ${id}`);

        // Notify the Parent Component
        this.check.emit(id);
    }  
    
    
    /** 
    * Propagates Parties Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked Party
    */
     onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked Party Id: ${id}`);

        // Notify the Parent Component
        this.uncheck.emit(id);
    }     

}
