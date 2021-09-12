import { ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Reporting Frameworks Records Tabulation Page]";

@Component({
    selector: 'sb-reporting-frameworks-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-frameworks-records-tabulation-page.component.html',
    styleUrls: ['reporting-frameworks-records-tabulation-page.component.scss'],
})
export class ReportingFrameworksRecordsTabulationPageComponent implements OnInit, OnDestroy {


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
