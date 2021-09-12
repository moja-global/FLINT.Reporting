import { ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Fluxes Types Records Tabulation Page]";

@Component({
    selector: 'sb-fluxes-types-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './fluxes-types-records-tabulation-page.component.html',
    styleUrls: ['fluxes-types-records-tabulation-page.component.scss'],
})
export class FluxesTypesRecordsTabulationPageComponent implements OnInit, OnDestroy {


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
