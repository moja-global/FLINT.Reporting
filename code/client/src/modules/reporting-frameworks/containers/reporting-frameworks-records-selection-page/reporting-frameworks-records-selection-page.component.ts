import { ChangeDetectionStrategy, Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Reporting Frameworks Records Tabulation Page]";

@Component({
    selector: 'sb-reporting-frameworks-records-selection-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-frameworks-records-selection-page.component.html',
    styleUrls: ['reporting-frameworks-records-selection-page.component.scss'],
})
export class ReportingFrameworksRecordsSelectionPageComponent implements OnInit {

    // Instantiate and avail a selected ReportingFramework Ids arrays to the parent component.
    // This will allow the parent component to initialize previously selected ReportingFramework Ids e.g. in the case of an update.
    // This array will contain a single item during single-selection mode and multiple items during multi-select mode
    @Input() selectedReportingFrameworkIds: number[] = [];

    // Instantiate and avail a selected reportingFrameworks treatment flag to the parent component.
    // This will allow the parent component to specify whether previously selected options will be disabled. 
    @Input() disableSelectedReportingFrameworkIdsTreatment = "false"       
    
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
    * Propagates ReportingFramework Selection Event to the Parent Component
    * @param id The id of the Selected ReportingFramework
    */
     onSelect(id: number) {
        this.log.trace(`${LOG_PREFIX} Selected ReportingFramework Id: ${id}`);

        // Push the Selected ReportingFramework Id to the Parent Component
        this.select.emit(id);
    }


    /** 
    * Propagates ReportingFrameworks Checkboxes Check Events to the Parent Component
    * @param id The id of the Checked ReportingFramework
    */
     onCheck(id: number) {
        this.log.trace(`${LOG_PREFIX} Checked ReportingFramework Id: ${id}`);

        // Notify the Parent Component
        this.check.emit(id);
    }  
    
    
    /** 
    * Propagates ReportingFrameworks Checkboxes Uncheck Events to the Parent Component
    * @param id The id of the Unchecked ReportingFramework
    */
     onUncheck(id: number) {

        this.log.trace(`${LOG_PREFIX} Unchecked ReportingFramework Id: ${id}`);

        // Notify the Parent Component
        this.uncheck.emit(id);
    }     

}
