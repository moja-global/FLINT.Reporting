import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';

const LOG_PREFIX: string = "[Pools Records Tabulation Page]";

@Component({
    selector: 'sb-pools-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './pools-records-tabulation-page.component.html',
    styleUrls: ['pools-records-tabulation-page.component.scss'],
})
export class PoolsRecordsTabulationPageComponent implements OnInit {

    constructor(public connectivityStatusService: ConnectivityStatusService,) {}

    ngOnInit() {}
      
}
