import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';

const LOG_PREFIX: string = "[Domains Records Tabulation Page]";

@Component({
    selector: 'sb-domains-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './domains-records-tabulation-page.component.html',
    styleUrls: ['domains-records-tabulation-page.component.scss'],
})
export class DomainsRecordsTabulationPageComponent implements OnInit {

    constructor(public connectivityStatusService: ConnectivityStatusService,) {}

    ngOnInit() {}
      
}
