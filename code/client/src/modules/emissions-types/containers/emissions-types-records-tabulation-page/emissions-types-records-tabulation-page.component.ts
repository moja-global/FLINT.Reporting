import { ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Emissions Types Records Tabulation Page]";

@Component({
    selector: 'sb-emissions-types-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './emissions-types-records-tabulation-page.component.html',
    styleUrls: ['emissions-types-records-tabulation-page.component.scss'],
})
export class EmissionsTypesRecordsTabulationPageComponent implements OnInit, OnDestroy {


    constructor(
        private cd: ChangeDetectorRef,
        private log: NGXLogger) { }

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }    

}
