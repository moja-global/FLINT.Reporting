import { ChangeDetectionStrategy, Component, HostListener, OnInit} from '@angular/core';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Reporting Variables Records Tabulation Page]";

@Component({
    selector: 'sb-reporting-variables-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-variables-records-tabulation-page.component.html',
    styleUrls: ['reporting-variables-records-tabulation-page.component.scss'],
})
export class ReportingVariablesRecordsTabulationPageComponent implements OnInit {

    constructor(private log: NGXLogger) { }

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }

}
