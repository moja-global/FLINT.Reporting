import { ChangeDetectionStrategy, Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Pools Records Tabulation Page]";

@Component({
    selector: 'sb-pools-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './pools-records-tabulation-page.component.html',
    styleUrls: ['pools-records-tabulation-page.component.scss'],
})
export class PoolsRecordsTabulationPageComponent implements OnInit {

    constructor(public connectivityStatusService: ConnectivityStatusService, private log: NGXLogger) {}

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }
      
}
