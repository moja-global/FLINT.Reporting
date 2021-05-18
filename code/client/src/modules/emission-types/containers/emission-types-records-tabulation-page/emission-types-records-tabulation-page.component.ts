import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';

const LOG_PREFIX: string = "[Emission Types Records Tabulation Page]";

@Component({
    selector: 'sb-emission-types-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './emission-types-records-tabulation-page.component.html',
    styleUrls: ['emission-types-records-tabulation-page.component.scss'],
})
export class EmissionTypesRecordsTabulationPageComponent implements OnInit {

    constructor(public connectivityStatusService: ConnectivityStatusService,) {}

    ngOnInit() {}
      
}
